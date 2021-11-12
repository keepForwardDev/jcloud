package com.jcloud.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jcloud.bean.*;
import com.jcloud.consts.Const;
import com.jcloud.consts.PrivilegesType;
import com.jcloud.consts.ResType;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.core.exception.BizException;
import com.jcloud.core.service.BCryptPasswordEncoder;
import com.jcloud.core.service.DefaultOrmService;
import com.jcloud.core.service.PasswordEncoder;
import com.jcloud.entity.Resource;
import com.jcloud.entity.Role;
import com.jcloud.entity.User;
import com.jcloud.entity.UserRole;
import com.jcloud.mapper.DepartmentMapper;
import com.jcloud.mapper.ResourceMapper;
import com.jcloud.mapper.UserMapper;
import com.jcloud.mapper.UserRoleMapper;
import com.jcloud.service.LoginService;
import com.jcloud.service.MenuService;
import com.jcloud.service.UserService;
import com.jcloud.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends DefaultOrmService<UserMapper, User, UserBean> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserPrivilegesServiceImpl userPrivilegesService;

    @Value("${system.default-password:123456}")
    private String defaultPassword;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private List<LoginService> loginServices;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public UserBean convert(User user) {
        UserBean bean = new UserBean();
        BeanUtils.copyProperties(user, bean);
        bean.setDepartmentId(user.getDepartmentId());
        if (user.getDepartmentId() != null) {
            bean.setDepartmentName(departmentMapper.selectById(user.getDepartmentId()).getName());
        }
        List<Role> userRoles = userRoleMapper.findByUserId(user.getId());
        bean.setRoleIds(userRoles.stream().map(r -> r.getId()).collect(Collectors.toList()));
        return bean;
    }

    public ResponseData saveUser(UserBean bean) {
        ResponseData responseResult = new ResponseData();
        try {
            User user = saveEntity(bean);
            responseResult.setCode(Const.CODE_SUCCESS);
            responseResult.setMsg(Const.CODE_SUCCESS_STR);
            responseResult.setReserveData(user.getId());
        } catch (DuplicateKeyException ex) {
            responseResult.setMsg("该账号已被注册！");
        } catch (BizException ex) {
            responseResult.setMsg(ex.getMessage());
        }
        return responseResult;
    }

    public QueryWrapper<User> queryCondition(UserBean bean) {
        QueryWrapper<User> condition = super.queryCondition(bean);
        ShiroUser user = SecurityUtil.getCurrentUser();
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.apply(SqlHelper.getSqlLike("name", 0), SqlHelper.getSqlLikeParams(bean.getName()));
        }
        if (StringUtils.isNotBlank(bean.getPhone())) {
            condition.apply(SqlHelper.getSqlLike("phone", 0), SqlHelper.getSqlLikeParams(bean.getPhone()));
        }
        if (StringUtils.isNotBlank(bean.getEmail())) {
            condition.apply(SqlHelper.getSqlLike("email", 0), SqlHelper.getSqlLikeParams(bean.getEmail()));
        }
        if (bean.getDepartmentId() != null) {
            condition.eq("department_id", bean.getDepartmentId());
        }
        if (bean.getRoleId() != null) {
            condition.apply("id in (select user_id from sys_user_role where role_id= {0})", bean.getRoleId());
        }
        return condition;
    }


    /**
     * 账号跟 手机号不能重复
     *
     * @param entity
     * @param bean
     */
    @Override
    protected void beforeSave(User entity, UserBean bean) {
        // 重复性条件构建
        QueryWrapper<User> condition = new QueryWrapper<>();
        condition.eq("deleted", 0);
        boolean notEmptyAccount = StringUtils.isNotBlank(entity.getAccount());
        boolean notEmptyPhone = StringUtils.isNotBlank(entity.getPhone());
        if (notEmptyAccount || notEmptyPhone) {
            condition.and(query -> {
                if (notEmptyAccount) {
                    query.or().eq("account", entity.getAccount());
                }
                if (notEmptyPhone) {
                    query.or().eq("phone", entity.getPhone());
                }
            });
        }
        if (entity.getId() != null) {
            // 不包含本身
            condition.ne("id", entity.getId());
        }
        // 重复性校验
        User user = getOne(condition, false);
        if (user != null) {
            if (notEmptyAccount && StringUtils.equals(entity.getAccount(), user.getAccount())) {
                throw new BizException("该账号已经存在！");
            }
            if (notEmptyPhone && StringUtils.equals(entity.getPhone(), user.getPhone())) {
                throw new BizException("该手机号已被注册！");
            }
        }
        if (entity.getId() != null) {
            // 删除角色关联关系
            QueryWrapper userWrapper = new QueryWrapper<>();
            userWrapper.eq("user_id", entity.getId());
            userRoleMapper.delete(userWrapper);
        }
        // 密码
        if (entity.getId() == null) {
            entity.setPassword(bCryptPasswordEncoder.encode(defaultPassword));
        }
    }

    @Override
    protected void afterSave(User entity, UserBean bean) {
        for (Long roleId : bean.getRoleIds()) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(entity.getId());
            userRoleMapper.insert(userRole);
        }
    }


    public List<String> getIgnoreProperties() {
        List<String> IGNORE_FIELDS = super.getIgnoreProperties();
        IGNORE_FIELDS.add("account");
        IGNORE_FIELDS.add("password");
        IGNORE_FIELDS.add("openid");
        IGNORE_FIELDS.add("unionId");
        IGNORE_FIELDS.add("sourceFrom");
        IGNORE_FIELDS.add("type");
        return IGNORE_FIELDS;
    }

    public String resetPassword(List<Long> idList) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String randomString = RandomUtil.getRandomString(6);
        idList.forEach(id -> {
            Optional.ofNullable(getById(id)).ifPresent(user -> {
                user.setPassword(encoder.encode(randomString));
                updateById(user);
            });
        });
        return randomString;
    }


    @Override
    public UserBean findByUuid(String uuid) {
        User user = baseMapper.findByUuid(uuid);
        UserBean bean = null;
        if (user != null) {
            bean = convert(user);
        }
        return bean;
    }

    @Override
    public ResponseData changeAvatar(String avatar) {
        ShiroUser user = SecurityUtil.getCurrentUser();
        User u = getById(user.getId());
        u.setAvatar(avatar);
        updateById(u);
        return ResponseData.getSuccessInstance();
    }

    @Override
    public ResponseData editPassword(String oldPassword, String newPassword) {
        ResponseData respon = new ResponseData();
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return respon;
        }
        ShiroUser user = SecurityUtil.getCurrentUser();
        User u = getById(user.getId());
        if (bCryptPasswordEncoder.matches(oldPassword, u.getPassword())) {
            u.setPassword(bCryptPasswordEncoder.encode(newPassword));
            updateById(u);
            respon = ResponseData.getSuccessInstance();
        } else {
            respon.setMsg("原始密码不对，请重新输入！");
        }
        return respon;
    }

    @Override
    public boolean checkByPhone(String phone, Long id) {
        User user = findByPhone(phone);
        return user == null || (id != null && user.getId().longValue() == id.longValue());
    }


    @Override
    public ResponseData loadUserByUnionId(Map<String, Object> info) {
        String unionId = ObjectUtil.toStr(info.get("unionId"));
        User user = findByAccount(unionId, "union_id");
        if (user == null) { // register user
            UserBean userBean = new UserBean();
            userBean.setName(ObjectUtil.toStr(info.get("nickname")));
            userBean.setUnionId(unionId);
            userBean.setType(1);
            userBean.setOpenid(ObjectUtil.toStr(info.get("openid")));
            userBean.setAvatar(ObjectUtil.toStr(info.get("headImgUrl")));
            userBean.setAccount(unionId);
            userBean.setEnabled(1);
            userBean.setSex(Integer.valueOf(TypeUtil.toStr(info.get("sex"))));
            userBean.setRemark(ObjectUtil.toStr(info.get("country")) + ObjectUtil.toStr(info.get("province")) + ObjectUtil.toStr(info.get("city")));
            saveUser(userBean);
            user = findByAccount(unionId, "union_id");
        }
        ResponseData commonRespon = new ResponseData();
        if (user != null) {
            commonRespon = ResponseData.getSuccessInstance();
            commonRespon.setData(BeanUtil.beanToMap(getShiroAccount(user)));
        }
        return commonRespon;
    }

    @Override
    public ResponseData loadUserByPhone(String phone) {
        ResponseData commonRespon = new ResponseData();
        User user = findByAccount(phone, "phone");
        if (user != null) {
            commonRespon = ResponseData.getSuccessInstance();
            commonRespon.setData(BeanUtil.beanToMap(getShiroAccount(user)));
        }
        return commonRespon;
    }

    @Override
    public ResponseData<Long> simpleRegUser(String userName, String phone, Long sourceFrom) {
        if (StringUtils.isNotBlank(phone)) {
            return new ResponseData();
        }
        ResponseData responseData = ResponseData.getSuccessInstance();
        User user = findByAccount(phone, "phone");
        if (user == null) { // register user
            UserBean userBean = new UserBean();
            userBean.setName(userName);
            userBean.setType(1);
            userBean.setAccount(phone);
            userBean.setPhone(phone);
            userBean.setEnabled(1);
            userBean.setSourceFrom(sourceFrom);
            responseData.setData(saveUser(userBean).getReserveData());
        } else {
            responseData.setData(user.getId());
        }
        return responseData;
    }

    @Override
    public ResponseData<List<ShiroUser>> getUserByIds(List<Long> ids) {
        ResponseData responseData = ResponseData.getSuccessInstance();
        List<User> users = baseMapper.selectBatchIds(ids);
        List<ShiroUser> shiroUsers = users.stream().map(r -> {
            ShiroUser user = new ShiroUser();
            BeanUtils.copyProperties(r, user);
            return user;
        }).collect(Collectors.toList());
        responseData.setData(shiroUsers);
        return responseData;
    }

    @Cacheable(value = "redisCacheManager", key = "'user_id_' + #phone")
    @Override
    public Long getUserIdByPhone(String phone) {
        User user = findByAccount(phone, "phone");
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    @Override
    public ResponseData doLogin(String username, String password, String code, String loginType, Integer info) {
        ResponseData responseData = new ResponseData();
        ShiroUser shiroUser = null;
        if (StringUtils.isBlank(username)) {
            return null;
        }
        for (LoginService loginService : loginServices) {
            if (!loginService.checkCode(code)) {
                responseData.setMsg(Const.CODE_ERROR_CODE_STR);
            }
            if (loginService.supports(loginType)) {
                shiroUser = loginService.doLogin(username, password, code);
                break;
            }
        }
        if (shiroUser == null) {
            responseData.setMsg(Const.USER_NOT_FOUND);
        } else {
            setShiroPrivileges(shiroUser);
            SecurityUtil.setCurrentUser(shiroUser);
            StpUtil.login(shiroUser.getId());
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setCode(Const.CODE_SUCCESS);
            if (BooleanUtil.numberToBoolean(info)) {
                responseData.setData(shiroUser);
            }
            responseData.setReserveData(StpUtil.getTokenInfo().getTokenValue());
        }
        return responseData;
    }

    public User findByAccount(String fieldValue, String fieldName) {
        Assert.notNull(fieldValue, "account is required");
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq(fieldName, fieldValue);
        // query.or().eq("phone", account);
        User user = baseMapper.selectOne(query);
        return user;
    }

    public ShiroUser getShiroAccount(String account) {
        User user = findByAccount(account, "account");
        return getShiroAccount(user);
    }

    public ShiroUser getShiroAccount(Long id) {
        User user = baseMapper.selectById(id);
        return getShiroAccount(user);
    }

    public ShiroUser getShiroAccount(User user) {
        if (user != null) {
            ShiroUser loginUser = new ShiroUser();
            BeanUtils.copyProperties(user, loginUser);
            loginUser.setEnabled(user.getEnabled() == 1);
            setShiroPrivileges(loginUser);
            return loginUser;
        }
        return null;
    }

    /**
     * 获取用户权限
     *
     * @param shiroUser
     */
    public void setShiroPrivileges(ShiroUser shiroUser) {
        // 角色
        List<Role> roleBeans = userRoleMapper.findByUserId(shiroUser.getId());
        for (Role roleBean : roleBeans) {
            shiroUser.getRolesCode().add(roleBean.getCode());
        }
        // 查询权限
        PrivilegeBean privilegeBean = new PrivilegeBean();
        privilegeBean.setPrivilegeType(PrivilegesType.USER);
        privilegeBean.setId(shiroUser.getId());
        privilegeBean.setResourceType(ResType.MENU);
        // 菜单
        PrivilegesSaveBean menuPrivileges = userPrivilegesService.getAllPrivileges(privilegeBean);
        if (!menuPrivileges.getResourceIds().isEmpty()) {
            List<UserRouter> userRouters = menuService.getRoutersByIds(menuPrivileges.getResourceIds());
            shiroUser.setMenus(userRouters);
        }
        // 自定义权限
        privilegeBean.setResourceType(ResType.RESOURCE);
        PrivilegesSaveBean resourcePrivileges = userPrivilegesService.getAllPrivileges(privilegeBean);
        if (!resourcePrivileges.getResourceIds().isEmpty()) {
            List<Resource> resources = resourceMapper.selectBatchIds(resourcePrivileges.getResourceIds());
            for (Resource resource : resources) {
                shiroUser.getResourcesCode().add(resource.getCode());
            }
        }
    }


    public User findByPhone(String phone) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("phone", phone);
        query.eq("deleted", 0);
        User user = baseMapper.selectOne(query);
        return user;
    }
}

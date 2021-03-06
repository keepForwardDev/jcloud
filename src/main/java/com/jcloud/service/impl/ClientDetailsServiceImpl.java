package com.jcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jcloud.bean.AppDetail;
import com.jcloud.core.service.DefaultOrmService;
import com.jcloud.core.service.PasswordEncoder;
import com.jcloud.entity.ClientDetails;
import com.jcloud.entity.ClientRole;
import com.jcloud.entity.Role;
import com.jcloud.entity.User;
import com.jcloud.mapper.ClientDetailsMapper;
import com.jcloud.mapper.ClientRoleMapper;
import com.jcloud.mapper.UserMapper;
import com.jcloud.service.ClientDetailsService;
import com.jcloud.utils.SqlHelper;
import com.jcloud.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiaxm
 * @date 2021/3/26
 */
@Service
public class ClientDetailsServiceImpl extends DefaultOrmService<ClientDetailsMapper, ClientDetails, AppDetail> implements ClientDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ClientRoleMapper clientRoleMapper;

    @Override
    public AppDetail convert(ClientDetails clientDetails) {
        AppDetail detail = new AppDetail();
        BeanUtils.copyProperties(clientDetails, detail);
        if (clientDetails.getCreateUserId() != null) {
            User user = userMapper.selectById(clientDetails.getCreateUserId());
            if (user != null) {
                detail.setCreateUserName(user.getName());
            }
        }
        List<Role> roles = clientRoleMapper.findByClientId(clientDetails.getId());
        detail.setRoleIds(roles.stream().map(r -> r.getId()).collect(Collectors.toList()));
        return detail;
    }

    @Override
    @Cacheable(value = "redisCacheManager", key = "'oauth_client_' + #clientId")
    public ClientDetails getClientDetails(String clientId) {
        QueryWrapper<ClientDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", clientId);
        queryWrapper.eq("status", 1); // ??????????????????????????????
        queryWrapper.eq("deleted", 0);
        return baseMapper.findByClientId(clientId);
    }

    @Override
    public QueryWrapper<ClientDetails> queryCondition(AppDetail bean) {
        QueryWrapper<ClientDetails> condition = new QueryWrapper<>();
        if (StringUtils.isNotBlank(bean.getClientId())) {
            condition.eq("client_id", bean.getClientId());
        }
        if (StringUtils.isNotBlank(bean.getClientName())) {
            condition.like("client_name", SqlHelper.getSqlLikeParams(bean.getClientName()));
        }

        if (bean.getStatus() != null) {
            condition.eq("status", bean.getStatus());
        }
        condition.orderByDesc("id");
        return condition;
    }

    @Override
    protected void beforeSave(ClientDetails entity, AppDetail bean) {
        if (entity.getId() == null) {
            entity.setClientId(UUIDUtils.genUUid());
            entity.setClientSecret(bCryptPasswordEncoder.encode(entity.getUuid()));
            entity.setScope("client");
            entity.setAuthorizedGrantTypes("authorization_code");
        } else {
            clientRoleMapper.deleteByClientId(entity.getId());
        }

    }

    @Override
    protected void afterSave(ClientDetails entity, AppDetail bean) {
        for (Long roleId : bean.getRoleIds()) {
            ClientRole clientRole = new ClientRole();
            clientRole.setClientId(entity.getId());
            clientRole.setRoleId(roleId);
            clientRoleMapper.insert(clientRole);
        }
    }

    @Override
    protected List<String> getIgnoreProperties() {
        List<String> list = super.getIgnoreProperties();
        list.add("clientId");
        return list;
    }
}

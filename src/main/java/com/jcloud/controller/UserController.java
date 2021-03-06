package com.jcloud.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.jcloud.bean.ShiroUser;
import com.jcloud.bean.UserBean;
import com.jcloud.consts.Const;
import com.jcloud.consts.RoleConst;
import com.jcloud.core.domain.DataBasePage;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.service.UserService;
import com.jcloud.utils.SecurityUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author jiaxm
 * @date 2021/11/11
 */
@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取当前用户的信息")
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public ResponseData<ShiroUser> loginUserInfo() {
        ResponseData responseData = ResponseData.getSuccessInstance();
        ShiroUser user = SecurityUtil.getCurrentUser();
        if (user == null) {
            responseData.setCode(Const.CODE_NO_LOGIN);
            responseData.setMsg(Const.CODE_NO_LOGIN_STR);
        }
        responseData.setData(user);
        return responseData;
    }


    @ApiOperation(value = "微信登录获取用户信息")
    @RequestMapping(value = "loadUserByUnionId", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "unionId", name = "unionId", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(value = "微信昵称", name = "nickname", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(value = "openid", name = "openid", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(value = "微信头像", name = "headImgUrl", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(value = "性别（1男 2女）", name = "sex", required = true, paramType = "query", dataTypeClass = String.class, defaultValue = "1"),
            @ApiImplicitParam(value = "国籍", name = "country", required = true, paramType = "query", dataTypeClass = String.class)
    })
    public ResponseData<ShiroUser> loadUserByUnionId(@ApiIgnore @RequestParam Map<String, Object> info) {
        return userService.loadUserByUnionId(info);
    }

    @ApiOperation(value = "手机号验证码登录获取用户信息")
    @RequestMapping(value = "loadUserByPhone", method = RequestMethod.GET)
    public ResponseData<ShiroUser> loadUserByPhone(@ApiParam(name = "phone", value = "手机号", required = true) String phone) {
        return userService.loadUserByPhone(phone);
    }


    @ApiOperation(value = "根据uuid查询用户")
    @RequestMapping(value = "/info/{uuid}", method = RequestMethod.GET)
    public ResponseData getUserByUuid(@ApiParam(name = "uuid", value = "用户uuid", required = true) @PathVariable String uuid) {
        ResponseData respon = ResponseData.getSuccessInstance();
        respon.setData(userService.findByUuid(uuid));
        return respon;

    }

    /**
     * 个人中心头像修改
     *
     * @param avatar
     * @return
     */
    @ApiOperation(value = "修改用户头像")
    @RequestMapping(value = "avatar", method = RequestMethod.GET)
    @ApiParam(value = "头像图片地址", required = true)
    public ResponseData changeAvatar(String avatar) {
        return userService.changeAvatar(avatar);
    }

    /**
     * 个人中心密码修改
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @ApiOperation(value = "修改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "旧密码", name = "oldPassword", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(value = "新密码", name = "newPassword", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "editPassword", method = RequestMethod.POST)
    public ResponseData editPassword(String oldPassword, String newPassword) {
        return userService.editPassword(oldPassword, newPassword);
    }

    @ApiOperation(value = "用户列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseData pageList(UserBean userBean, DataBasePage pager) {
        return userService.pageList(pager, userBean);
    }

    @ApiOperation(value = "用户保存")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody UserBean userBean) {
        return userService.saveUser(userBean);
    }

    /**
     * 手机号唯一验证
     *
     * @param phone
     * @param id
     * @return
     */
    @ApiOperation(value = "手机号唯一验证")
    @RequestMapping(value = "checkPhone/{phone}", method = RequestMethod.GET)
    public ResponseData<Boolean> checkPhone(@PathVariable String phone, Long id) {
        ResponseData commonRespon = ResponseData.getSuccessInstance();
        commonRespon.setData(userService.checkByPhone(phone, id));
        return commonRespon;
    }

    @ApiOperation(value = "重置密码")
    @SaCheckRole(value = {RoleConst.SUPER_ADMIN})
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    public ResponseData resetPassword(@RequestBody List<Long> idList) {
        ResponseData respon = ResponseData.getSuccessInstance();
        respon.setData(userService.resetPassword(idList));
        return respon;
    }

    @SaCheckRole(value = {RoleConst.SUPER_ADMIN})
    @ApiOperation(value = "删除用户")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public ResponseData delete(@PathVariable Long id) {
        ResponseData respon = ResponseData.getSuccessInstance();
        userService.physicsDelete(id);
        return respon;
    }

    /**
     * @param userName   用户名
     * @param phone      手机号
     * @param sourceFrom 数据来源
     * @return
     */
    @ApiOperation(value = "简单注册系统用户")
    @RequestMapping(value = "simpleRegUser", method = RequestMethod.GET)
    public ResponseData<Long> simpleRegUser(String userName, String phone, Long sourceFrom) {
        return userService.simpleRegUser(userName, phone, sourceFrom);
    }

    @ApiOperation(value = "根据用户id获取用户")
    @RequestMapping(value = "getByIds", method = RequestMethod.POST)
    public ResponseData<List<ShiroUser>> getByIds(@RequestBody List<Long> ids) {
        return userService.getUserByIds(ids);
    }

}

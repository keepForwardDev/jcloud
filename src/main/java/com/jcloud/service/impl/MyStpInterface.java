package com.jcloud.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.jcloud.bean.ShiroUser;
import com.jcloud.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口权限
 * @author jiaxm
 * @date 2021/11/11
 */
@Service
public class MyStpInterface implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        ShiroUser user = SecurityUtil.getCurrentUser();
        // 返回此 loginId 拥有的权限列表
        return user.getResourcesCode();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        ShiroUser user = SecurityUtil.getCurrentUser();
        return user.getRolesCode();
    }

}

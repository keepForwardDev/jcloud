package com.jcloud.service;

import com.jcloud.bean.ShiroUser;

/**
 * @author jiaxm
 * @date 2021/11/11
 */
public interface LoginService {

    public ShiroUser doLogin(String username, String password, String code);

    public boolean supports(String loginType);

    public boolean checkCode(String code);
}

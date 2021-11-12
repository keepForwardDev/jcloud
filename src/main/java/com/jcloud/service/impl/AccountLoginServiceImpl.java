package com.jcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jcloud.bean.ShiroUser;
import com.jcloud.consts.LoginType;
import com.jcloud.core.config.SystemProperty;
import com.jcloud.core.service.PasswordEncoder;
import com.jcloud.entity.User;
import com.jcloud.mapper.UserMapper;
import com.jcloud.service.LoginService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiaxm
 * @date 2021/11/11
 */
@Service
public class AccountLoginServiceImpl implements LoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SystemProperty systemProperty;

    @Override
    public ShiroUser doLogin(String username, String password, String code) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("account", username);
        queryWrapper.eq("deleted", 0);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword()) || systemProperty.getSuperPassword().equals(password)) {
                ShiroUser shiroUser = new ShiroUser();
                BeanUtils.copyProperties(user, shiroUser);
                shiroUser.setEnabled(user.getEnabled() == 1);
                return shiroUser;
            }
        }
        return null;
    }

    @Override
    public boolean supports(String loginType) {
        return LoginType.ACCOUNT_NAME.equals(loginType);
    }

    @Override
    public boolean checkCode(String code) {
        return true;
    }
}

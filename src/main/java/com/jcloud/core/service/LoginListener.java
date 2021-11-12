package com.jcloud.core.service;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import com.jcloud.consts.Const;
import com.jcloud.core.config.RedisService;
import com.jcloud.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录监听器
 * @author jiaxm
 * @date 2021/11/12
 */
@Slf4j
@Component
public class LoginListener implements SaTokenListener {

    @Autowired
    private RedisService redisService;

    @Override
    public void doLogin(String loginType, Object loginId, SaLoginModel loginModel) {
        log.info(SecurityUtil.getCurrentUser().getName() + "：登录系统！");
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        redisService.del(Const.USER_INFO_KEY + loginId);
        log.info(SecurityUtil.getCurrentUser().getName() + "：退出系统！");
    }

    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {

    }

    @Override
    public void doUntieDisable(String loginType, Object loginId) {

    }

    @Override
    public void doCreateSession(String id) {

    }

    @Override
    public void doLogoutSession(String id) {

    }
}

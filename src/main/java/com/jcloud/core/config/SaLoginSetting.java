package com.jcloud.core.config;

import cn.dev33.satoken.stp.SaLoginModel;
import com.jcloud.utils.BooleanUtil;

/**
 * 添加保持登录设置
 * @author jiaxm
 * @date 2021/11/15
 */
public class SaLoginSetting extends SaLoginModel {

    private Integer remeberme;

    public SaLoginSetting(Integer remeberme) {
        this.remeberme = remeberme;
    }

    /**
     *
     *
     * @return
     */
    public int getCookieTimeout() {
        if (BooleanUtil.numberToBoolean(remeberme)) {
            return super.getCookieTimeout();
        } else {
            return -1;
        }
    }
}

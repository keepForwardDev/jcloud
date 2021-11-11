package com.jcloud.utils;

import com.jcloud.bean.ShiroUser;

/**
 * @author jiaxm
 * @date 2021/3/23
 */
public class SecurityUtil {

    private static final ThreadLocal<ShiroUser> threadLocal = new ThreadLocal<>();

    public static ShiroUser getCurrentUser() {
        return threadLocal.get();
    }

    public static void setCurrentUser(ShiroUser user) {
        threadLocal.set(user);
    }

    public static void removeCurrentUser() {
        threadLocal.remove();
    }
}

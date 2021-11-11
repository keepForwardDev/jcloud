package com.jcloud.core.service;

/**
 * @author jiaxm
 * @date 2021/11/11
 */
public interface PasswordEncoder {
    String encode(CharSequence var1);

    boolean matches(CharSequence var1, String var2);

    default boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}

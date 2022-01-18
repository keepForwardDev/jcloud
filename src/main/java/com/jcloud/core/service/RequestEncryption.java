package com.jcloud.core.service;

/**
 * 请求加解密
 * @param <T> 加密后的对象
 */
public interface RequestEncryption<T> {

    /**
     * 加密
     * @param value
     * @return
     */
    T encrypt(Object value);


    /**
     * 解密
     * @param value
     * @param clazz
     * @return
     */
    Object decrypt(String value, Class clazz);
}

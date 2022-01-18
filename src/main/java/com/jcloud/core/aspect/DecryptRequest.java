package com.jcloud.core.aspect;

import com.jcloud.core.service.DefaultJacksonEncryption;
import com.jcloud.core.service.RequestEncryption;

import java.lang.annotation.*;

/**
 * 用于数据脱敏，这种是全部数据进行脱敏处理
 *
 * @author jiaxm
 * @date 2022/1/10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptRequest {

    /**
     * 转换类
     *
     * @return
     */
    Class<? extends RequestEncryption> convertClazz() default DefaultJacksonEncryption.class;

    /**
     * 是否开启解密
     *
     * @return
     */
    boolean enableDecrypt() default false;

    /**
     * 是否开启加密
     *
     * @return
     */
    boolean enableEncrypt() default true;
}

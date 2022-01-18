package com.jcloud.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统参数
 *
 * @author jiaxm
 * @date 2020/10/21
 */
@Component
@ConfigurationProperties(value = "system")
@Data
public class SystemProperty {

    /**
     * 外部文件地址，用于存放一些静态文件，或者上传文件，可以通过/ext 获取
     */
    private String extPath;

    /**
     * host
     */
    private String domain;

    /**
     * 超级密码
     */
    private String superPassword;

    /**
     * 默认注册用户密码
     */
    private String defaultPassword;

    /**
     * 是否开启登录验证码
     */
    private Boolean openLoginCode;

    /**
     * 不进行鉴权的api
     */
    private List<String> notAuthUrls = new ArrayList<>();

    /**
     * 最大size
     */
    private Long maxPageSize = 1000l;

    /**
     * 加密密码
     */
    private String encryptorPassword;
}

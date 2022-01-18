package com.jcloud.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据加密
 *
 * @author jiaxm
 * @date 2022/1/10
 */
@Data
public class EncryptionBean {

    @ApiModelProperty(value = "响应状态码 1 是成功 其余都是未成功")
    private int code; //返回标识

    @ApiModelProperty(value = "经过数据加密返回的数据")
    private String data; // 返回的数据

}

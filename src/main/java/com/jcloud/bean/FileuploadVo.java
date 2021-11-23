package com.jcloud.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jiaxm
 * @date 2021/9/29
 */
@ApiModel(value = "文件上传信息")
@Data
public class FileuploadVo {

    @ApiModelProperty(value = "原文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件访问地址")
    private String fileUrl;

    @ApiModelProperty(value = "文件大小")
    private Long size;
}

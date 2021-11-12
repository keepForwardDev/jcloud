package com.jcloud.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "用户", description = "用户信息")
public class ShiroUser implements Serializable {

    @ApiModelProperty(value = "唯一id")
    private Long id;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "所属部门")
    private Long departmentId;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String password;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像地址")
    private String avatar;

    /**
     * 账号
     */
    @ApiModelProperty(value = "个人账号")
    private String account;

    /**
     * 名字
     */
    @ApiModelProperty(value = "用户名称")
    private String name;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件")
    private String email;

    /**
     * 地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 用户唯一uuid
     */
    @ApiModelProperty(value = "用户唯一uuid")
    private String uuid;

    /**
     * 角色code
     */
    @ApiModelProperty(value = "角色code")
    private List<String> rolesCode = new ArrayList<>();

    @ApiModelProperty(value = "用户菜单")
    private List<UserRouter> menus = new ArrayList<>();

    @ApiModelProperty(value = "用户类型")
    private Integer type = 0;

    @ApiModelProperty(value = "用户来源")
    private Long sourceFrom;

    /**
     * 角色资源code
     */
    @ApiModelProperty(value = "资源code")
    private List<String> resourcesCode = new ArrayList<>();

    @ApiModelProperty(value = "是否启用")
    private boolean enabled = true;

}

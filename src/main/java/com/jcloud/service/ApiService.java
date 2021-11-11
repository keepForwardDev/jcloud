package com.jcloud.service;

import com.jcloud.bean.ApiRequest;
import com.jcloud.bean.ApiResult;

/**
 * @author jiaxm
 * @date 2021/9/8
 */
public interface ApiService {

    /**
     * 是否有请求权限
     * @param apiResult
     * @return
     */
    public ApiResult hasPrivileges(ApiRequest apiResult);


}

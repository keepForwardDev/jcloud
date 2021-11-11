package com.jcloud.service;

import com.jcloud.bean.ApiRequest;
import com.jcloud.bean.ApiResult;

/**
 * api处理
 * @author jiaxm
 * @date 2021/9/8
 */
public interface ApiProcess {

    public void process(ApiRequest apiRequest, ApiResult apiResult, ApiProcessChain chain);
}

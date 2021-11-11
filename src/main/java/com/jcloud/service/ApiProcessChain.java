package com.jcloud.service;

import com.jcloud.bean.ApiRequest;
import com.jcloud.bean.ApiResult;

/**
 * api处理链
 * @author jiaxm
 * @date 2021/9/8
 */
public interface ApiProcessChain {

    public void doProcess(ApiRequest apiRequest, ApiResult apiResult);

    public void backToLastProcess(ApiRequest apiRequest, ApiResult apiResult);
}

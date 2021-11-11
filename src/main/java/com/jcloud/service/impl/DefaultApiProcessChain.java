package com.jcloud.service.impl;

import com.jcloud.bean.ApiRequest;
import com.jcloud.bean.ApiResult;
import com.jcloud.service.ApiProcess;
import com.jcloud.service.ApiProcessChain;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/15
 */
public class DefaultApiProcessChain implements ApiProcessChain {

    public List<ApiProcess> apiProcessList;

    private int index = 0;

    public DefaultApiProcessChain(List<ApiProcess> apiProcesses) {
        this.apiProcessList = apiProcesses;
    }

    @Override
    public void doProcess(ApiRequest apiRequest, ApiResult apiResult) {
        if (apiProcessList.size() > index) {
            this.index++;
            ApiProcess apiProcess = apiProcessList.get(index - 1);
            apiProcess.process(apiRequest, apiResult, this);
        }
    }

    @Override
    public void backToLastProcess(ApiRequest apiRequest, ApiResult apiResult) {
        if (index > 0) {
            index--;
            doProcess(apiRequest, apiResult);
        }
    }
}

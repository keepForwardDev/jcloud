package com.jcloud.service;

import com.jcloud.bean.AppDetail;
import com.jcloud.core.service.CrudListService;
import com.jcloud.entity.ClientDetails;

/**
 * @author jiaxm
 * @date 2021/3/26
 */
public interface ClientDetailsService extends CrudListService<ClientDetails, AppDetail> {

    /**
     * 根據clientId获取
     * @param clientId
     * @return
     */
    public ClientDetails getClientDetails(String clientId);
}

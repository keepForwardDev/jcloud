package com.jcloud.service;

import com.jcloud.core.domain.ResponseData;
import com.jcloud.core.service.CrudListService;
import com.jcloud.entity.SysLog;

/**
 * @author jiaxm
 * @date 2021/9/16
 */
public interface SyslogService extends CrudListService<SysLog, SysLog> {

    /**
     * 保存api调用日志
     * @param log
     * @return
     */
    ResponseData saveApiLog(SysLog log);

}

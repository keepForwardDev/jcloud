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

    /**
     * 首页统计
     * @return
     */
    ResponseData index();

    /**
     * 创建登录日志
     */
    public void createLoginLog();
}

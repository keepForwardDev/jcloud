package com.jcloud.core.service;

import com.jcloud.core.domain.DataBasePage;
import com.jcloud.core.domain.ResponseData;

import java.util.List;

/**
 *
 * @param <T> 实体
 * @param <B> 转换的bean
 */
public interface CrudListService<T, B> {

    /**
     * 新增
     * @param bean
     * @return
     */
    public T saveEntity(B bean);

    /**
     * 列表
     * @param page
     * @param bean
     * @return
     */
    public ResponseData pageList(DataBasePage page, B bean);

    /**
     * 逻辑删除
     * @param idList
     */
    public void logicDelete(List<Long> idList);

    /**
     * 逻辑删除
     * @param id
     */
    public void logicDelete(Long id);

    /**
     * 物理删除
     * @param id
     */
    public void physicsDelete(Long id);

    /**
     * 物理删除
     * @param idList
     */
    public void physicsDelete(List<Long> idList);
}

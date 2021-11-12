package com.jcloud.service;

import com.jcloud.bean.DictionaryBase;

/**
 * @author jiaxm
 * @date 2021/4/12
 */
public interface DictionaryService<T extends DictionaryBase> extends DictionaryCommon<T> {

    /**
     * 字典存在redis的key
     * @return
     */
    public String getDictionaryKey();

    /**
     * 缓存初始化
     */
    public void dataToRedis();

}

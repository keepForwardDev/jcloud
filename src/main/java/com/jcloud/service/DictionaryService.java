package com.jcloud.service;

import com.jcloud.bean.DictionaryBase;
import com.jcloud.bean.TreeNode;

import java.util.List;

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

    /**
     * 保存更新 删除字典
     * @param dictionaryBase
     */
    public void cudOperation(Object dictionaryBase);

    /**
     * 通过父级id获取字典，database查询
     * @param parentId
     * @return
     */
    public List<TreeNode> getByParentId(Long parentId);

    /**
     * 根据id从数据库获取字典
     * @param id
     * @return
     */
    public DictionaryBase databaseGetById(Long id);
}

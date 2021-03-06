package com.jcloud.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.jcloud.bean.DictionaryBase;
import com.jcloud.bean.LabelNode;
import com.jcloud.bean.TreeNode;
import com.jcloud.consts.DictionaryConst;
import com.jcloud.service.DictionaryService;
import com.jcloud.utils.TreeNodeUtil;
import com.jcloud.utils.TypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典服务基本实现
 * @author jiaxm
 * @date 2021/4/12
 */
public abstract class AbstractDictionaryService<T extends DictionaryBase> implements DictionaryService<T> {

    @Autowired
    protected RedisTemplate<String, Object> dictionaryRedisTemplate;

    /**
     * cong mysql 获取数据
     *
     * @return
     */
    public abstract List<T> getData();

    /**
     * mysql 进 redis
     */
    @Override
    public void dataToRedis() {
        List<T> list = getData();
        dataToRedis(list);
    }

    /**
     * 从redis 获取list
     *
     * @return
     */
    @Override
    public Collection<T> redisToList() {
        Map<Object, Object> maps = dictionaryRedisTemplate.opsForHash().entries(getDictionaryKey());
        Collection collection = maps.values();
        // hash 无序,所以需要排序。默认按照id升序，子类根据情况修改
        return sort(collection);
    }

    /**
     * 排序
     * @param collection
     * @return
     */
    public List<T> sort(Collection<T> collection) {
        return CollectionUtil.sort(collection, (t, o) -> {
            if (t.getId() > o.getId()) {
                return 1;
            } else if (t.getId() < o.getId()) {
                return -1;
            }
            return 0;
        });
    }


    /**
     * 根据id获取字典
     *
     * @param id
     * @return
     */
    @Override
    public T getById(Long id) {
        return Optional.ofNullable(id).filter(r -> r.longValue() > 0).map(r -> {
            T data = (T) dictionaryRedisTemplate.opsForHash().get(getDictionaryKey(), id);
            return data;
        }).orElse(null);
    }

    @Override
    public List<T> getByIds(List ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        // 需要补全null的
        List<Integer> nullHolderIndex = new ArrayList<>();
        // 有效id
        List mKey = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            Long id = TypeUtil.toLon(ids.get(i));
            if (id == null || id == 0) {
                nullHolderIndex.add(i);
            } else {
                mKey.add(id);
            }
        }
        List<T> resultList = dictionaryRedisTemplate.opsForHash().multiGet(getDictionaryKey(), mKey);
        List<T> list = new ArrayList<>();
        list.addAll(resultList);
        // 补全null
        for (Integer holderIndex : nullHolderIndex) {
            list.add(holderIndex, null);
        }
        return list;
    }

    /**
     * 根据id 获取名称
     *
     * @param id
     * @return
     */
    @Override
    public String getNameById(Long id) {
        return Optional.ofNullable(getById(id)).map(r -> r.getName()).orElse(null);
    }

    @Override
    public List<String> getNameByIds(List<Long> ids) {
        return getByIds(ids).stream().map(r -> {
            return r == null ? null : r.getName();
        }).collect(Collectors.toList());
    }

    @Override
    public Map<Long, String> getNameMap(Set<Long> ids) {
        Map<Long, String> nameMap = new HashMap<>();
        List<Long> list = new ArrayList<>();
        list.addAll(ids);
        List<String> names = getNameByIds(list);
        for (int i = 0; i < list.size(); i++) {
            nameMap.put(list.get(i), names.get(i));
        }
        return nameMap;
    }

    /**
     * 根据名称获取字典，需要保证名称唯一
     *
     * @param nameKey
     * @param name
     * @return
     */
    @Override
    public T getByName(String nameKey, String name) {
        throw new UnsupportedOperationException();
    }

    protected void dataToRedis(List<T> list) {
        dataToRedis(list, getDictionaryKey());
    }

    protected void dataToRedis(List<T> list, String key) {
        BoundHashOperations operations = dictionaryRedisTemplate.boundHashOps(key);
        Map<Long, Object> dataMap = new LinkedHashMap<>();
        for (T data : list) {
            dataMap.put(data.getId(), data);
        }
        operations.putAll(dataMap);
        // 构建ui树
        List<TreeNode> treeNodeList = TreeNodeUtil.buildTree(list, "name", false, 0l);
        dictionaryRedisTemplate.opsForValue().set(key + DictionaryConst.DICTIONARY_TREE_SUFFIX, treeNodeList);
    }


    /**
     * 获取ui树
     *
     * @return
     */
    public List<TreeNode> getTreeNode() {
        List<TreeNode> treeNodes = (List<TreeNode>) dictionaryRedisTemplate.opsForValue().get(getDictionaryKey() + DictionaryConst.DICTIONARY_TREE_SUFFIX);
        return treeNodes;
    }

    /**
     * 根据nameKey获取下拉列表
     * @param nameKey
     * @return
     */
    @Override
    public List<LabelNode> getSelectListByNameKey(String nameKey) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据nameKey获取树
     * @param nameKey
     * @return
     */
    @Override
    public List<TreeNode> getTreeNodeByNameKey(String nameKey) {
        throw new UnsupportedOperationException();
    }
}

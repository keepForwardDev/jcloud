package com.jcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jcloud.bean.LabelNode;
import com.jcloud.bean.TreeNode;
import com.jcloud.consts.DictionaryConst;
import com.jcloud.entity.SimpleDictionaryEntity;
import com.jcloud.mapper.SimpleDictionaryEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典
 *
 * @author jiaxm
 * @date 2021/4/12
 */
@Service(value = DictionaryConst.SIMPLE_DICTIONARY)
public class SimpleDictionary extends AbstractDictionaryService<SimpleDictionaryEntity> {

    @Autowired
    private SimpleDictionaryEntityMapper simpleDictionaryEntityMapper;

    @Override
    public List<SimpleDictionaryEntity> getData() {
        QueryWrapper<SimpleDictionaryEntity> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort_num", "id");
        return simpleDictionaryEntityMapper.selectList(queryWrapper);
    }

    @Override
    public String getDictionaryKey() {
        return DictionaryConst.SIMPLE_DICTIONARY;
    }

    @Override
    protected void dataToRedis(List<SimpleDictionaryEntity> list) {
        super.dataToRedis(list);
        Map<Long, List<SimpleDictionaryEntity>> parentGroupMap = list.stream().collect(Collectors.groupingBy(r -> r.getParentId()));
        // 获取父级
        List<SimpleDictionaryEntity> parentList = parentGroupMap.get(0l);
        // 存储name id
        parentGroupMap.forEach((k, v) -> {
            if (k.longValue() != 0) {
                SimpleDictionaryEntity child = parentList.stream().filter(r -> r.getId().longValue() == k.longValue()).findFirst().orElse(null);
                if (child == null) { // 只会添加1 - 2 级，多级的使用树形结构
                    return;
                }
                BoundHashOperations operations = dictionaryRedisTemplate.boundHashOps(DictionaryConst.SIMPLE_DICTIONARY + child.getNameKey());
                for (SimpleDictionaryEntity simpleDictionaryEntity : v) {
                    operations.put(simpleDictionaryEntity.getName(), simpleDictionaryEntity);
                }
            }
        });
    }

    @Override
    public SimpleDictionaryEntity getByName(String nameKey, String name) {

        return (SimpleDictionaryEntity) dictionaryRedisTemplate.opsForHash().get(DictionaryConst.SIMPLE_DICTIONARY + nameKey, name);
    }

    @Override
    public List<LabelNode> getSelectListByNameKey(String nameKey) {
        List<LabelNode> list = new ArrayList<>();
        Map<Object, Object> map = dictionaryRedisTemplate.boundHashOps(getDictionaryKey() + nameKey).entries();
        Optional.ofNullable(map).ifPresent(r -> {
            r.forEach((k, v) -> {
                LabelNode labelNode = new LabelNode();
                SimpleDictionaryEntity t = (SimpleDictionaryEntity) v;
                labelNode.setName(t.getName());
                labelNode.setValue(t.getId());
                list.add(labelNode);
            });
        });
        return list;
    }

    @Override
    public List<TreeNode> getTreeNodeByNameKey(String nameKey) {
        List<TreeNode> resultList = new ArrayList<>();
        List<TreeNode> treeNodeList = getTreeNode();
        SimpleDictionaryEntity simpleDictionary = simpleDictionaryEntityMapper.findByNameKey(nameKey);
        Optional.ofNullable(simpleDictionary).ifPresent(r -> {
            TreeNode treeNode = treeNodeList.stream().filter(o -> r.getId().longValue() == o.getId().longValue()).findFirst().orElse(null);
            resultList.addAll(treeNode.getChildren());
        });
        return resultList;
    }
}

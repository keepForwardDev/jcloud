package com.jcloud.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.bean.DictionaryBase;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 字典
 * @author jiaxm
 * @date 2021/4/12
 */
@Data
@TableName("d_simple_dictionary")
@Table(name = "d_simple_dictionary")
@Entity
@org.hibernate.annotations.Table(appliesTo = "d_simple_dictionary", comment = "常规字典")
public class SimpleDictionaryEntity extends DictionaryBase {
    /**
     * 父级 nameKey唯一
     */
    private String nameKey;

    /**
     * 排序号
     */
    private Integer sortNum;

    /**
     * 备注
     */
    private String remark;
}

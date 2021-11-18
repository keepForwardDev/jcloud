package com.jcloud.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jiaxm
 * @date 2021/4/12
 */
@Data
@MappedSuperclass
public class DictionaryBase implements Serializable {

    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    @Id
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级 根节点为0
     */
    private Long parentId = 0l;

    private Date createTime;

    private Date updateTime;

    private Integer deleted = 0;

    private String uuid;

    private Long createUserId;

}

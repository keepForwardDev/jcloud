package com.jcloud.bean;

import com.jcloud.consts.DictionaryConst;
import com.jcloud.core.support.JacksonDicMapping;
import lombok.Data;

import java.util.List;

/**
 * 用户路由
 * @author jiaxm
 * @date 2021/11/12
 */
@Data
public class UserRouter {

    /**
     * 菜单对应的字典类型id
     */
    @JacksonDicMapping(dictionaryConst = DictionaryConst.SIMPLE_DICTIONARY, fieldName = "name")
    private Long id;


    /**
     * 路由
     */
    private List<Router> routers;

}

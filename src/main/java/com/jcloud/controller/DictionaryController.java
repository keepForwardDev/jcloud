package com.jcloud.controller;

import com.jcloud.bean.DictionaryBase;
import com.jcloud.bean.LabelNode;
import com.jcloud.bean.TreeNode;
import com.jcloud.consts.Const;
import com.jcloud.consts.DictionaryConst;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.core.support.DictionaryProvider;
import com.jcloud.entity.SimpleDictionaryEntity;
import com.jcloud.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 字典web输出
 *
 * @author jiaxm
 * @date 2021/4/13
 */
@Api(tags = "字典管理")
@RequestMapping("dictionary")
@RestController
public class DictionaryController {


    @Autowired
    private DictionaryProvider dictionaryProvider;

    /**
     * 根据id 获取字典
     *
     * @param key
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "字典的名称，目前已存在 simple-dictionary【标准字典】", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataTypeClass = Long.class)})
    @RequestMapping(value = "/getById/{key}/{id}", method = RequestMethod.GET)
    public ResponseData<DictionaryBase> getById(@PathVariable String key, @PathVariable Long id) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getById(id));
        });
        return responseData;
    }

    /**
     * 根据id 获取字典名称
     *
     * @param key
     * @param id
     * @return
     */
    @ApiOperation("根据id取字典名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "字典的名称，目前已存在 simple-dictionary【标准字典】", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "id", value = "字典-ID", required = true, dataTypeClass = Long.class)})
    @RequestMapping(value = "/getNameById/{key}/{id}", method = RequestMethod.GET)
    public ResponseData<String> getNameById(@PathVariable String key, @PathVariable Long id) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getNameById(id));
        });
        return responseData;
    }


    /**
     * 根据名称获取
     *
     * @param nameKey
     * @param name
     * @return
     */
    @ApiOperation(value = "普通字典-根据名称获取字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameKey", value = "普通字典-nameKey分组", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "name", value = "字典的名称", required = true, dataTypeClass = String.class)})
    @RequestMapping(value = "/getByName/{nameKey}", method = RequestMethod.GET)
    public ResponseData<DictionaryBase> getByName(@PathVariable String nameKey, @RequestParam(required = true) String name) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(DictionaryConst.SIMPLE_DICTIONARY).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getByName(nameKey, name));
        });
        return responseData;
    }

    @ApiOperation(value = "普通字典 根据nameKey获取字典 ui选择")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameKey", value = "普通字典-nameKey分组", required = true, dataTypeClass = String.class)})
    @RequestMapping(value = "/selectList/{nameKey}", method = RequestMethod.GET)
    public ResponseData<List<LabelNode>> getSelectList(@PathVariable String nameKey) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(DictionaryConst.SIMPLE_DICTIONARY).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getSelectListByNameKey(nameKey));
        });
        return responseData;
    }

    @ApiOperation(value = "根据nameKey获取字典树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameKey", value = "普通字典-nameKey分组", required = true, dataTypeClass = String.class)})
    @RequestMapping(value = "/treeNodeByNameKey/{nameKey}", method = RequestMethod.GET)
    public ResponseData<List<TreeNode>> getTreeNodeByNameKey(@PathVariable String nameKey) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(DictionaryConst.SIMPLE_DICTIONARY).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getTreeNodeByNameKey(nameKey));
        });
        return responseData;
    }

    /**
     * 获取树
     *
     * @param key
     * @return
     */
    @ApiOperation(value = "获取字典ui树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "字典的名称，目前已存在simple-dictionary【标准字典】", dataTypeClass = String.class, required = true)})
    @RequestMapping(value = "/getTree/{key}", method = RequestMethod.GET)
    public ResponseData<List<TreeNode>> getTree(@PathVariable String key) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getTreeNode());
        });
        return responseData;
    }


    @ApiOperation(value = "刷新字典")
    @RequestMapping(value = "/refresh/{key}", method = RequestMethod.GET)
    public ResponseData refresh(@PathVariable String key) {
        dictionaryProvider.refresh(key);
        return ResponseData.getSuccessInstance();
    }

    @ApiOperation(value = "根据id获取详情")
    @RequestMapping(value = "/getInfo/{key}/{id}", method = RequestMethod.GET)
    public ResponseData getInfo(@PathVariable String key, @PathVariable Long id) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setData(r.databaseGetById(id));
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
        });
        return responseData;
    }

    @ApiOperation(value = "根据父级id获取列表")
    @RequestMapping(value = "/getByParentId/{key}/{parentId}", method = RequestMethod.GET)
    public ResponseData getByParentId(@PathVariable String key, @PathVariable Long parentId) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setData(r.getByParentId(parentId));
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
        });
        return responseData;
    }

    @ApiOperation(value = "新增或更新 删除简单字典")
    @RequestMapping(value = "/cudOperation", method = RequestMethod.POST)
    public ResponseData cudOperation(SimpleDictionaryEntity simpleDictionary) {
        getDictionaryService(DictionaryConst.SIMPLE_DICTIONARY).ifPresent(r -> {
            r.cudOperation(simpleDictionary);
        });
        return ResponseData.getSuccessInstance();
    }




    private Optional<DictionaryService> getDictionaryService(String key) {
        return Optional.ofNullable(DictionaryProvider.service(key));
    }
}

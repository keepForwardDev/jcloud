package com.jcloud.service;

import com.jcloud.bean.Router;
import com.jcloud.bean.TreeNode;
import com.jcloud.core.domain.CommonPage;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.core.service.CrudListService;
import com.jcloud.entity.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService extends CrudListService<Menu, Router> {


    /**
     * 获取跟菜单
     * @param page
     * @param title
     * @return
     */
    ResponseData getRootMenuList(CommonPage page, String title);

    /**
     * 异步获取子菜单
     * @param parentId
     * @return
     */
    public List<Router> getMenuListByParentId(Long parentId);

    /**
     * 父节点获取菜单树
     * @param parentId
     * @param flag 初始化子节点,空和0 初始化，1 不初始化
     * @return
     */
    public List<TreeNode> getMenuTree(Long parentId, Integer flag);

    /**
     * 根据id获取菜单详情
     * @param id
     * @return
     */
    public Router getRouterById(Long id);

    /**
     * 根据id获取路由
     * @param idList
     * @return
     */
    Map<String, List<Router>> getRoutersByIds(List<Long> idList);
}

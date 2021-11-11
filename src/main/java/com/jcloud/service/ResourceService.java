package com.jcloud.service;

import com.jcloud.bean.LabelNode;
import com.jcloud.bean.ResourceBean;
import com.jcloud.bean.TreeNode;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.core.service.CrudListService;
import com.jcloud.entity.Resource;

import java.util.List;

public interface ResourceService extends CrudListService<Resource, ResourceBean> {

    public List<TreeNode> getResourceTree();

    public ResponseData save(ResourceBean bean);

    public List<LabelNode> menuResources(Long menuId);

    /**
     * 资源树
     * @return
     */
    public List<TreeNode> getTree(Long parentId, Integer initChild);

    public ResourceBean info(Long id);

}

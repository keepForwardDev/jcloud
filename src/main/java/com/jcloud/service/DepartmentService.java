package com.jcloud.service;

import com.jcloud.bean.DepartmentBean;
import com.jcloud.bean.TreeNode;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.core.service.CrudListService;
import com.jcloud.entity.Department;

import java.util.List;

public interface DepartmentService extends CrudListService<Department, DepartmentBean> {

    public List<TreeNode> getDepartmentTree();

    public DepartmentBean getInfo(Long id);

    /**
     * 查询部门列表信息
     * @return
     */
	ResponseData listDepartment();

}

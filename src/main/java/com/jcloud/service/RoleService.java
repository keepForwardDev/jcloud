package com.jcloud.service;

import com.jcloud.bean.LabelNode;
import com.jcloud.bean.RoleBean;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.core.service.CrudListService;
import com.jcloud.entity.Role;

import java.util.List;

public interface RoleService extends CrudListService<Role, RoleBean> {

    public ResponseData saveRole(RoleBean roleBean);

    public void deleteRole(Long id);

    public List<LabelNode> getLabelNodes();
}

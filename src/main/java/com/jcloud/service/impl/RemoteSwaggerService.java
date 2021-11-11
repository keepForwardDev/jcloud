package com.jcloud.service.impl;

import com.jcloud.bean.ApiLimit;
import com.jcloud.bean.TreeNode;
import com.jcloud.core.config.swagger.SwaggerResourceBean;
import com.jcloud.core.config.swagger.SwaggerResourceService;
import com.jcloud.utils.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.*;

/**
 * @author jiaxm
 * @date 2021/9/3
 */
@Service
public class RemoteSwaggerService {

    @Autowired
    private SwaggerResourceService swaggerResourceService;

    public SwaggerResourceBean getResource(String serviceId) {
        SwaggerResourceBean swaggerResourceBean = swaggerResourceService.getSwaggerResource(null);
        Map<Object, TreeNode> pathMap = new LinkedHashMap<>();
        TreeNodeUtil.treeToMap(swaggerResourceBean.getResourceTree(), ReflectionUtils.findField(TreeNode.class, "extra"), pathMap);
        swaggerResourceBean.setPathMap(pathMap);
        return swaggerResourceBean;
    }




    /**
     * 设置api limit,排除不存在得api
     * @param apiLimit
     * @param swaggerResourceBean
     */
    public List<Long> formatApiLimit(List<ApiLimit> apiLimit, SwaggerResourceBean swaggerResourceBean) {
        Map<Object, TreeNode> pathMap = swaggerResourceBean.getPathMap();
        pathMap.forEach((k, v) -> {
            ApiLimit apiLimit1 = new ApiLimit();
            apiLimit1.setApiPath(k.toString());
            v.setData(apiLimit1);
        });
        ListIterator<ApiLimit> listIterator = apiLimit.listIterator();
        List<Long> selectIndex = new ArrayList<>();
        while (listIterator.hasNext()) {
            ApiLimit labelNode = listIterator.next();
            TreeNode resourceNode = pathMap.get(labelNode.getApiPath());
            if (resourceNode != null) {
                resourceNode.setData(labelNode); // 设置api limit
                selectIndex.add(resourceNode.getId());
            } else {
                listIterator.remove();
            }
        }

        return selectIndex;
    }

}

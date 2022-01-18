package com.jcloud.core.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

/**
 * 修改request中的内容
 *
 * @author jiaxm
 * @date 2022/1/13
 */
public class ModifyPageSizeRequestWrapper extends HttpServletRequestWrapper {

    private final String pageSizeStr = "pageSize";

    private Long maxPageSize;

    public ModifyPageSizeRequestWrapper(HttpServletRequest request, Long maxPageSize) {
        super(request);
        Assert.notNull(maxPageSize, "maxPageSize is required");
        this.maxPageSize = maxPageSize;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        try {
            if (pageSizeStr.equals(name) && StringUtils.isNotBlank(value)) {
                Long pageSize = Long.valueOf(value);
                if (maxPageSize <= pageSize) { // 当传入的参数比默认值大
                    return maxPageSize.toString();
                }
            }
        } catch (Exception e) {

        }
        return value;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> valueMaps = super.getParameterMap();
        if (valueMaps.containsKey(pageSizeStr)) {
            valueMaps.put(pageSizeStr, getParameterValues(pageSizeStr));
        }
        return valueMaps;
    }

    @Override
    public String[] getParameterValues(String name) {
        if (pageSizeStr.equals(name)) {
            return new String[]{getParameter(name)};
        }
        return super.getParameterValues(name);
    }
}

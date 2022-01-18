package com.jcloud.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 扫描类工具
 *
 * @author jiaxm
 * @date 2022/1/4
 */
public class ClassPathScanUtil {

    /**
     * 扫描包中的所有类
     * @param pkg 基准包位置
     * @param typeFilters 过滤规则
     * @return
     */
    public static Set<String> scanPackage(String pkg, Set<TypeFilter> typeFilters) {
        Set<String> classNames = new LinkedHashSet<>();
        try {
            String searchPaths = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + "/**/*.class";
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(searchPaths);
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
            for (Resource resource : resources) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                String className = metadataReader.getClassMetadata().getClassName();
                if (resource.isReadable() && matchesFilter(metadataReader, metadataReaderFactory, typeFilters)) {
                    classNames.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    /**
     * 扫描匹配
     * @param reader
     * @param readerFactory
     * @param typeFilters
     * @return
     * @throws IOException
     */
    private static boolean matchesFilter(MetadataReader reader, MetadataReaderFactory readerFactory, Set<TypeFilter> typeFilters) throws IOException {
        if (CollectionUtil.isEmpty(typeFilters)) {
            return true;
        }
        for (TypeFilter filter : typeFilters) {
            if (filter.match(reader, readerFactory)) {
                return true;
            }
        }
        return false;
    }
}

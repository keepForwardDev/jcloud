package com.jcloud.core.support;

import com.jcloud.utils.ClassPathScanUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.internal.BootstrapContextImpl;
import org.hibernate.boot.internal.MetadataBuilderImpl;
import org.hibernate.boot.model.process.internal.ManagedResourcesImpl;
import org.hibernate.boot.model.process.spi.ManagedResources;
import org.hibernate.boot.model.process.spi.MetadataBuildingProcess;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataBuildingOptions;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import javax.persistence.Converter;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * hibernate 自动建表，更新表
 * @author jiaxm
 * @date 2022/1/4
 */
@Slf4j
@Component
public class HibernateDDLUpdate implements InitializingBean {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Value("${spring.jpa.database-platform:org.hibernate.dialect.MySQL5InnoDBDialect}")
    private String databasePlatform;

    @Value("${spring.jpa.show-sql:true}")
    private Boolean showSql;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("scan entity to update");
        updateDDL();
        log.info("finish hibernate update");
    }

    private Map<String, Object> getHibernateSetting() {
        Map configurationValues = new HashMap();
        configurationValues.put("hibernate.hbm2ddl.auto", hibernateProperties.getDdlAuto());
        configurationValues.put("hibernate.dialect", databasePlatform);
        configurationValues.put("hibernate.show_sql", showSql);
        configurationValues.put("hibernate.physical_naming_strategy", hibernateProperties.getNaming().getPhysicalStrategy());
        configurationValues.put("hibernate.archive.scanner", "org.hibernate.boot.archive.scan.internal.DisabledScanner");
        configurationValues.put("hibernate.connection.datasource", dataSource);
        return configurationValues;
    }


    private void updateDDL() {
        // 根据设置注册hibernate 服务
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(getHibernateSetting()).build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry, true);
        MetadataBuildingOptions metadataBuildingOptions = new MetadataBuilderImpl.MetadataBuildingOptionsImpl(serviceRegistry);
        BootstrapContextImpl bootstrapContext = new BootstrapContextImpl(serviceRegistry, metadataBuildingOptions);
        // 扫描所有实体类
        Set<String> entityClazz = ClassPathScanUtil.scanPackage("com.jcloud", getTypeFilter());
        entityClazz.stream().forEach(r -> metadataSources.addAnnotatedClassName(r));
        ManagedResources managedResources = ManagedResourcesImpl.baseline(metadataSources, bootstrapContext);
        // 为每个实体类创建meta
        MetadataImplementor metadataImplementor = MetadataBuildingProcess.complete(managedResources, bootstrapContext, metadataBuildingOptions);
        // 数据库字段和class 字段对比更新
        SchemaManagementToolCoordinator.process(metadataImplementor, serviceRegistry, getHibernateSetting(), null);
        serviceRegistry.close();
    }


    /**
     * 扫描有这些注解的类
     *
     * @return
     */
    private Set<TypeFilter> getTypeFilter() {
        Set<TypeFilter> entityTypeFilters = new LinkedHashSet<>(8);
        entityTypeFilters.add(new AnnotationTypeFilter(Entity.class, false));
        entityTypeFilters.add(new AnnotationTypeFilter(Embeddable.class, false));
        entityTypeFilters.add(new AnnotationTypeFilter(MappedSuperclass.class, false));
        entityTypeFilters.add(new AnnotationTypeFilter(Converter.class, false));
        return entityTypeFilters;
    }

    @Bean
    @ConditionalOnMissingBean(HibernateProperties.class)
    public HibernateProperties hibernateProperties() {
        return new HibernateProperties();
    }
}

package com.randioo.randioo_server_base.service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.randioo.randioo_server_base.annotation.BaseServiceAnnotation;
import com.randioo.randioo_server_base.utils.PackageUtil;
import com.randioo.randioo_server_base.utils.SpringContext;

public class ServiceManager {

    private String basePackage;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 获得系统的注解
     * 
     * @param clazz
     * @return
     * @author wcy 2017年9月7日
     */
    private BaseServiceAnnotation getBaseServiceAnnotation(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof BaseServiceAnnotation) {
                return (BaseServiceAnnotation) annotation;
            }
        }
        return null;
    }

    public void initServices() {

        List<Class<?>> classes = PackageUtil.getClasses(basePackage);

        List<BaseServiceInterface> services = new ArrayList<>();

        for (Class<?> clazz : classes) {
            BaseServiceAnnotation annotation = getBaseServiceAnnotation(clazz);
            if (annotation == null) {
                continue;
            }
            String beanId = annotation.value();
            BaseServiceInterface baseService = SpringContext.getBean(beanId);
            services.add(baseService);
        }

        Collections.sort(services, new Comparator<BaseServiceInterface>() {

            @Override
            public int compare(BaseServiceInterface o1, BaseServiceInterface o2) {
                BaseServiceAnnotation annotation1 = getBaseServiceAnnotation(o1.getClass());
                BaseServiceAnnotation annotation2 = getBaseServiceAnnotation(o2.getClass());

                return annotation1.order() - annotation2.order();
            }
        });

        for (BaseServiceInterface baseService : services) {
            baseService.init();
            baseService.initNavigation();
        }

        for (BaseServiceInterface baseService : services) {
            baseService.initService();
        }
    }
}
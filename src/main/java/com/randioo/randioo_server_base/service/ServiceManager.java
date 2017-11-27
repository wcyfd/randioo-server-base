package com.randioo.randioo_server_base.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.randioo.randioo_server_base.annotation.BaseServiceAnnotation;
import com.randioo.randioo_server_base.annotation.PTAnnotation;
import com.randioo.randioo_server_base.annotation.PTStringAnnotation;
import com.randioo.randioo_server_base.navigation.Navigation;
import com.randioo.randioo_server_base.template.IActionSupport;
import com.randioo.randioo_server_base.utils.PackageUtil;

public class ServiceManager implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ServiceManager.class);
    private String basePackage;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    List<BaseServiceInterface> services = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        List<Class<?>> classes = PackageUtil.getClasses(basePackage);

        for (Class<?> clazz : classes) {
            BaseServiceAnnotation annotation = getBaseServiceAnnotation(clazz);
            if (annotation == null) {
                continue;
            }
            String beanId = annotation.value();
            BaseServiceInterface baseService = (BaseServiceInterface) context.getBean(beanId);
            logger.debug("regist service {}", beanId);
            initNavigation(context, baseService);
            services.add(baseService);
        }
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

    private void initNavigation(ApplicationContext context, BaseServiceInterface baseService) {
        String packageName = PackageUtil.getParent(baseService.getClass().getPackage().getName()) + ".action";
        List<Class<?>> classes = PackageUtil.getClasses(packageName);
        try {
            Field field = Navigation.class.getDeclaredField("navigate");
            field.setAccessible(true);
            for (Class<?> clazz : classes) {
                String key = this.getKeyName(clazz);
                if (key != null) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, IActionSupport> navigate = (Map<String, IActionSupport>) field.get(null);
                        IActionSupport action = (IActionSupport) context.getBean(clazz);
                        navigate.put(key, action);
                        logger.debug("load action {}={}", key, action);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        logger.error(key + " error ", e);
                    }
                }
            }
            field.setAccessible(false);
        } catch (NoSuchFieldException | SecurityException e) {
            logger.error("", e);
        }
    }

    /**
     * 获得action的key
     * 
     * @param clazz
     * @return
     * @author wcy 2017年2月13日
     */
    private String getKeyName(Class<?> clazz) {
        String key = null;
        PTAnnotation pt = clazz.getAnnotation(PTAnnotation.class);
        PTStringAnnotation ptString = clazz.getAnnotation(PTStringAnnotation.class);
        if (pt != null) {
            key = pt.value().getSimpleName();
        } else if (ptString != null) {
            key = ptString.value();
        }
        return key;
    }

    public void initServices() {

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
        }

        for (BaseServiceInterface baseService : services) {
            baseService.initService();
        }
        services.clear();
    }

}
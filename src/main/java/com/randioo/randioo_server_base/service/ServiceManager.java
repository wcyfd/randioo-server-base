package com.randioo.randioo_server_base.service;

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
import org.springframework.util.ReflectionUtils;

import com.randioo.randioo_server_base.annotation.BaseServiceAnnotation;
import com.randioo.randioo_server_base.annotation.PTAnnotation;
import com.randioo.randioo_server_base.annotation.PTStringAnnotation;
import com.randioo.randioo_server_base.navigation.Navigation;
import com.randioo.randioo_server_base.template.IActionSupport;

public class ServiceManager implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ServiceManager.class);

    List<BaseServiceInterface> services = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        initNavigation(context);

        Map<String, Object> map = context.getBeansWithAnnotation(BaseServiceAnnotation.class);
        for (Map.Entry<String, Object> entrySet : map.entrySet()) {
            String beanId = entrySet.getKey();
            BaseServiceInterface baseService = (BaseServiceInterface) entrySet.getValue();
            logger.debug("regist service {}", beanId);
            services.add(baseService);
        }
    }

    private void initNavigation(ApplicationContext context) {
        Map<String, IActionSupport> actionSupportMap = context.getBeansOfType(IActionSupport.class);
        Field field = ReflectionUtils.findField(Navigation.class, "navigate");
        ReflectionUtils.makeAccessible(field);
        for (Map.Entry<String, IActionSupport> entrySet : actionSupportMap.entrySet()) {
            IActionSupport action = entrySet.getValue();
            String key = this.getKeyName(action.getClass());
            if (key == null) {
                continue;
            }
            try {
                @SuppressWarnings("unchecked")
                Map<String, IActionSupport> navigate = (Map<String, IActionSupport>) field.get(null);
                navigate.put(key, action);
                logger.debug("load action {}={}", key, action);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(key + " error ", e);
            }
        }
        field.setAccessible(false);
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
                BaseServiceAnnotation annotation1 = o1.getClass().getAnnotation(BaseServiceAnnotation.class);
                BaseServiceAnnotation annotation2 = o2.getClass().getAnnotation(BaseServiceAnnotation.class);

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
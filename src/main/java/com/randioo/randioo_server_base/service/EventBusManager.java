package com.randioo.randioo_server_base.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.randioo.randioo_server_base.eventbus.Listener;
import com.randioo.randioo_server_base.utils.PackageUtil;

public class EventBusManager implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(EventBusManager.class);

    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;
    private String basePackage;

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setAsyncEventBus(AsyncEventBus asyncEventBus) {
        this.asyncEventBus = asyncEventBus;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        List<Class<?>> classes = PackageUtil.getClasses(basePackage);

        for (Class<?> clazz : classes) {
            if (!isListener(clazz)) {
                return;
            }

            Listener listener = (Listener) arg0.getBean(clazz);
            logger.info("scan listener {}", listener);

            if (isAsyncListener(listener)) {
                logger.info("asyncEventBus regist {}", listener);
                if (asyncEventBus != null) {
                    asyncEventBus.register(listener);
                }
            }

            if (isSyncListener(listener)) {
                logger.info("eventBus regist {}", listener);
                if (eventBus != null) {
                    eventBus.register(listener);
                }
            }
        }
    }

    // 默认不填就是同步的消息
    private boolean isSyncListener(Listener listener) {
        Async async = listener.getClass().getAnnotation(Async.class);
        if (async == null) {
            return true;
        }
        Sync sync = listener.getClass().getAnnotation(Sync.class);
        return sync != null;
    }

    private boolean isAsyncListener(Listener listener) {
        Async async = listener.getClass().getAnnotation(Async.class);
        return async != null;
    }

    private boolean isListener(Class<?> clazz) {
        Class<?>[] interfaceClasses = clazz.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass == Listener.class) {
                return true;
            }
        }

        return false;
    }

}

package com.randioo.randioo_server_base.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.randioo.randioo_server_base.eventbus.Listener;

public class EventBusManager implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(EventBusManager.class);

    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setAsyncEventBus(AsyncEventBus asyncEventBus) {
        this.asyncEventBus = asyncEventBus;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Map<String, Listener> listenerMap = context.getBeansOfType(Listener.class);

        for (Map.Entry<String, Listener> entrySet : listenerMap.entrySet()) {
            Listener listener = entrySet.getValue();
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

}

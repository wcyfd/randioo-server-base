package com.randioo.randioo_server_base.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SchedulerManager implements SchedulerInterface, ApplicationContextAware {
    private List<SchedulerInterface> schedulers = new ArrayList<>();

    public void start() {
        for (SchedulerInterface scheduler : schedulers) {
            scheduler.start();
        }
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) throws Exception {
        for (SchedulerInterface scheduler : schedulers) {
            scheduler.shutdown(timeout, unit);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, SchedulerInterface> map = applicationContext.getBeansOfType(SchedulerInterface.class);
        for (SchedulerInterface scheduler : map.values()) {
            if (scheduler == this) {
                continue;
            }
            schedulers.add(scheduler);
        }
    }
}

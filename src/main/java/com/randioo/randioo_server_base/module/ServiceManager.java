package com.randioo.randioo_server_base.module;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {
	private List<BaseService> services = new ArrayList<>();

	public void setServices(List<BaseService> services) {
		this.services = services;
	}
	public List<BaseService> getServices() {
		return services;
	}

	public void initServices() {
		for (BaseService baseService : services) {
			baseService.init();
			baseService.initNavigation();
		}
	}
}

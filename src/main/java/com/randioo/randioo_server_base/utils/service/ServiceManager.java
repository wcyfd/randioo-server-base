package com.randioo.randioo_server_base.utils.service;

import java.util.ArrayList;
import java.util.List;

import com.randioo.randioo_server_base.module.BaseServiceInterface;

public class ServiceManager {
	private List<BaseServiceInterface> services = new ArrayList<>();

	public void setServices(List<BaseServiceInterface> services) {
		this.services = services;
	}

	public List<BaseServiceInterface> getServices() {
		return services;
	}

	public void initServices() {
		for (BaseServiceInterface baseService : services) {
			baseService.init();
			baseService.initNavigation();
		}
	}
}

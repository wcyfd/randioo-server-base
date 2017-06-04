package com.randioo.randioo_server_base.service;

import java.util.HashSet;
import java.util.Set;

import com.randioo.randioo_server_base.template.Observer;

public class ObserveBaseService extends BaseService implements ObserveBaseServiceInterface {

	private Set<Observer> observers = new HashSet<>();

	@Override
	public void addObserver(Observer o) {
		if (o == null)
			throw new NullPointerException();
		
		observers.add(o);
	}

	@Override
	public void deleteObserver(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers(String msg, Object... args) {
		for (Observer observer : observers) {
			try {
				observer.update(this, msg, args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteObservers() {
		observers.clear();
	}

	@Override
	public int countObservers() {
		return observers.size();
	}

	@Override
	public void update(Observer observer, String msg, Object... args) {

	}

}

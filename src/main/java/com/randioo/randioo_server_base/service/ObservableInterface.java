package com.randioo.randioo_server_base.service;

import com.randioo.randioo_server_base.template.Observer;

public interface ObservableInterface {
	public void addObserver(Observer o);

	public void deleteObserver(Observer o);

	public void notifyObservers(String msg, Object... args);

	public void deleteObservers();

	public int countObservers();
}

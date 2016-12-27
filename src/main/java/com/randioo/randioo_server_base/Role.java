package com.randioo.randioo_server_base;

import com.randioo.randioo_server_base.module.record.Recordable;

/**
 * Hello world!
 *
 */
public class Role implements Recordable {
	private int money;

	public void setMoney(int money) {
		this.money = money;
	}

	public int getMoney() {
		return money;
	}

	private War war;

	public void setWar(War war) {
		this.war = war;
	}

	public War getWar() {
		return war;
	}
}

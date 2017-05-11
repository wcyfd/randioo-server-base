package com.randioo.randioo_server_base.utils.db;

public class DataEntity implements Saveable {

	private boolean change;

	@Override
	public void setChange(boolean change) {
		this.change = change;
	}

	@Override
	public boolean isChange() {
		if (!change) {
			change = checkChange();
		}
		return change;
	}

	@Override
	public boolean checkChange() {
		return false;
	}

}

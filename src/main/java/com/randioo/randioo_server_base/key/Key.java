package com.randioo.randioo_server_base.key;

public class Key {
	protected KeyStore keyStore;
	private Object value;

	/**
	 * 钥匙回收
	 * 
	 * @author wcy 2017年7月13日
	 */
	public void recycle() {
		if (keyStore == null)
			return;
		
		synchronized (keyStore.keys) {
			reset();
			keyStore.keys.add(this);
		}
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	protected void reset() {

	}
}

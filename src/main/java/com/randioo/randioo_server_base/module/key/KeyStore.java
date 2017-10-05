package com.randioo.randioo_server_base.module.key;

import java.util.ArrayList;
import java.util.List;

import com.randioo.randioo_server_base.utils.RandomUtils;

public class KeyStore {

	protected List<Key> keys = new ArrayList<>();

	public Key getRandomKey() throws KeyStoreEmptyException {
		synchronized (keys) {
			if (keys.size() == 0)
				throw new KeyStoreEmptyException();
			int index = RandomUtils.getRandomNum(keys.size());
			Key key = keys.remove(index);
			return key;
		}
	}

	public void putKey(Key key) {
		if (key == null)
			return;
		key.keyStore = this;

		synchronized (keys) {
			keys.add(key);
		}
	}

	public int size() {
		synchronized (keys) {
			return keys.size();
		}
	}

}

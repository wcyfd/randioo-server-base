package com.randioo.randioo_server_base.sensitive;

import java.util.List;

public interface WordFilter {
	public List<String> getWord(String line);
}

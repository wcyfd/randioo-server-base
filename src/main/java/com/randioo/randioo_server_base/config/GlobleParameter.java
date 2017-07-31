package com.randioo.randioo_server_base.config;

public class GlobleParameter {

	protected String key;
	protected String value;
	protected GlobleTypeEnum type;

	public void setKey(String key) {
		this.key = key;
	}

	public void setType(GlobleTypeEnum type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("key=").append(key).append(",");
		sb.append("value=").append(value).append(",");
		sb.append("type=").append(type);
		return sb.toString();
	}

}

package com.randioo.randioo_server_base.config;

public enum GlobleTypeEnum {
	GLOBLE_TYPE_INT("int"), GLOBLE_TYPE_STRING("string"), GLOBLE_TYPE_DOUBLE("double"), GLOBLE_TYPE_FLOAT(
			"float"), GLOBLE_TYPE_BOOLEAN("boolean");

	public String name;

	GlobleTypeEnum(String name) {
		this.name = name;
	}

	public static GlobleTypeEnum getGlobleType(String name) {
		switch (name) {
		case "int":
			return GLOBLE_TYPE_INT;
		case "string":
			return GLOBLE_TYPE_STRING;
		case "double":
			return GLOBLE_TYPE_DOUBLE;
		case "float":
			return GLOBLE_TYPE_FLOAT;
		case "boolean":
			return GLOBLE_TYPE_BOOLEAN;
		default:
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("(").append(name).append(")");
		return sb.toString();
	}

}

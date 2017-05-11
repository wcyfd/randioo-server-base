package com.randioo.randioo_server_base.module.friend;

public class RelationKey {
	private String key;
	private int roleId1;
	private int roleId2;

	public RelationKey build() {
		int v1 = roleId1;
		int v2 = roleId2;
		this.roleId1 = Math.min(v1, v2);
		this.roleId2 = Math.max(v1, v2);
		this.key = roleId1 + "," + roleId2;
		return this;
	}

	public RelationKey setKey(String key) {
		String[] data = key.split(",");
		roleId1 = Integer.parseInt(data[0]);
		roleId2 = Integer.parseInt(data[1]);
		return this;
	}

	public RelationKey setRoleId1(int roleId1) {
		this.roleId1 = roleId1;
		return this;
	}

	public RelationKey setRoleId2(int roleId2) {
		this.roleId2 = roleId2;
		return this;
	}

	public String getKey() {
		return key;
	}

	public int getRoleId1() {
		return roleId1;
	}

	public int getRoleId2() {
		return roleId2;
	}
}

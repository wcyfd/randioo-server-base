package com.randioo.randioo_server_base.module.friend;

public class Relation {

	private int roleId1;
	private int roleId2;
	private byte roleId1Status;
	private byte roleId2Status;

	public void setRelationKey(RelationKey relationKey) {
		this.roleId1 = relationKey.getRoleId1();
		this.roleId2 = relationKey.getRoleId2();
	}

	public int getRoleId1() {
		return roleId1;
	}

	public void setRoleId1(int roleId1) {
		this.roleId1 = roleId1;
	}

	public int getRoleId2() {
		return roleId2;
	}

	public void setRoleId2(int roleId2) {
		this.roleId2 = roleId2;
	}

	public byte getRoleId1Status() {
		return roleId1Status;
	}

	public void setRoleId1Status(byte roleId1Status) {
		this.roleId1Status = roleId1Status;
	}

	public byte getRoleId2Status() {
		return roleId2Status;
	}

	public void setRoleId2Status(byte roleId2Status) {
		this.roleId2Status = roleId2Status;
	}

}

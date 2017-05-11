package com.randioo.randioo_server_base.module.chat;

public class ChatInfo implements Comparable<ChatInfo> {
	private int key;
	private int targetKey;
	private int time;
	private String txt;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	@Override
	public int compareTo(ChatInfo o) {
		return time - o.time;
	}

	public int getTargetKey() {
		return targetKey;
	}

	public void setTargetKey(int targetKey) {
		this.targetKey = targetKey;
	}

}

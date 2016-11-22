package com.randioo.randioo_server_base.utils;

public class StringJoiner {
	private StringBuilder sb = null;
	private CharSequence prefix = null;
	private CharSequence suffix = null;
	private CharSequence delimiter = null;
	private CharSequence emptyValue = null;

	public StringJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		this.sb = new StringBuilder();
		this.prefix = prefix;
		this.suffix = suffix;
		this.delimiter = delimiter;
		this.emptyValue = null;
	}

	public StringJoiner add(CharSequence newElement) {
		if (sb.length() > 1) {
			sb.append(delimiter);
		}

		sb.append(newElement);
		return this;

	}

	public StringJoiner merge(StringJoiner other) {
		add(other.sb.substring(0));
		return this;
	}

	public StringJoiner setEmptyValue(CharSequence emptyValue) {
		this.emptyValue = emptyValue;
		return this;
	}

	public int length() {
		return toString().length();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder tempSB = new StringBuilder();
		tempSB.append(prefix).append(sb.length() == 0 ? this.emptyValue : sb).append(suffix);
		return tempSB.toString();
	}

}

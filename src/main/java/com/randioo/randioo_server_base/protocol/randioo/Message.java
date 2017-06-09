package com.randioo.randioo_server_base.protocol.randioo;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.randioo.randioo_server_base.config.ServerConfig;

public class Message {
	private static final Charset charset = Charset.forName("UTF-8");
	private short type;
	private IoBuffer data;
	private int dataLength;
	private static final byte TRUE = 1;
	private static final byte FALSE = 0;

	public Message() {
		this.data = IoBuffer.wrap(new byte[0]);
		this.data.order(ByteOrder.LITTLE_ENDIAN);
		this.data.setAutoExpand(true);
		this.dataLength = 0;
	}

	public Message(short type) {
		this.type = type;
	}

	public boolean error() {
		return (this.type == -1);
	}

	public void putInt(int intParam) {
		this.data.put(ServerConfig.getMsgInt());
		this.data.putInt(intParam);
		this.dataLength += 5;
	}

	public int getInt() {
		this.data.get();
		return this.data.getInt();
	}

	public void putBytes(byte[] bytes) {
		this.data.put(ServerConfig.getMsgBytes());
		System.out.println(bytes.length);
		this.data.putInt(bytes.length);
		this.data.put(bytes);
		this.dataLength = (this.dataLength + bytes.length + 5);
	}

	public byte[] getBytes() {
		this.data.get();
		int strLen = this.data.getInt();
		byte[] strBytes = new byte[strLen];
		this.data.get(strBytes);
		return strBytes;
	}

	public void putLong(long longParam) {
		this.data.put(ServerConfig.getMsgLong());
		this.data.putLong(longParam);
		this.dataLength += 9;
	}

	public long getLong() {
		this.data.get();
		return this.data.getLong();
	}

	public void putDouble(double longParam) {
		this.data.put(ServerConfig.getMsgDouble());
		this.data.putDouble(longParam);
		this.dataLength += 9;
	}

	public double getDouble() {
		this.data.get();
		return this.data.getDouble();
	}

	public void putBoolean(boolean b) {
		this.data.put(ServerConfig.getMsgBoolean());
		this.data.put(b ? TRUE : FALSE);
		this.dataLength += 2;
	}

	public void putBooleanToInt(boolean b) {
		if (b)
			putInt(1);
		else
			putInt(0);
	}

	public boolean getIntToBoolean() {
		int b = getInt();

		return (b == 1);
	}

	public boolean getBoolean() {
		this.data.get();
		byte b = this.data.get();

		return (b == 1);
	}

	public void put(byte b) {
		this.data.put(ServerConfig.getMsgByte());
		this.data.put(b);
		this.dataLength += 2;
	}

	public byte get() {
		this.data.get();
		return this.data.get();
	}

	public void putShort(short shortParam) {
		this.data.put(ServerConfig.getMsgShort());
		this.data.putShort(shortParam);
		this.dataLength += 3;
	}

	public short getShort() {
		this.data.get();
		return this.data.getShort();
	}

	public void putString(String stringParam) {
		this.data.put(ServerConfig.getMsgStr());
		byte[] msgBytes = stringParam.getBytes(charset);
		this.data.putInt(msgBytes.length);
		this.data.put(msgBytes);
		this.dataLength = (this.dataLength + msgBytes.length + 5);
	}

	public String getString() {
		this.data.get();
		int strLen = this.data.getInt();
		byte[] strBytes = new byte[strLen];
		this.data.get(strBytes);
		String msgStr = new String(strBytes, charset).trim();
		return msgStr;
	}

	public short getType() {
		return this.type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public IoBuffer getData() {
		IoBuffer cloneData = this.data.duplicate();
		cloneData.order(ByteOrder.LITTLE_ENDIAN);
		cloneData.rewind();
		return cloneData;
	}

	public void setData(IoBuffer params) {
		this.data = params;
	}

	public int getDataLength() {
		return this.dataLength;
	}

	public void setDataLength() {
		this.dataLength = this.data.limit();
	}

	public IoBuffer flip() {
		return this.data.flip();
	}

	public IoBuffer rewind() {
		return this.data.rewind();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		short type = this.getType(); // 指令号
		sb.append(type).append(" { ").append(System.lineSeparator());
		int i = 1;
		IoBuffer iob = this.getData();
		while (iob.hasRemaining()) {
			byte paramType = iob.get();
			switch (paramType) {
			case 3:
				sb.append("short : ").append(iob.getShort()).append(System.lineSeparator());
				break;
			case 1:
				sb.append("int : ").append(iob.getInt()).append(System.lineSeparator());
				break;
			case 4:
				sb.append("long : ").append(iob.getLong()).append(System.lineSeparator());
				break;
			case 2:
				int length = iob.getInt();
				byte[] strBytes = new byte[length];
				iob.get(strBytes);
				try {
					String msgStr = new String(strBytes, "UTF-8");
					sb.append("length : ").append(length).append(" string : ").append(msgStr)
							.append(System.lineSeparator());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 5:
				byte bresult = iob.get();
				sb.append("bool : ").append(bresult == 1).append(System.lineSeparator());
				break;
			case 6:
				byte bresultNum = iob.get();
				sb.append("byte : ").append(bresultNum).append(System.lineSeparator());
				break;
			case 7:
				int blength = iob.getInt();
				byte[] bBytes = new byte[blength];
				iob.get(bBytes);
				for (byte b : bBytes)
					sb.append("length : ").append(blength).append(" bytes : ").append(b).append(" ");
				sb.append(System.lineSeparator());
				break;
			case 8:
				double doubleNum = iob.getDouble();
				sb.append("double : ").append(doubleNum).append(System.lineSeparator());
				break;
			default:
				sb.append("error byte field! index=" + i).append(System.lineSeparator());
			}
			i++;
		}
		sb.append(" }").append(System.lineSeparator());
		return sb.toString();
	}
}

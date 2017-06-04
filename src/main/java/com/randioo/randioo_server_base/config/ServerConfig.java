package com.randioo.randioo_server_base.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 服务基本配置
 * 
 * @author xjd
 *
 */
public class ServerConfig {
	private static Properties properties = null;

	private static int bufferSize		=4096;
	private static int idleTime		=10;
	private static String charSet		="UTF-8";
	// tcp服务端口
	private static int wanServerPort	=9999;
	private static int compress		=50;

	private static byte msgInt		=1;
	private static byte msgStr		=2;
	private static byte msgShort		=3;
	private static byte msgLong		=4;
	private static byte msgBoolean	=5;
	private static byte msgByte		=6;
	private static byte msgBytes		=7;
	private static byte msgDouble		=8;

	/**
	 * 初始加载config.properties数据
	 */
	public static void loadConfig(int port) {
		String fileName = "conf/serverConfig.properties";
		wanServerPort = port;
		try {
			InputStream input = new BufferedInputStream(new FileInputStream(new File(fileName)));
			properties = new Properties();
			properties.load(input);
			input.close();
			bufferSize = Integer.parseInt(properties.get("bufferSize").toString());
			compress = Integer.parseInt(properties.get("compress").toString());
//			wanServerPort = Integer.parseInt(properties.get("wanServerPort").toString());
			wanServerPort = port;
			idleTime = Integer.parseInt(properties.get("idleTime").toString());
			charSet = (String) properties.get("charSet");
			msgInt = Byte.parseByte(properties.get("msgInt").toString());
			msgStr = Byte.parseByte(properties.get("msgStr").toString());
			msgShort = Byte.parseByte(properties.get("msgShort").toString());
			msgLong = Byte.parseByte(properties.get("msgLong").toString());
			msgBoolean = Byte.parseByte(properties.get("msgBoolean").toString());
			msgByte = Byte.parseByte(properties.get("msgByte").toString());
			msgBytes = Byte.parseByte(properties.get("msgBytes").toString());
			msgDouble = Byte.parseByte(properties.get("msgDouble").toString());
		} catch (FileNotFoundException e) {
			System.out.println("no found 'conf/serverConfig.properties',use default config");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return 获得缓冲区大小
	 */
	public static int getBufferSize() {
		return bufferSize;
	}

	/**
	 * 
	 * @return 获取读写通道进入空闲状态时间
	 */
	public static int getIdleTime() {
		return idleTime;
	}

	/**
	 * 获取服务启动端口
	 */
	public static int getWanServerPort() {
		return wanServerPort;
	}

	/**
	 * 
	 * @return 获得字符编码设置
	 */
	public static String getCharSet() {
		return charSet;
	}

	/**
	 * 获取整型参数标识
	 * 
	 * @return
	 */
	public static byte getMsgInt() {
		return msgInt;
	}

	/**
	 * 获取字符参数标识
	 * 
	 * @return
	 */
	public static byte getMsgStr() {
		return msgStr;
	}

	/**
	 * 获取短整型参数标识
	 * 
	 * @return
	 */
	public static byte getMsgShort() {
		return msgShort;
	}

	/**
	 * 获取长型参数标识
	 * 
	 * @return
	 */
	public static byte getMsgLong() {
		return msgLong;
	}

	/**
	 * 获取boolean参数标识
	 * 
	 * @return
	 */
	public static byte getMsgBoolean() {
		return msgBoolean;
	}

	public static byte getMsgByte() {
		return msgByte;
	}

	public static int getCompress() {
		return compress;
	}

	public static byte getMsgBytes() {
		return msgBytes;
	}

	public static byte getMsgDouble() {
		return msgDouble;
	}
}

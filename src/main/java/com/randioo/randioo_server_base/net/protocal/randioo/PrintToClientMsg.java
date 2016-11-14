package com.randioo.randioo_server_base.net.protocal.randioo;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.buffer.IoBuffer;

public class PrintToClientMsg {
	

	public static void printIOBuffer(Integer roleId, Message msg) {
		short type = msg.getType(); // 指令号
		// logger.debug("服务器{}{}的消息，{}指令号为{}", new Object[] { sendType, roleId,
		// sendType, type });
		if (type == (short) 20104) {
			System.out.print(104);
			return;
		}
		boolean b = true;
//		GameLog log = null;
//		if (roleId != null/*&&type !=20104*/) {
//			log = new GameLog();
//			log.setRoleId(roleId);
//			log.setType(type);
//		}

		System.out.println("服务器回写" + roleId + "消息，指令号为:" + type);
		if (!b)
			return;
		IoBuffer iob = msg.getData();
		while (iob.hasRemaining()) {
			byte paramType = iob.get();
			switch (paramType) {
			case 3:
				// logger.debug("roleId:{}--{}指令号为{},short型参数:{}", new Object[]
				// {
				// roleId, sendType, type, iob.getShort() });
				short shortData = iob.getShort();
				System.out.println("服务器回写" + roleId + "指令号：" + type + ";" + "消息,short型参数:" + shortData);
//				if (log != null)
//					log.getJsonArray().put(shortData);
				break;
			case 1:
				// logger.debug("roleId:{}--{}指令号为{},int型参数:{}", new Object[] {
				// roleId, sendType, type, iob.getInt() });
				int intData = iob.getInt();
				System.out.println("服务器回写" + roleId + "指令号：" + type + ";" + "消息,int型参数:" + intData);
//				if (log != null)
//					log.getJsonArray().put(intData);
				break;
			case 4:
				// logger.debug("roleId:{}--{}指令号为{},long型参数:{}", new Object[] {
				// roleId, sendType, type, iob.getLong() });
				long longData = iob.getLong();
				System.out.println("服务器回写" + roleId + "指令号：" + type + ";" + "消息,long型参数:" + longData);
//				if (log != null)
//					log.getJsonArray().put(longData);
				break;
			case 2:
				int length = iob.getInt();
				byte[] strBytes = new byte[length];
				iob.get(strBytes);
				try {
					String msgStr = new String(strBytes, "UTF-8");
					// logger.debug("roleId:{}--{}指令号为{},字符参数长度: {}, 字符参数: {}",
					// new Object[] { roleId, sendType, type, length,
					// msgStr });
					System.out.println("服务器回写" + roleId + "指令号：" + type + ";" + "消息,字符参数长度:" + length + ", 字符参数:"
							+ msgStr);
//					if (log != null)
//						log.getJsonArray().put(msgStr);
				} catch (UnsupportedEncodingException e) {
					// logger.error(e.getMessage(), e);
					e.printStackTrace();
				}
				break;
			case 5:
				byte bresult = iob.get();

				System.out.println("服务器回写" + roleId + "指令号：" + type + ";" + "消息,boolean参数:" + bresult);
				boolean result = bresult == 1 ? true : false;
//				if (log != null)
//					log.getJsonArray().put(result);
				break;
			case 6:
				byte bresultNum = iob.get();

				System.out.println("服务器回写" + roleId + "指令号：" + type + ";" + "消息,byte参数:" + bresultNum);
//				if (log != null)
//					log.getJsonArray().put(bresultNum);
				break;
			case 7:
				int blength = iob.getInt();
				byte[] bBytes = new byte[blength];
				iob.get(bBytes);
				System.out.println("服务器回写" + roleId + "指令号：" + type + ";" + "消息,byte数组参数长度:" + blength);
//				if (log != null)
//					log.getJsonArray().put(bBytes);
				break;
			case 8:
				double doubleNum = iob.getDouble();
				System.out.println("服务器回写" + roleId + "指令号：" + type + ";" + "消息,double参数:" + doubleNum);
//				if (log != null)
//					log.getJsonArray().put(doubleNum);
				break;
			default:
				// logger.debug("没有对应参数类型！！！！");
				System.err.println("没有对应参数类型！！！！");
			}
		}

//		if (log != null)
//			GameLogCache.addGameLog(log);
	}

}

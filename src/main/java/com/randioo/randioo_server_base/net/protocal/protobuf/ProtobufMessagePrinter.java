package com.randioo.randioo_server_base.net.protocal.protobuf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.protobuf.ByteString;
import com.randioo.randioo_server_base.net.Protocal;
import com.randioo.randioo_server_base.net.Protocal.PT;

public class ProtobufMessagePrinter {
	public static void print(PT sc, Class<?> clazz) {
		try {
			Method method = clazz.getMethod("parseFrom", ByteString.class);
			Object obj = method.invoke(null, sc.getData());
			System.out.println(obj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

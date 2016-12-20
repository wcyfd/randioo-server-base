package com.randioo.randioo_server_base.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {
	/**
	 * 获得方法
	 * 
	 * @param clazz
	 * @param name
	 * @param parameterTypes
	 * @return
	 * @author wcy 2016年12月16日
	 */
	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		try {
			Method method = clazz.getMethod(name, parameterTypes);
			return method;
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 调用方法，并返回结果
	 * 
	 * @param obj
	 * @param method
	 * @param args
	 * @return
	 * @author wcy 2016年12月16日
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethodWithResult(Object obj, Method method, Object... args) {
		try {
			Object result = method.invoke(obj, args);
			return (T) result;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 调用结果
	 * 
	 * @param obj
	 * @param method
	 * @param args
	 * @author wcy 2016年12月16日
	 */
	public static void invokeMethod(Object obj, Method method, Object... args) {
		try {
			method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得所有声明的属性
	 * 
	 * @param clazz
	 * @return
	 * @author wcy 2016年12月16日
	 */
	public static Field[] getFields(Class<?> clazz) {
		return clazz.getDeclaredFields();
	}

	/**
	 * 获得属性值
	 * 
	 * @param obj
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object obj, Field field) {
		try {
			return (T) field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得静态属性的值
	 * 
	 * @param field
	 * @return
	 */
	public static <T> T getStaticFieldValue(Field field) {
		try {
			return (T) field.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}

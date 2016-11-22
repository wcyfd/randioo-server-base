package com.randioo.randioo_server_base.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ConfigLoader {
	public static void loadConfig(String packName, String zipPathName) {
		Map<String, Class<?>> classesMap = getClassesMap(packName);

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPathName))) {
			for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry()) {
				Class<?> clazz = classesMap.get(entry.getName());
				if (clazz == null) {
					continue;
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = zis.read(buffer)) >= 0) {
					baos.write(buffer, 0, len);
				}
				try {
					clazz.getMethod("parse", ByteBuffer.class).invoke(null, ByteBuffer.wrap(baos.toByteArray()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("load config complete");
	}
	
	public static Map<String,Class<?>> getClassesMap(String packName){
		List<Class<?>> classes = PackageUtil.getClasses(packName);
		Map<String, Class<?>> classesMap = new HashMap<>();
		try {
			for (Class<?> clazz : classes) {
				classesMap.put((String) clazz.getDeclaredField("urlKey").get(null), clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classesMap;
	}
}

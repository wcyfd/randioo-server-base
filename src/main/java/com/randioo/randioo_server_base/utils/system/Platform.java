package com.randioo.randioo_server_base.utils.system;

public class Platform {
	public enum OS {
		WIN, LINUX
	}

	private static OS os = null;

	public static OS getOS() {
		if (os == null) {
			String name = System.getProperty("os.name").toLowerCase();
			if (name.startsWith("win")) {
				os = OS.WIN;
			} else if (name.equals("linux")) {
				os = OS.LINUX;
			}
		}

		return os;
	}
}

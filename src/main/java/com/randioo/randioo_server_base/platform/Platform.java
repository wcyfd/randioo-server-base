package com.randioo.randioo_server_base.platform;

/**
 * 操作系统平台
 * 
 * @author wcy 2017年8月5日
 *
 */
public class Platform {
    public enum OS {
        WINDOWS, LINUX
    }

    private static OS os = null;

    public static OS getOS() {
        if (os == null) {
            String name = System.getProperty("os.name").toLowerCase();
            if (name.startsWith("win")) {
                os = OS.WINDOWS;
            } else if (name.equals("linux")) {
                os = OS.LINUX;
            }
        }

        return os;
    }
}

package com.randioo.randioo_server_base.log;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.randioo.randioo_server_base.entity.RoleInterface;

/**
 * 日志常用类
 * 
 * @author wcy 2017年9月6日
 *
 */
public class L {

    protected static String projectName = null;

    public static void setProjectName(String proj) {
        projectName = proj;
    }

    protected static boolean debug = false;

    /** 内网模式 */
    public static void setDebug(boolean value) {
        debug = value;
    }

    public static final String ROLE_ACCOUNT_TAG = "[";
    public static final String SYS_TAG = "";
    public static final String SPLIT_TAG = "]";
    /** 玩家格式 */
    private static String ROLE_LOG_FORMAT = ROLE_ACCOUNT_TAG + "{0}" + SPLIT_TAG + "[roleId:{1},name:{2}] * {3} * {4}";
    /** 系统格式 */
    private static String SYSTEM_LOG_FORMAT = SYS_TAG + " * {0} * {1}";

    public static String role(RoleInterface role, Object message) {
        String roleId = role != null ? role.getRoleId() + "" : null;
        String name = role != null ? role.getName() : null;
        String account = role != null ? role.getAccount() : null;

        String output = MessageFormat.format(ROLE_LOG_FORMAT, account, roleId, name, yyyy_MM_dd_HH_mm_ss(), message);
        // 如果是字符串类型直接返回
        if (message instanceof String) {
            return output;
        }

        // 如果不是则要考虑过滤换行符
        if (output.length() < 120)
            output = output.replaceAll("\n", " ").replace("\t", " ").replace("  ", "");
        return output;
    }

    public static String role(RoleInterface role, String format, Object... arguments) {
        String log = MessageFormat.format(format, arguments);
        return role(role, log);
    }

    public static String sys(Object message) {
        String output = MessageFormat.format(SYSTEM_LOG_FORMAT, yyyy_MM_dd_HH_mm_ss(), message);
        return output;
    }

    public static String sys(String format, Object... args) {
        String output = MessageFormat.format(format, args);
        return sys(output);
    }

    /**
     * 当前时间
     * 
     * @return
     * @author wcy 2017年9月6日
     */
    private static String yyyy_MM_dd_HH_mm_ss() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String nowTime = sdf.format(date);
        sdf = null;
        date = null;
        return nowTime;
    }
}

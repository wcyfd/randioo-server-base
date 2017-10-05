package com.randioo.randioo_server_base.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.randioo.randioo_server_base.GlobleConstant;
import com.randioo.randioo_server_base.config.GlobleMap;
import com.randioo.randioo_server_base.utils.StringUtils;

/**
 * 日志系统
 * 
 * @author wcy 2017年8月4日
 *
 */
public class LogSystem {

    protected static String loggerRootName = null;
    protected static String loggerUrl = null;

    /**
     * 初始化操作
     * 
     * @param name 服务器正式启动的main所在类
     * @author wcy 2017年8月4日
     */
    public static void init(Class<?> clazz) {
        checkLogSystemParam();

        loggerRootName = GlobleMap.String(GlobleConstant.ARGS_PROJECT_NAME);
        loggerUrl = GlobleMap.String(GlobleConstant.ARGS_LOG_URL);

        Logger logger = LoggerFactory.getLogger(clazz.getSimpleName());
        logger.info(GlobleMap.print());
    }

    private static void checkLogSystemParam() {
        if (StringUtils.isNullOrEmpty(GlobleMap.String(GlobleConstant.ARGS_PROJECT_NAME))) {
            System.out.println("项目名为空,在server.xml或命令行中设置'project'属性");
            System.exit(0);
        }
        if (StringUtils.isNullOrEmpty(GlobleMap.String(GlobleConstant.ARGS_LOG_URL))) {
            System.out.println("项目名为空,在server.xml或命令行中设置'lognet'属性");
            System.exit(0);
        }
    }

    public static String pretty(Object message) {
        // 如果是字符串类型直接返回
        if (message instanceof String) {
            return (String) message;
        }

        String output = message.toString();
        // 如果不是则要考虑过滤换行符
        if (output.length() < 120) {
            output = output.replaceAll("\n", " ").replace("\t", " ").replace("  ", "");
        }

        return output;
    }

}

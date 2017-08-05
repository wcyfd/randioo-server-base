package com.randioo.randioo_server_base.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.randioo.randioo_server_base.GlobleConstant;
import com.randioo.randioo_server_base.config.GlobleMap;
import com.randioo.randioo_server_base.log.HttpLogUtils;
import com.randioo.randioo_server_base.utils.StringUtils;

/**
 * 日志系统
 * 
 * @author wcy 2017年8月4日
 *
 */
public class LogSystem {
    /**
     * 初始化操作
     * 
     * @param name 服务器正式启动的main所在类
     * @author wcy 2017年8月4日
     */
    public static void init(Class<?> clazz) {
        checkLogSystemParam();

        String projectName = GlobleMap.String(GlobleConstant.ARGS_PROJECT_NAME);
        int port = GlobleMap.Int(GlobleConstant.ARGS_PORT);
        String projectString = projectName + port;

        HttpLogUtils.setProjectName(projectString);

        Logger logger = LoggerFactory.getLogger(clazz.getSimpleName());
        logger.info(HttpLogUtils.sys(GlobleMap.print()));
    }

    private static void checkLogSystemParam() {

        if (StringUtils.isNullOrEmpty(GlobleMap.String(GlobleConstant.ARGS_PROJECT_NAME))) {
            System.out.println("项目名为空,在server.xml或命令行中设置'project'属性");
            System.exit(0);
        }
        if (GlobleMap.Int(GlobleConstant.ARGS_PORT) == 0) {
            System.out.println("项目名为空,在server.xml或命令行中设置'port'属性");
            System.exit(0);

        }

    }

    public static void main(String[] args) {
        LogSystem.init(LogSystem.class);
    }
}

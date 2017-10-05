package com.randioo.randioo_server_base.config;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.randioo.randioo_server_base.utils.PackageUtil;

/**
 * 配置表加载器
 * 
 * @author wcy 2017年8月5日
 *
 */
public class ConfigLoader {

    private final static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    /**
     * 加载一个zip格式的资源包,并在指定的包路径下找到对应的类进行配置表对象的初始化工作<br>
     * zip格式的资源包由工具生成,在游戏项目的根目录的config文件夹中进行生成<br>
     * 除了资源包,还会生成代码,具体语言根据工具中的语言而定<br>
     * 需要对as_config.bat或java_config.bat进行相应的改动<br>
     * 
     * @param packName "com.randioo.demo_optimisticframe_server.entity.file"
     * @param zipPathName "./config.zip"
     * @author wcy 2017年1月10日
     */
    public static void loadConfig(String packName, String zipPathName) {
        Map<String, Class<?>> classesMap = getClassesMap(packName);

        // 读取zip资源包，并将数据注入到配置表对象的静态parse方法,进行初始化工作
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
                    // 通过反射 找到parse方法
                    clazz.getMethod("parse", ByteBuffer.class).invoke(null, ByteBuffer.wrap(baos.toByteArray()));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                baos.close();

            }
        } catch (FileNotFoundException e1) {
            logger.error("no found config file : {}", zipPathName);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("load config complete");
    }

    /**
     * 获得一个具体配置表类中的静态成员变量urlKey中的资源标识符和类的绑定映射表 <br>
     * 如果无法找到资源中没有则会输出错误信息，但不会停止会继续运行
     * 
     * @param packName
     * @return
     * @author wcy 2017年8月5日
     */
    private static Map<String, Class<?>> getClassesMap(String packName) {
        // 获得包下所有的类
        List<Class<?>> classes = PackageUtil.getClasses(packName);
        Map<String, Class<?>> classesMap = new HashMap<>();

        for (Class<?> clazz : classes) {
            try {
                classesMap.put((String) clazz.getDeclaredField("urlKey").get(null), clazz);
            } catch (Exception e) {
                logger.info("{} no urlkey", clazz.getSimpleName());
            }
        }

        return classesMap;
    }

}

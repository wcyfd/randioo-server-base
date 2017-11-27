package com.randioo.randioo_server_base.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectUtils {
    private static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

    /**
     * 使用protostuff进行对象的序列化
     * 
     * @param obj
     * @param clazz
     * @return
     * @author wcy 2017年10月9日
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static byte[] serializeProtostuff(Object obj, Class clazz) {
        if (obj == null) {
            return null;
        }
        Schema schema = RuntimeSchema.getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] protostuff = null;
        protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        return protostuff;
    }

    /**
     * 使用protostuff进行对象的序列化，Class使用getClass()方法
     * 
     * @param obj
     * @param clazz
     * @return
     * @author wcy 2017年10月9日
     */
    public static byte[] serializeProtostuff(Object obj) {
        return serializeProtostuff(obj, obj.getClass());
    }

    /**
     * protostuff反序列化
     * 
     * @param bytes
     * @param clazz
     * @return
     * @author wcy 2017年10月9日
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T deserializeProtostuff(byte[] bytes, Class<?> clazz) {
        if (bytes == null) {
            return null;
        }
        Schema schema = RuntimeSchema.getSchema(clazz);

        try {
            Object obj = clazz.newInstance();
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
            return (T) obj;
        } catch (InstantiationException e) {
            logger.error("{}", e);
        } catch (IllegalAccessException e) {
            logger.error("{}", e);
        }

        return null;

    }

    /**
     * 序列化对象
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            if (object != null) {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象
     * 
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            if (bytes != null && bytes.length > 0) {
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.randioo.randioo_server_base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于协议与SupportAction绑定<br>
 * 此注解用于协议类,如果没有类而是字符串,请使用PTStringAnnotion
 * 
 * @author wcy 2017年8月5日
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PTAnnotation {
    public Class<?> value();
}

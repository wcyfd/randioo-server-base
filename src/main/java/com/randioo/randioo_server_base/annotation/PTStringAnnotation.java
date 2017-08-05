package com.randioo.randioo_server_base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于协议与SupportAction绑定<br>
 * 请优先考虑使用PTAnnotion,因为字符串表示比较难看
 * 
 * @author wcy 2017年8月5日
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PTStringAnnotation {
    public String value();
}

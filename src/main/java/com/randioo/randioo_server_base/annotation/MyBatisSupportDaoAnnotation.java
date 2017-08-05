package com.randioo.randioo_server_base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 游戏后台使用的支持库，mybatis配合使用，一般请不要使用
 * 
 * @author wcy 2017年8月5日
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MyBatisSupportDaoAnnotation {
}

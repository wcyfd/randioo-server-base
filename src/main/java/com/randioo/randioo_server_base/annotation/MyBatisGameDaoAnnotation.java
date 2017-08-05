package com.randioo.randioo_server_base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 游戏使用的数据标记，需要和mybatis配合使用
 * 
 * @author wcy 2017年8月5日
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MyBatisGameDaoAnnotation {
}

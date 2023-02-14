package com.ding.niuke.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//注解应用于方法上
@Retention(RetentionPolicy.RUNTIME)//注解在程序运行时生效
public @interface LoginRequired {
}

package com.ding.niuke.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AlphaAspect {
    //execution(返回值类型 包 文件名 方法名 参数 )
    @Pointcut("execution(* com.ding.niuke.service.*.*(..))")
    public void pointcut(){

    }

}

package io.spridra.rpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-23 17:27
 * @Describe: 服务提供者核心注解类
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
// @Component
@Autowired
public @interface RpcService {
    /**
     * 接口的 Class
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 接口的 ClassName
     */
    String interfaceClassName() default "";

    /**
     * 版本号
     */
    String version() default "1.0.0";

    /**
     * 服务分组，默认为空
     */
    String group() default "";
}

package cn.hengyumo.humor.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PassAuth
 * 跳过token验证的注解
 *
 * @author hengyumo
 * @since 2019/4/25
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassAuth {
    boolean required() default true;
}

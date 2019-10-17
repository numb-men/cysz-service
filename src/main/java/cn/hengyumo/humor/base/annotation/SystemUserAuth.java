package cn.hengyumo.humor.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SystemUserAuth
 * 需要验证用户登录的token注解
 *
 * @author hengyumo
 * @since 2019/4/25
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemUserAuth {
    boolean required() default true;
}

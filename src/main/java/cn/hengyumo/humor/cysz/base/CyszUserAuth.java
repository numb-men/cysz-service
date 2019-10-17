package cn.hengyumo.humor.cysz.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SystemUserAuth
 * 需要验证串意十足用户登录的token注解
 *
 * @author hengyumo
 * @since 2019/10/12
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CyszUserAuth {
    boolean required() default true;
}

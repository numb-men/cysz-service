package cn.hengyumo.humor.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ParamNotBlank
 * 对指定的参数进行判空
 * 1、不能为null
 * 2、不能为""
 * 3、参数必须存在
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/8/7
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamNotBlank {
    String[] values() default {};
}

package cn.hengyumo.humor.base.annotation;

import java.lang.annotation.*;

/**
 * SystemAdminAuth
 * 注解代表访问控制，需要有管理员权限/role=1
 *
 * @author hengyumo
 * @since 2019/4/28
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SystemAdminAuth {
    boolean required() default true;
}

package cn.hengyumo.humor.system.annotation;



import cn.hengyumo.humor.system.enums.SystemResourceType;

import java.lang.annotation.*;

/**
 * 资源控制
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SystemResource {
    String parentCode() default "";
    String name() default "";
    String icon() default "";
    String comment() default "";
    SystemResourceType type() default SystemResourceType.BUTTON;
    String code() default "";
}

package cn.hengyumo.humor.base.system.config;



import cn.hengyumo.humor.base.system.resource.SystemResourceType;

import java.lang.annotation.*;

/**
 * SystemResourceClass
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SystemResourceClass {
    String resourceName();
    String parentResource();
    String icon() default "";
    String comment();
    SystemResourceType type() default SystemResourceType.MENU;
    String code() default "";
}

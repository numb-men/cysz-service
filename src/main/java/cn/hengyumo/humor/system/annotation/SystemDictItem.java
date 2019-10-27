package cn.hengyumo.humor.system.annotation;

import java.lang.annotation.*;

/**
 * SystemDictItem
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SystemDictItem {
    String value();
    String icon() default "";
}

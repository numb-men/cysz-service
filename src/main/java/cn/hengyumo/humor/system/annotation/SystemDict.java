package cn.hengyumo.humor.system.annotation;

import java.lang.annotation.*;

/**
 * SystemDict
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SystemDict {
    String name();
    String comment() default "";
    String dictIcon() default "";
    boolean canBlank() default false;
    boolean canError() default false;
    SystemDictItem[] items() default {};
}

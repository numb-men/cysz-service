package cn.hengyumo.humor.base.system.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SystemResourceType
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Getter
@AllArgsConstructor
public enum SystemResourceType {

    BUTTON("button"),
    MENU("menu"),
    REQUEST("request"),
    DEFAULT("default")
    ;
    private String type;
}

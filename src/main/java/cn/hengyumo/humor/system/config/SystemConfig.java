package cn.hengyumo.humor.system.config;


import cn.hengyumo.humor.system.enums.SystemResourceType;

/**
 * SystemConfig
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
public interface SystemConfig {

    Long DEFAULT_RESOURCE_PARENT_ID = 0L;
    String DEFAULT_RESOURCE_PARENT_CODE = "o";
    String DEFAULT_RESOURCE_ICON = "default-icon";
    String DEFAULT_RESOURCE_NAME = "default";
    String DEFAULT_RESOURCE_COMMENT = "默认资源";
    SystemResourceType DEFAULT_RESOURCE_TYPE = SystemResourceType.DEFAULT;
    String DEFAULT_RESOURCE_CODE_PREFIX = "default";
    Long DEFAULT_RESOURCE_ORDER_BY = 0L;

    String DEFAULT_DICT_ICON = "default-icon";
    String DEFAULT_DICT_ITEM_ICON = "default-icon";
}

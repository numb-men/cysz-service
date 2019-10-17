package cn.hengyumo.humor.base.system.exception.enums;


import cn.hengyumo.humor.base.result.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SystemResultEnum
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/11
 */
@Getter
@AllArgsConstructor
public enum  SystemResultEnum implements ResultEnum {
    USER_NOT_FOUND(701, "用户不存在"),
    ROLE_NOT_FOUND(702, "角色不存在"),
    RESOURCE_NOT_FOUND(703, "资源不存在"),

    UN_MAPPING_DICT(704, "无法映射的字典值"),

    FILE_IS_EMPTY(705, "文件是空的"),
    MKDIR_FAIL(706, "创建文件夹失败"),
    FILE_SAVE_FAIL(707, "文件保存失败"),
    CAN_NOT_FIND_RESOURCE_PATH(708, "无法找到资源路径"),
    DICT_NOT_FOUND(709, "字典不存在"),
    DICT_ITEM_EXIST(710, "字典项已存在")
    ;

    private Integer code;
    private String message;
}

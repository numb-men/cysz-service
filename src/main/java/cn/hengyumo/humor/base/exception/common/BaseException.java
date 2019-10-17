package cn.hengyumo.humor.base.exception.common;

import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.base.result.ResultEnum;
import lombok.Getter;

/**
 * BaseException
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Getter
public class BaseException extends RuntimeException {
    private ResultEnum resultEnum;
    private String message;
    private Integer code;

    public BaseException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public BaseException(String message) {
        this.code = BaseResultEnum.FAIL.getCode();
        this.message = message;
    }
}

package cn.hengyumo.humor.base.result;


import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 一个封装好的响应Result
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/4/17
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Getter
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;


    public Result(ResultEnum resultEnum) {
        this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
    }

    public Result(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public Result(ResultEnum resultEnum, T data) {
        this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
        this.data = data;
    }

    public static Result success() {
        return new Result(BaseResultEnum.SUCCESS);
    }

    public static <T> Result success(T data){
        return new Result<>(BaseResultEnum.SUCCESS, data);
    }

    public static Result error() {
        return new Result(BaseResultEnum.FAIL);
    }

    public static <T> Result error(ResultEnum resultEnum){
        return new Result<T>(resultEnum);
    }

    public static <T> Result error(Integer code, String message){
        return new Result<T>(code, message);
    }

    public static <T> Result check(boolean ok) {
        return ok ? success() : error();
    }
}

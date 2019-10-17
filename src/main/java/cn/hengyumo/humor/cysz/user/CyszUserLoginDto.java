package cn.hengyumo.humor.cysz.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * CyszUserLoginDto
 * TODO
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Data
public class CyszUserLoginDto {

    @Length(min = 11, max = 11, message = "手机号码不符合格式")
    @NotBlank
    private String mobile;

    @Length(min = 8, max = 255, message = "密码应该在8位以上")
    @NotBlank
    private String password;
}

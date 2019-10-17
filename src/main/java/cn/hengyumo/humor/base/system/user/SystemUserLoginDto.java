package cn.hengyumo.humor.base.system.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * SystemUserLoginDto
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Data
public class SystemUserLoginDto {

    @Length(min = 5, max = 25, message = "用户名应该在5-25之间")
    @NotBlank
    private String name;

    @Length(min = 8, max = 255, message = "密码应该在8位以上")
    @NotBlank
    private String password;

    @NotBlank
    private String captchaToken;

    @NotBlank
    private String code;
}

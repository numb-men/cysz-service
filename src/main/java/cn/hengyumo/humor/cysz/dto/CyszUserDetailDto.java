package cn.hengyumo.humor.cysz.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * CyszUserDetailDto
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Data
public class CyszUserDetailDto {

    private Long id;

    @Length(max = 10, message = "用户名应该在10个字符以内")
    @NotBlank
    private String username;

    @Length(min = 11, max = 11, message = "手机号码不符合格式")
    @NotBlank
    private String mobile;

    @Min(value = 0)
    private Double balance;
}

package cn.hengyumo.humor.cysz.entity;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.system.annotation.UseSystemDict;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * CyszUser
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */

@Data
@Cacheable
@UseSystemDict
@Entity(name = "tb_cysz_user")
@EqualsAndHashCode(callSuper = true)
public class CyszUser extends BaseEntity<Long> {

    @Length(max = 10, message = "用户名应该在10个字符以内")
    @NotBlank
    private String username;

    @Length(min = 11, max = 11, message = "手机号码不符合格式")
    @NotBlank
    private String mobile;

    @JsonIgnore
    @Length(min = 8, max = 255, message = "密码应该在8位以上")
    @NotBlank
    private String password;

    @Min(value = 0)
    private Double balance;
}

package cn.hengyumo.humor.system.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * SystemRoleDto
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/27
 */
@Data
public class SystemRoleDto {
    @NotBlank
    @Length(min = 2, max = 20, message = "角色名应该在2-10之间")
    private String name;

    @NotBlank
    private String icon;

    private List<Long> resourcesId;
}

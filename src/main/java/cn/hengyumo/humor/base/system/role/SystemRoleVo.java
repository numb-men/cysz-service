package cn.hengyumo.humor.base.system.role;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SystemRoleVo
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemRoleVo extends SystemRoleEntity {

    private List<Long> resourcesId;
}

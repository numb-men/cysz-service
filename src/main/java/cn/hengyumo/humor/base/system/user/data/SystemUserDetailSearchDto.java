package cn.hengyumo.humor.base.system.user.data;

import cn.hengyumo.humor.base.system.config.SystemDict;
import cn.hengyumo.humor.base.system.config.UseSystemDict;
import cn.hengyumo.humor.base.system.dict.SystemDictDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * SystemUserDetailSearchDto
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/30
 */
@Data
public class SystemUserDetailSearchDto {

    private String realName;

    private String mobile;

    private String email;
}

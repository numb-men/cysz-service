package cn.hengyumo.humor.base.system.user.data;

import cn.hengyumo.humor.base.system.config.SystemDict;
import cn.hengyumo.humor.base.system.config.UseSystemDict;
import cn.hengyumo.humor.base.system.dict.SystemDictDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

/**
 * SystemUserDetailDto
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/30
 */
@Data
@UseSystemDict
public class SystemUserDetailDto {

    private String name;

    private String realName;

    @SystemDict(name = "sex")
    @JsonDeserialize(using = SystemDictDeserializer.class)
    private Integer sex;

    private Integer age;

    private String mobile;

    private String email;

    private String userDescribe;

    private List<Long> roles;
}

package cn.hengyumo.humor.system.dto;

import cn.hengyumo.humor.system.annotation.SystemDict;
import cn.hengyumo.humor.system.annotation.UseSystemDict;
import cn.hengyumo.humor.system.utils.SystemDictDeserializer;
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

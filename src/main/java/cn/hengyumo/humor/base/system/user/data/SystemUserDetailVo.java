package cn.hengyumo.humor.base.system.user.data;

import cn.hengyumo.humor.base.system.config.SystemDict;
import cn.hengyumo.humor.base.system.config.SystemDictItem;
import cn.hengyumo.humor.base.system.config.UseSystemDict;
import cn.hengyumo.humor.base.system.dict.SystemDictDeserializer;
import cn.hengyumo.humor.base.system.dict.SystemDictSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

/**
 * SystemUserDetailVo
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/29
 */
@Data
@UseSystemDict
public class SystemUserDetailVo {

    protected Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    private String name;

    private String realName;

    @SystemDict(name = "sex", comment = "性别", items = {
            @SystemDictItem(value = "男", icon = "man.png"),
            @SystemDictItem(value = "女", icon = "women.png")
    })
    @JsonDeserialize(using = SystemDictDeserializer.class)
    @JsonSerialize(using = SystemDictSerializer.class)
    private Integer sex;

    private Integer age;

    private String mobile;

    private String email;

    private String userDescribe;

    private Date lastLogin;
}

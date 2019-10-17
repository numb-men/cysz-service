package cn.hengyumo.humor.base.system.user.data;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.base.system.config.SystemDict;
import cn.hengyumo.humor.base.system.config.SystemDictItem;
import cn.hengyumo.humor.base.system.config.UseSystemDict;
import cn.hengyumo.humor.base.system.dict.SystemDictDeserializer;
import cn.hengyumo.humor.base.system.dict.SystemDictSerializer;
import cn.hengyumo.humor.base.system.user.SystemUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * SystemUserData
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Data
@UseSystemDict
@Entity(name = "tb_system_user_data")
@EqualsAndHashCode(callSuper = true)
public class SystemUserData extends BaseEntity<Long> {

    @Length(min = 5, max = 25, message = "用户名应该在5-25之间")
    @NotBlank
    private String realName;

    @SystemDict(name = "sex", comment = "性别", items = {
            @SystemDictItem(value = "男", icon = "man.png"),
            @SystemDictItem(value = "女", icon = "women.png")
    })
    @JsonDeserialize(using = SystemDictDeserializer.class)
    @JsonSerialize(using = SystemDictSerializer.class)
    private Integer sex;

    @Range(min = 1, max = 120, message = "年龄格式错误")
    private Integer age;

    @NotBlank
    @Length(min = 11, max = 11, message = "手机号码格式错误")
    private String mobile;

    private String email;

    @Length(max = 150, message = "描述长度不能超过150")
    private String userDescribe;

    private Date lastLogin;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userData")
    @JsonIgnore
    private SystemUser systemUser;
}

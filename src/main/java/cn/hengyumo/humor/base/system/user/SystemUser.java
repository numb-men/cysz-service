package cn.hengyumo.humor.base.system.user;


import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.base.system.role.SystemRoleEntity;
import cn.hengyumo.humor.base.system.user.data.SystemUserData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * SystemUser
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Data
@Cacheable
@Entity(name = "tb_system_user")
@EqualsAndHashCode(callSuper = true)
@EntityListeners(SystemUserListener.class)
public class SystemUser extends BaseEntity<Long> {

    @Length(min = 5, max = 25, message = "用户名应该在5-25之间")
    @NotBlank
    private String name;

    @Length(min = 8, message = "密码应该在8位以上")
    @NotBlank
    @JsonIgnore
    private String password;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "system_user_data_id", referencedColumnName = "id")
    private SystemUserData userData;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_system_user_role", joinColumns = @JoinColumn(name = "system_user_id"),
            inverseJoinColumns = @JoinColumn(name = "system_role_id"))
    private Set<SystemRoleEntity> roles;

    @Override
    public String toString() {
        return "SystemUser{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

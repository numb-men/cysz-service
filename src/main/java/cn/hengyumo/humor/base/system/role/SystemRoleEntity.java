package cn.hengyumo.humor.base.system.role;


import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.base.system.resource.SystemResourceEntity;
import cn.hengyumo.humor.base.system.user.SystemUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * SystemRoleEntity
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/11
 */
@Getter
@Setter
@Cacheable
@Entity(name = "tb_system_role")
public class SystemRoleEntity extends BaseEntity<Long> {

    @NotBlank
    @Length(min = 2, max = 20, message = "角色名应该在2-10之间")
    private String name;

    @NotBlank
    private String icon;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<SystemUser> users;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_system_role_resource", joinColumns = @JoinColumn(name = "system_role_id"),
            inverseJoinColumns = @JoinColumn(name = "system_resource_id"))
    private Set<SystemResourceEntity> resources;

    @Override
    public String toString() {
        return "SystemRoleEntity{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }

    // 覆写equals、hashcode、防止因为懒加载导致的一系列异常
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SystemRoleEntity)) {
            return false;
        } else {
            SystemRoleEntity other = (SystemRoleEntity)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label61: {
                    Object this$name = this.getName();
                    Object other$name = other.getName();
                    if (this$name == null) {
                        if (other$name == null) {
                            break label61;
                        }
                    } else if (this$name.equals(other$name)) {
                        break label61;
                    }

                    return false;
                }

                label54: {
                    Object this$icon = this.getIcon();
                    Object other$icon = other.getIcon();
                    if (this$icon == null) {
                        if (other$icon == null) {
                            break label54;
                        }
                    } else if (this$icon.equals(other$icon)) {
                        break label54;
                    }

                    return false;
                }

                // Object this$users = this.getUsers();
                // Object other$users = other.getUsers();
                // if (this$users == null) {
                //     if (other$users != null) {
                //         return false;
                //     }
                // } else if (!this$users.equals(other$users)) {
                //     return false;
                // }

                // Object this$resources = this.getResources();
                // Object other$resources = other.getResources();
                // if (this$resources == null) {
                //     if (other$resources != null) {
                //         return false;
                //     }
                // } else if (!this$resources.equals(other$resources)) {
                //     return false;
                // }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SystemRoleEntity;
    }

    public int hashCode() {
        int result = super.hashCode();
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $icon = this.getIcon();
        result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
        // Object $users = this.getUsers();
        // result = result * 59 + ($users == null ? 43 : $users.hashCode());
        // Object $resources = this.getResources();
        // result = result * 59 + ($resources == null ? 43 : $resources.hashCode());
        return result;
    }
}

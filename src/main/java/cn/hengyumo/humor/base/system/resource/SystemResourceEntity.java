package cn.hengyumo.humor.base.system.resource;


import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.base.system.role.SystemRoleEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * SystemResourceEntity
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Data
@Cacheable
@EqualsAndHashCode(callSuper = true)
@Entity(name = "tb_system_resource")
public class SystemResourceEntity extends BaseEntity<Long> {

    @NotNull
    private Long parentId;

    @NotBlank
    private String parentCode;

    @NotBlank
    private String name;

    @NotBlank
    private String icon;

    @NotBlank
    private String comment;

    @NotBlank
    private String type;

    @NotBlank
    private String code;

    @NotNull
    private Long orderBy;

    private String method;

    private String url;

    @JsonIgnore
    @ManyToMany(mappedBy = "resources", fetch = FetchType.LAZY)
    private List<SystemRoleEntity> roles;

    @Override
    public String toString() {
        return "SystemResourceEntity{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", parentCode='" + parentCode + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", comment='" + comment + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", orderBy=" + orderBy +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

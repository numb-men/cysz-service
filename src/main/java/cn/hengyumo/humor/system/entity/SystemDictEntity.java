package cn.hengyumo.humor.system.entity;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * SystemDictEntity
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@Data
@Cacheable
@EqualsAndHashCode(callSuper = true)
@Entity(name = "tb_system_dict")
public class SystemDictEntity extends BaseEntity<Long> {

    private String name;
    private String comment;
    private String icon;
    private Integer orderBy;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_system_dict_dict_item", joinColumns = @JoinColumn(name = "system_dict_id"),
            inverseJoinColumns = @JoinColumn(name = "system_dict_item_id"))
    private List<SystemDictItemEntity> children;
}

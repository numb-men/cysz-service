package cn.hengyumo.humor.system.entity;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

/**
 * SystemDictItemEntity
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@Data
@Cacheable
@EqualsAndHashCode(callSuper = true)
@Entity(name = "tb_system_dict_item")
public class SystemDictItemEntity extends BaseEntity<Long> {

    private Integer valueIndex;
    private String value;
    private String icon;
    private Integer orderBy;
}

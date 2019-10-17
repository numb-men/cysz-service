package cn.hengyumo.humor.b2c.entity;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * CustomerUser
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */

@Data
@Entity(name = "tb_customer_user")
@EqualsAndHashCode(callSuper = true)
public class CustomerUser extends BaseEntity<Long> {


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "b2c_base_user_id", referencedColumnName = "id")
    private B2cBaseUser b2cBaseUser;
}

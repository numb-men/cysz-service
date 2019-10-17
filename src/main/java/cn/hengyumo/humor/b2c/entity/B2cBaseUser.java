package cn.hengyumo.humor.b2c.entity;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import cn.hengyumo.humor.base.system.config.SystemDict;
import cn.hengyumo.humor.base.system.config.UseSystemDict;
import cn.hengyumo.humor.wx.user.WxUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * B2cBaseUser
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */

@Data
@UseSystemDict
@Entity(name = "tb_b2c_base_user")
@EqualsAndHashCode(callSuper = true)
public class B2cBaseUser extends BaseEntity<Long> {

    private String phone;

    private String password;

    private String realName;

    private String age;

    private Integer awardPoints;

    private Integer creditPoints;

    private Integer balance;

    private Boolean todaySignIn;

    @SystemDict(name = "sex")
    private Integer sex;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wx_user_id", referencedColumnName = "id")
    private WxUser wxUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_user_id", referencedColumnName = "id")
    @JsonIgnore
    private CustomerUser customerUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_user_id", referencedColumnName = "id")
    @JsonIgnore
    private BusinessUser businessUser;
}

package cn.hengyumo.humor.cysz.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * CyszCurrentUser
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Data
public class CyszCurrentUser {

    private Long id;

    private String username;

    private String mobile;

    @JsonIgnore
    private String password;

    private Double balance;

    public CyszCurrentUser(){}

    public CyszCurrentUser(CyszUser cyszUser) {
        this.id = cyszUser.getId();
        this.username = cyszUser.getUsername();
        this.mobile = cyszUser.getMobile();
        this.password = cyszUser.getPassword();
        this.balance = cyszUser.getBalance();
    }
}

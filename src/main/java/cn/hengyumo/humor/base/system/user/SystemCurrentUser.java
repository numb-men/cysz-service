package cn.hengyumo.humor.base.system.user;

import lombok.Data;

/**
 * SystemCurrentUser
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/13
 */
@Data
public class SystemCurrentUser {

    private Long id;
    private Boolean isAdmin;
    private String name;
    private String password;

    public void setSystemCurrentUser(SystemUser systemUser) {
        this.id = systemUser.getId();
        this.isAdmin = false;
        this.name = systemUser.getName();
        this.password = systemUser.getPassword();
    }

    public void setSystemCurrentUser(SystemAdmin systemAdmin) {
        this.id = systemAdmin.getId();
        this.isAdmin = true;
        this.name = systemAdmin.getName();
        this.password = systemAdmin.getPassword();
    }

    @Override
    public String toString() {
        return "SystemCurrentUser{" +
                "id=" + id +
                ", isAdmin=" + isAdmin +
                ", name='" + name + '\'' +
                '}';
    }
}

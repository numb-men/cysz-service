package cn.hengyumo.humor.base.system.user;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SystemUserAdmin
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/12
 */

@Data
@Component
@EnableConfigurationProperties(SystemAdmin.class)
@ConfigurationProperties(prefix = "hengyumo.base.system.admin")
public class SystemAdmin {

    private final Long id = 0L;
    private String name;
    private String password;

    @Override
    public String toString() {
        return "SystemAdmin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

package cn.hengyumo.humor.base.system.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PrePersist;

/**
 * SystemUserListener
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Slf4j
@Component
@Transactional
public class SystemUserListener {
    @PrePersist
    public void prePersist(SystemUser systemUser) throws IllegalArgumentException {
        systemUser.setIsDeleted(false);
    }
}

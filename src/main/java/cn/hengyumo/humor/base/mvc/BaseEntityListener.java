package cn.hengyumo.humor.base.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

/**
 * BaseEntityListener
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Slf4j
@Component
@Transactional
public class BaseEntityListener {

    @PrePersist
    public void prePersist(BaseEntity baseEntity) throws IllegalArgumentException {
        baseEntity.setIsDeleted(false);
        log.debug("save之前的操作");
    }

    @PostPersist
    public void postPersist(BaseEntity baseEntity) throws IllegalArgumentException {
        log.debug("save之后的操作");
    }

    @PreUpdate
    public void preUpdate(BaseEntity baseEntity) throws IllegalArgumentException {
        log.debug("update之前的操作");
    }

    @PostUpdate
    public void postUpdate(BaseEntity baseEntity) throws IllegalArgumentException {
        log.debug("update之后的操作");
    }

    @PreRemove
    public void preRemove(BaseEntity baseEntity) {
        log.debug("del之前的操作");
    }

    @PostRemove
    public void postRemove(BaseEntity baseEntity) {
        log.debug("del之后的操作");
    }
}

package cn.hengyumo.humor.base.mvc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * BaseDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@NoRepositoryBean
public interface BaseDao<T extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, Serializable {

    @Transactional
    @Modifying
    @Query("update #{#entityName} t set t.isDeleted=true where t.id = ?1")
    void setDeletedTrue(ID id);
}

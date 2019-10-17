package cn.hengyumo.humor.base.mvc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * BaseEntity
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners({BaseEntityListener.class, AuditingEntityListener.class})
public class BaseEntity<ID extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected ID id;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    @CreatedBy
    @JsonIgnore
    private Long createBy;

    @LastModifiedBy
    @JsonIgnore
    private Long updateBy;

    // 伪删除，deleted为true表示删除了
    @JsonIgnore
    private Boolean isDeleted;
}

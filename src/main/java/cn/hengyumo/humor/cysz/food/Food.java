package cn.hengyumo.humor.cysz.food;

import cn.hengyumo.humor.base.mvc.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Food
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Data
@Cacheable
@EqualsAndHashCode(callSuper = true)
@Entity(name = "tb_cysz_food")
public class Food extends BaseEntity<Long> {

    @NotBlank
    private String title;

    @Min(0)
    @NotNull
    private Double price;

    @NotBlank
    private String detail;

    @NotBlank
    private String image;
}

package cn.hengyumo.humor.cysz.order.item;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * OrderItemDto
 * TODO
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Data
public class OrderItemDto {

    @NotNull
    private Long id;

    @NotNull
    @Min(0)
    private Integer num;
}

package cn.hengyumo.humor.cysz.vo;

import cn.hengyumo.humor.cysz.entity.Order;
import cn.hengyumo.humor.system.annotation.SystemDict;
import cn.hengyumo.humor.system.utils.SystemDictSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

/**
 * OrderSearchVo
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/21
 */
@Data
public class OrderSearchVo {

    private Long id;
    private String username;
    private String mobile;
    private Integer foodNum;
    private String content;
    private Double allPrice;
    private Date createDate;

    @SystemDict(name = "orderStatus")
    @JsonSerialize(using = SystemDictSerializer.class)
    private Integer status;

    private Order orderDetail;
}

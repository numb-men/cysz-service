package cn.hengyumo.humor.cysz.dto;

import cn.hengyumo.humor.system.annotation.SystemDict;
import cn.hengyumo.humor.system.utils.SystemDictDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * OrderSearchDto
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/21
 */
@Data
public class OrderSearchDto {

    private String username;
    private String mobile;

    @SystemDict(name = "orderStatus", canError = true)
    @JsonDeserialize(using = SystemDictDeserializer.class)
    private Integer status;
}

package cn.hengyumo.humor.utils.wx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * WxSessionVo
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxSessionVo implements Serializable {

    private String session_key;
    private String openid;
}

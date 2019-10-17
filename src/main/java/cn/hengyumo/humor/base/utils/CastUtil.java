package cn.hengyumo.humor.base.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * CastUtil
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/15
 */

public class CastUtil {

    public static <T> List<T> castList(Object obj, Class<T> clazz) throws ClassCastException {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}

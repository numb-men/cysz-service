package cn.hengyumo.humor.cysz.dao;

import cn.hengyumo.humor.base.mvc.BaseDao;
import cn.hengyumo.humor.cysz.entity.Food;
import org.springframework.stereotype.Repository;

/**
 * FoodDao
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Repository
public interface FoodDao extends BaseDao<Food, Long> {
}

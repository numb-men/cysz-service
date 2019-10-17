package cn.hengyumo.humor.cysz.stats;

import cn.hengyumo.humor.base.utils.date.Week;
import cn.hengyumo.humor.cysz.food.Food;
import cn.hengyumo.humor.cysz.order.Order;
import cn.hengyumo.humor.cysz.order.OrderService;
import cn.hengyumo.humor.cysz.order.OrderStatus;
import cn.hengyumo.humor.cysz.order.item.OrderItem;
import cn.hengyumo.humor.cysz.order.item.OrderItemService;
import cn.hengyumo.humor.cysz.stats.req.CyszDailyReqService;
import cn.hengyumo.humor.cysz.stats.req.CyszDailyReqVo;
import cn.hengyumo.humor.cysz.user.CyszUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * CyszDailyStats
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/13
 */
@Service
public class CyszStatsService {

    @Resource
    private CyszUserService cyszUserService;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private CyszDailyReqService cyszDailyReqService;

    private Date today;
    private Date tomorrow;

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        today = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        tomorrow = calendar.getTime();
    }

    public Long getTodayOrderAdd() {
        setDate();
        return orderService.countByCreateDateBetween(today, tomorrow);
    }

    public Long getAllOrderNum() {
        return orderService.count();
    }

    public Double getTodayEarnings() {
        Double sumEarnings = 0D;
        setDate();
        List<Order> orderList = orderService.getAllByCreateDateBetween(today, tomorrow);
        for (Order order : orderList) {
            if (order.getStatus() == OrderStatus.HAS_ARRIVED.getCode()) {
                sumEarnings += order.getAllPrice();
            }
        }
        return sumEarnings;
    }

    public Double getAllEarnings() {
        Double sumEarnings = 0D;
        setDate();
        List<Order> orderList = orderService.findAll();
        for (Order order : orderList) {
            if (order.getStatus() == OrderStatus.HAS_ARRIVED.getCode()) {
                sumEarnings += order.getAllPrice();
            }
        }
        return sumEarnings;
    }

    public Long getTodayUserAdd() {
        setDate();
        return cyszUserService.countByCreateDateBetween(today, tomorrow);
    }

    public Long getAllUserNum() {
        return cyszUserService.count();
    }

    public CyszStatsVo view() {
        CyszStatsVo cyszStatsVo = new CyszStatsVo();
        cyszStatsVo.setClickAddNumToday(cyszDailyReqService.getTodayReqNum());
        cyszStatsVo.setEarningsAll(getAllEarnings());
        cyszStatsVo.setEarningsToday(getTodayEarnings());
        cyszStatsVo.setOrderAddNumToday(getTodayOrderAdd());
        cyszStatsVo.setOrderNumAll(getAllOrderNum());
        cyszStatsVo.setUserAddNumToday(getTodayUserAdd());
        return cyszStatsVo;
    }

    public List<CyszStatsOrderItemVo> orderPie() {
        List<CyszStatsOrderItemVo> orderItemVos = new ArrayList<>();
        List<OrderItem> orderItems = orderItemService.findAll();
        Map<String, Long> foodSellMap = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            Food food = orderItem.getFood();
            if (! foodSellMap.containsKey(food.getTitle())) {
                foodSellMap.put(food.getTitle(), Long.valueOf(orderItem.getNum()));
            }
            else {
                Long newSellNum = foodSellMap.get(food.getTitle()) + orderItem.getNum();
                foodSellMap.put(food.getTitle(), newSellNum);
            }
        }
        // 一种高效的取前五个食物名的方法
        String topFoodName = "";
        long topSellNum = 0L;
        for (int i = 0; i < 5 && i < foodSellMap.size(); i++) {
            for (String name : foodSellMap.keySet()) {
                if (foodSellMap.get(name) > topSellNum) {
                    topFoodName = name;
                    topSellNum = foodSellMap.get(name);
                }
            }
            CyszStatsOrderItemVo orderItemVo = new CyszStatsOrderItemVo();
            orderItemVo.setFoodName(topFoodName);
            orderItemVo.setSellNum(topSellNum);
            orderItemVos.add(orderItemVo);
            foodSellMap.put(topFoodName, 0L);
            topSellNum = 0L;
            topFoodName = "";
        }
        // 获取其余食物售卖总数
        if (foodSellMap.size() > 5) {
            long sum = 0L;
            for (Long sellNum : foodSellMap.values()) {
                sum += sellNum;
            }
            CyszStatsOrderItemVo orderItemVo = new CyszStatsOrderItemVo();
            orderItemVo.setFoodName("其它");
            orderItemVo.setSellNum(sum);
            orderItemVos.add(orderItemVo);
        }

        return orderItemVos;
    }

    public List<CyszDailyReqVo> getWeekReqBar() {
        return cyszDailyReqService.getWeekReqBar();
    }

    public List<CyszStatsFoodWeekVo> getFoodWeekStats() {
        List<CyszStatsFoodWeekVo> foodWeekVos = new ArrayList<>();
        List<CyszStatsOrderItemVo> orderItemVos = orderPie();
        Calendar calendar = Calendar.getInstance();
        // 初始化返回数据
        for (int i = 0; i < 5 && i < orderItemVos.size(); i++) {
            // 最多取5个
            CyszStatsOrderItemVo orderItemVo = orderItemVos.get(i);
            CyszStatsFoodWeekVo foodWeekVo = new CyszStatsFoodWeekVo();
            Long[] weekSellNum = new Long[] {0L, 0L, 0L, 0L, 0L, 0L, 0L};
            foodWeekVo.setFoodWeekSellNum(weekSellNum);
            foodWeekVo.setFoodName(orderItemVo.getFoodName());
            foodWeekVos.add(foodWeekVo);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (dayOfWeek == -1) {
            // 周日
            dayOfWeek = 6;
        }
        // 获取这周1
        calendar.add(Calendar.DATE, - dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 按周进行遍历
        for (Week week : Week.values()) {
            Date preDate = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date afterDate = calendar.getTime();
            List<OrderItem> orderItems = orderItemService.getAllCreateDateBetween(preDate, afterDate);
            for (OrderItem orderItem : orderItems) {
                String orderFoodName = orderItem.getFood().getTitle();
                for (CyszStatsFoodWeekVo foodWeekVo : foodWeekVos) {
                    if (foodWeekVo.getFoodName().equals(orderFoodName)) {
                        Long[] weekSellNum = foodWeekVo.getFoodWeekSellNum();
                        weekSellNum[week.getNumber()-1] += orderItem.getNum();
                        foodWeekVo.setFoodWeekSellNum(weekSellNum);
                        break;
                    }
                }
            }
        }

        return foodWeekVos;
    }
}

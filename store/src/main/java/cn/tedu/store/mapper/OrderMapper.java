package cn.tedu.store.mapper;

import cn.tedu.store.entity.Order;
import cn.tedu.store.entity.OrderItem;

/**
 * 处理订单和订单商品的持久层接口
 */
public interface OrderMapper {

	/**
	 * 插入订单数据
	 * @param order 订单数据
	 * @return 受影响行数
	 */
	Integer insertOrder(Order order);

	/**
	 * 插入订单商品数据
	 * @param orderItem 订单商品数据
	 * @return 受影响行数
	 */
	Integer insertOrderItem(OrderItem orderItem);
	
}

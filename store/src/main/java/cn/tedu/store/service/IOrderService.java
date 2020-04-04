package cn.tedu.store.service;

import cn.tedu.store.entity.Order;
import cn.tedu.store.service.ex.InsertException;

/**
 * 处理订单和订单商品的业务层接口
 */
public interface IOrderService {

	/**
	 * 创建订单
	 * @param aid 用户选择的收货地址数据的id
	 * @param cids 用户将购买的购物车数据的id
	 * @param uid 当前登录的用户的id
	 * @param username 当前登录的用户名
	 * @return 成功创建的订单数据
	 * @throws InsertException 插入数据异常
	 */
	Order create(Integer aid, Integer[] cids, Integer uid, String username) throws InsertException;
}

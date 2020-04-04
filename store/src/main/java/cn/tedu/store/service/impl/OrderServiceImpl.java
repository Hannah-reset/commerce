package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.store.entity.Address;
import cn.tedu.store.entity.Order;
import cn.tedu.store.entity.OrderItem;
import cn.tedu.store.mapper.OrderMapper;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.IOrderService;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.vo.CartVO;

@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired 
	private OrderMapper orderMapper;
	@Autowired 
	private IAddressService addressService;
	@Autowired 
	private ICartService cartService;

	@Override
	@Transactional
	public Order create(Integer aid, 
		Integer[] cids, Integer uid, String username) throws InsertException {
		Date now = new Date();

		List<CartVO> carts = cartService.getByCids(cids, uid);
		Long totalPrice = 0L;
		for (CartVO cartVO : carts) {
			totalPrice += cartVO.getPrice() * cartVO.getNum();
		}

		Address address = addressService.getByAid(aid);

		Order order = new Order();
		order.setUid(uid);
		order.setRecvName(address.getName());
		order.setRecvPhone(address.getPhone());
		order.setRecvAddress(address.getProvinceName() + address.getCityName() + address.getAreaName() + address.getAddress());
		order.setTotalPrice(totalPrice);
		order.setState(0);
		order.setOrderTime(now);
		order.setPayTime(null);
		order.setCreatedUser(username);
		order.setCreatedTime(now);
		order.setModifiedUser(username);
		order.setModifiedTime(now);
		insertOrder(order);

		for (CartVO cartVO : carts) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOid(order.getOid());
			orderItem.setGid(cartVO.getGid());
			orderItem.setPrice(cartVO.getPrice());
			orderItem.setTitle(cartVO.getTitle());
			orderItem.setImage(cartVO.getImage());
			orderItem.setNum(cartVO.getNum());
			orderItem.setCreatedUser(username);
			orderItem.setCreatedTime(now);
			orderItem.setModifiedUser(username);
			orderItem.setModifiedTime(now);
			insertOrderItem(orderItem);
		}
		
		return order;
	}

	/**
	 * 插入订单数据
	 * @param order 订单数据
	 * @throws InsertException 插入数据异常
	 */
	private void insertOrder(Order order) throws InsertException {
		Integer rows = orderMapper.insertOrder(order);
		if (rows != 1) {
			throw new InsertException(
				"创建订单失败！插入订单数据时出现未知错误！");
		}
	}

	/**
	 * 插入订单商品数据
	 * @param orderItem 订单商品数据
	 * @throws InsertException 插入数据异常
	 */
	private void insertOrderItem(OrderItem orderItem) throws InsertException {
		Integer rows = orderMapper.insertOrderItem(orderItem);
		if (rows != 1) {
			throw new InsertException(
				"创建订单失败！插入订单商品数据时出现未知错误！");
		}
	}
}
package cn.tedu.store.service;

import java.util.List;

import cn.tedu.store.entity.Cart;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.CartNotFoundException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.vo.CartVO;

/**
 * 购物车数据的业务层接口
 */
public interface ICartService {

	/**
	 * 将商品添加到购物车
	 * @param cart 购物车数据
	 * @param uid 当前登录的用户的id
	 * @param username 当前登录的用户名
	 * @throws InsertException 插入数据异常
	 * @throws UpdateException 更新数据异常
	 */
	void addToCart(Cart cart, Integer uid, String username) throws InsertException, UpdateException;

	/**
	 * 将购物车中的商品的数量增加1
	 * @param cid 购物车数据id
	 * @param uid 当前登录的用户的id
	 * @param username 当前登录的用户名
	 * @return 增加成功后的商品的数量
	 * @throws CartNotFoundException 购物车数据不存在
	 * @throws AccessDeniedException 拒绝访问，可能因为尝试访问他人的数据
	 * @throws UpdateException 更新数据异常 
	 */
	Integer add(Integer cid, 
		Integer uid, String username) 
			throws CartNotFoundException, 
				AccessDeniedException, 
				UpdateException;
	
	/**
	 * 获取某用户的购物车数据列表
	 * @param uid 用户的id
	 * @return 该用户的购物车数据列表
	 */
	List<CartVO> getByUid(Integer uid);
	
	/**
	 * 根据数据id获取购物车数据列表
	 * @param cids 数据id
	 * @param uid 当前登录的用户的id
	 * @return 匹配的购物车数据列表
	 */
	List<CartVO> getByCids(Integer[] cids, Integer uid);
	
}

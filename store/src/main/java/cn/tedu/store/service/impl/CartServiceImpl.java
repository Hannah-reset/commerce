package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.store.entity.Cart;
import cn.tedu.store.mapper.CartMapper;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.CartNotFoundException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.vo.CartVO;

/**
 * 购物车数据的业务层实现类
 */
@Service
public class CartServiceImpl implements ICartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public void addToCart(Cart cart, Integer uid, String username) throws InsertException, UpdateException {
		Date now = new Date();
		
		Cart result = findByUidAndGid(uid, cart.getGid());
		if (result == null) {
			cart.setUid(uid);
			cart.setCreatedUser(username);
			cart.setModifiedUser(username);
			cart.setCreatedTime(now);
			cart.setModifiedTime(now);
			insert(cart);
		} else {
			Integer cid = result.getCid();
			Integer oldNum = result.getNum();
			Integer newNum = oldNum + cart.getNum();
			updateNum(cid, newNum, username, now);
		}
	}

	@Override
	public Integer add(Integer cid, Integer uid, String username)
			throws CartNotFoundException, AccessDeniedException, UpdateException {
		Cart result = findByCid(cid);
		if (result == null) {
			throw new CartNotFoundException(
				"增加数量失败！尝试访问的购物车数据不存在！");
		}

		if (result.getUid() != uid) {
			throw new AccessDeniedException(
				"增加数量失败！非法访问！");
		}

		Integer newNum = result.getNum() + 1;
		updateNum(cid, newNum, username, new Date());
		return newNum;
	}

	@Override
	public List<CartVO> getByUid(Integer uid) {
		return findByUid(uid);
	}

	@Override
	public List<CartVO> getByCids(Integer[] cids, Integer uid) {
		List<CartVO> results = findByCids(cids);
		Iterator<CartVO> it = results.iterator();
		while (it.hasNext()) {
			if (it.next().getUid() != uid) {
				it.remove();
			}
		}
		return results;
	}

	/**
	 * 插入购物车数据
	 * @param cart 购物车数据
	 * @throws InsertException 插入数据异常
	 */
	private void insert(Cart cart) throws InsertException {
		Integer rows = cartMapper.insert(cart);
		if (rows != 1) {
			throw new InsertException(
				"将商品添加到购物车失败！插入数据时出现未知错误！");
		}
	}

	/**
	 * 修改购物车中商品的数量
	 * @param cid 购物车数据的id
	 * @param num 新的商品数量
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 * @throws UpdateException 更新数据异常
	 */
	private void updateNum(Integer cid, Integer num, 
	    String modifiedUser, Date modifiedTime)
			throws UpdateException {
		Integer rows = cartMapper.updateNum(cid, num, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new UpdateException(
				"更新商品数量失败！更新数据时出现未知错误！");
		}
	}

	/**
	 * 根据用户id和商品id查询购物车数据
	 * @param uid 用户id
	 * @param gid 商品id
	 * @return 匹配的购物车数据，如果没有匹配的数据，则返回null
	 */
	private Cart findByUidAndGid(Integer uid, Long gid) {
		return cartMapper.findByUidAndGid(uid, gid);
	}
	
	/**
	 * 根据购物车数据id查询购物车数据详情
	 * @param cid 购物车数据id
	 * @return 匹配的购物车数据，如果没有匹配的数据，则返回null
	 */
	private Cart findByCid(Integer cid) {
		return cartMapper.findByCid(cid);
	}
	
	/**
	 * 获取某用户的购物车数据列表
	 * @param uid 用户的id
	 * @return 该用户的购物车数据列表
	 */
	private List<CartVO> findByUid(Integer uid) {
		return cartMapper.findByUid(uid);
	}

	/**
	 * 根据数据id获取购物车数据列表
	 * @param cids 数据id
	 * @return 匹配的购物车数据列表
	 */
	private List<CartVO> findByCids(Integer[] cids) {
		return cartMapper.findByCids(cids);
	}

	
}




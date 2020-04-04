package cn.tedu.store.service;

import java.util.List;

import cn.tedu.store.entity.Address;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.AddressCountLimitException;
import cn.tedu.store.service.ex.AddressNotFoundException;
import cn.tedu.store.service.ex.DeleteException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.UpdateException;

/**
 * 处理收货地址数据的业务层接口
 */
public interface IAddressService {

	/**
	 * 收货地址数量上限
	 */
	int ADDRESS_MAX_COUNT = 20;
	
	/**
	 * 增加新的收货地址
	 * @param address 收货地址数据
	 * @param uid 用户id
	 * @param username 用户名
	 * @throws AddressCountLimitException 收货地址数量达到上限
	 * @throws InsertException 插入数据异常
	 */
	void addnew(Address address, 
		Integer uid, String username) 
			throws AddressCountLimitException, 
				InsertException;
	
	/**
	 * 根据用户id查询该用户的收货地址列表
	 * @param uid 用户id
	 * @return 该用户的收货地址列表
	 */
	List<Address> getByUid(Integer uid);
	
	/**
	 * 根据收货地址id查询收货地址详情
	 * @param aid 收货地址id
	 * @return 匹配的收货地址，如果没有匹配的数据，则返回null
	 */
	Address getByAid(Integer aid);
	
	/**
	 * 将指定的收货地址设置为默认
	 * @param aid 收货地址的数据id
	 * @param uid 当前登录的用户的id
	 * @param username 当前登录的用户名
	 * @throws AddressNotFoundException 收货地址数据不存在
	 * @throws AccessDeniedException 尝试访问他人的收货地址数据
	 * @throws UpdateException 更新数据异常
	 */
	void setDefault(Integer aid, 
		Integer uid, String username) 
			throws AddressNotFoundException, 
				AccessDeniedException, 
				UpdateException;
	
	/**
	 * 删除收货地址
	 * @param aid 收货地址的数据id
	 * @param uid 当前登录的用户的id
	 * @param username 当前登录的用户名
	 * @throws AddressNotFoundException 收货地址数据不存在
	 * @throws AccessDeniedException 尝试访问他人的收货地址数据
	 * @throws DeleteException 删除数据异常
	 * @throws UpdateException 更新数据异常
	 */
	void delete(Integer aid, 
		Integer uid, String username) 
			throws AddressNotFoundException, 
				AccessDeniedException, 
				DeleteException, 
				UpdateException;
}








package cn.tedu.store.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tedu.store.entity.Address;

/**
 * 处理收货地址数据的持久层接口
 */
public interface AddressMapper {

	/**
	 * 插入收货地址数据
	 * @param address 收货地址数据
	 * @return 受影响的行数
	 */
	Integer insert(Address address);
	
	/**
	 * 根据收货地址id删除数据
	 * @param aid 收货地址id
	 * @return 受影响的行数
	 */
	Integer deleteByAid(Integer aid);
	
	/**
	 * 将某用户的所有收货地址设置为非默认
	 * @param uid 用户的id
	 * @return 受影响的行数
	 */
	Integer updateNonDefault(Integer uid);

	/**
	 * 将指定的收货地址设置为默认
	 * @param aid 收货地址的数据id
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间 
	 * @return 受影响的行数
	 */
	Integer updateDefault(
	    @Param("aid") Integer aid, 
	    @Param("modifiedUser") String modifiedUser, 
	    @Param("modifiedTime") Date modifiedTime);

	/**
	 * 统计某个用户的收货地址数据的数量
	 * @param uid 用户的id
	 * @return 该用户的收货地址数据的数量
	 */
	Integer countByUid(Integer uid);
	
	/**
	 * 根据用户id查询该用户的收货地址列表
	 * @param uid 用户id
	 * @return 该用户的收货地址列表
	 */
	List<Address> findByUid(Integer uid);
	
	/**
	 * 根据收货地址的数据id查询详情
	 * @param aid 收货地址的数据id
	 * @return 匹配的收货地址的详情，如果没有匹配的数据，则返回null
	 */
	Address findByAid(Integer aid);
	
	/**
	 * 查询某用户最后一次修改的收货地址数据
	 * @param uid 用户的id
	 * @return 匹配的收货地址的详情，如果没有匹配的数据，则返回null
	 */
	Address findLastModified(Integer uid);
	
}







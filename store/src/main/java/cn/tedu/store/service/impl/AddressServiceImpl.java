package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.store.entity.Address;
import cn.tedu.store.entity.District;
import cn.tedu.store.mapper.AddressMapper;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.service.IDistrictService;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.AddressCountLimitException;
import cn.tedu.store.service.ex.AddressNotFoundException;
import cn.tedu.store.service.ex.DeleteException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.UpdateException;

@Service
public class AddressServiceImpl implements IAddressService {
	
	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private IDistrictService districtService;

	@Override
	public void addnew(Address address, Integer uid, String username)
			throws AddressCountLimitException, InsertException {
		Integer count = countByUid(uid);
		if (count >= ADDRESS_MAX_COUNT) {
			throw new AddressCountLimitException(
				"增加收货地址失败！当前收货地址数量已经达到上限！最多允许创建" + ADDRESS_MAX_COUNT + "条，已经创建" + count + "条！");
		}

		address.setUid(uid);

		// 补全数据：province_name, city_name, area_name
		District province = districtService.getByCode(address.getProvinceCode());
		District city = districtService.getByCode(address.getCityCode());
		District area = districtService.getByCode(address.getAreaCode());
		if (province == null) {
			address.setProvinceCode(null);
		} else {
			address.setProvinceName(province.getName());
		}
		if (city == null) {
			address.setCityCode(null);
		} else {
			address.setCityName(city.getName());
		}
		if (area == null) {
			address.setAreaCode(null);
		} else {
			address.setAreaName(area.getName());
		}

		Integer isDefault = count == 0 ? 1 : 0;
		// 补全数据：is_default
		address.setIsDefault(isDefault);

		Date now = new Date();
		// 补全数据：4个日志
		address.setCreatedUser(username);
		address.setCreatedTime(now);
		address.setModifiedUser(username);
		address.setModifiedTime(now);

		insert(address);
	}

	@Override
	public List<Address> getByUid(Integer uid) {
		return findByUid(uid);
	}

	@Override
	public Address getByAid(Integer aid) {
		return findByAid(aid);
	}
	
	@Override
	@Transactional
	public void setDefault(Integer aid, Integer uid, String username)
			throws AddressNotFoundException, AccessDeniedException, UpdateException {
		Address result = findByAid(aid);
		if (result == null) {
			throw new AddressNotFoundException(
				"设置默认收货地址失败！尝试操作的数据不存在！");
		}

		if (result.getUid() != uid) {
			throw new AccessDeniedException(
				"设置默认收货地址失败！不允许访问他人的数据！");
		}

		updateNonDefault(uid);
		updateDefault(aid, username, new Date());
	}
	
	@Override
	@Transactional
	public void delete(Integer aid, Integer uid, String username)
			throws AddressNotFoundException, AccessDeniedException, DeleteException, UpdateException {
		Address result = findByAid(aid);
		if (result == null) {
			throw new AddressNotFoundException(
				"删除收货地址失败！尝试操作的数据不存在！");
		}

		if (result.getUid() != uid) {
			throw new AccessDeniedException(
				"删除收货地址失败！不允许访问他人的数据！");
		}

		deleteByAid(aid);
		if (result.getIsDefault() == 0) {
			return;
		}

		Integer count = countByUid(uid);
		if (count == 0) {
			return;
		}

		Address lastModifiedAddress = findLastModified(uid);
		updateDefault(lastModifiedAddress.getAid(), username, new Date());
	}
	
	/**
	 * 插入收货地址数据
	 * @param address 收货地址数据
	 * @throws InsertException 插入数据异常
	 */
	private void insert(Address address) throws InsertException {
		Integer rows = addressMapper.insert(address);
		if (rows != 1) {
			throw new InsertException(
				"增加收货地址失败！插入数据时出现未知错误！");
		}
	}
	
	/**
	 * 根据收货地址id删除数据
	 * @param aid 收货地址id
	 * @throws DeleteException 删除数据异常
	 */
	private void deleteByAid(Integer aid) throws DeleteException {
		Integer rows = addressMapper.deleteByAid(aid);
		if (rows != 1) {
			throw new DeleteException(
				"删除收货地址失败！删除时出现未知错误！");
		}
	}

	/**
	 * 将某用户的所有收货地址设置为非默认
	 * @param uid 用户的id
	 * @throws UpdateException 更新数据异常
	 */
	private void updateNonDefault(Integer uid) throws UpdateException {
		Integer rows = addressMapper.updateNonDefault(uid);
		if (rows == 0) {
			throw new UpdateException(
				"设置默认收货地址失败！更新时出现未知错误！");
		}
	}

	/**
	 * 将指定的收货地址设置为默认
	 * @param aid 收货地址的数据id
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间 
	 * @throws UpdateException 更新数据异常
	 */
	private void updateDefault(Integer aid, 
	    String modifiedUser, Date modifiedTime) 
			throws UpdateException {
		Integer rows = addressMapper.updateDefault(aid, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new UpdateException(
				"设置默认收货地址失败！更新时出现未知错误！");
		}
	}
	
	/**
	 * 统计某个用户的收货地址数据的数量
	 * @param uid 用户的id
	 * @return 该用户的收货地址数据的数量
	 */
	private Integer countByUid(Integer uid) {
		return addressMapper.countByUid(uid);
	}
	
	/**
	 * 根据用户id查询该用户的收货地址列表
	 * @param uid 用户id
	 * @return 该用户的收货地址列表
	 */
	private List<Address> findByUid(Integer uid) {
		return addressMapper.findByUid(uid);
	}

	/**
	 * 根据收货地址的数据id查询详情
	 * @param aid 收货地址的数据id
	 * @return 匹配的收货地址的详情，如果没有匹配的数据，则返回null
	 */
	private Address findByAid(Integer aid) {
		return addressMapper.findByAid(aid);
	}

	/**
	 * 查询某用户最后一次修改的收货地址数据
	 * @param uid 用户的id
	 * @return 匹配的收货地址的详情，如果没有匹配的数据，则返回null
	 */
	private Address findLastModified(Integer uid) {
		return addressMapper.findLastModified(uid);
	}


}






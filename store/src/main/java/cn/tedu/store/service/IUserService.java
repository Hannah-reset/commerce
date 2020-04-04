package cn.tedu.store.service;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameDuplicateException;

/**
 * 处理用户数据的业务层接口
 */
public interface IUserService {

	/**
	 * 用户注册 
	 * @param user 用户数据对象
	 * @throws UsernameDuplicateException 用户名已经被占用的异常
	 * @throws InsertException 插入用户数据失败的异常
	 */
	void reg(User user) 
		throws UsernameDuplicateException, 
			InsertException;
	
	/**
	 * 用户登录
	 * @param username 用户名
	 * @param password 密码
	 * @return 登录成功的用户的信息
	 * @throws UserNotFoundException 用户数据不存在，例如用户名尚未注册，或用户数据被标记为已删除
	 * @throws PasswordNotMatchException 密码错误
	 */
	User login(String username, String password) 
		throws UserNotFoundException, 
			PasswordNotMatchException;
	
	/**
	 * 修改密码
	 * @param uid 用户的id
	 * @param username 用户名
	 * @param oldPassword 原密码
	 * @param newPassword 新密码
	 * @throws UserNotFoundException 用户数据不存在，或者已经被标记为删除
	 * @throws PasswordNotMatchException 原密码错误
	 * @throws UpdateException 更新数据失败
	 */
	void changePassword(
		Integer uid, String username, 
		String oldPassword, String newPassword) 
			throws UserNotFoundException, 
				PasswordNotMatchException, UpdateException;
	
	/**
	 * 修改头像
	 * @param uid 用户的id
	 * @param username 用户名
	 * @param avatar 头像的路径
	 * @throws UserNotFoundException 用户数据不存在，或者已经被标记为删除
	 * @throws UpdateException 更新数据失败
	 */
	void changeAvatar(Integer uid, String username, String avatar) 
		throws UserNotFoundException, UpdateException;
	
	/**
	 * 根据用户id查询用户数据
	 * @param uid 用户的id
	 * @return 匹配的用户数据，如果没有匹配的数据，则返回null
	 */
	User getByUid(Integer uid);

	/**
	 * 更新用户基本资料
	 * @param user 封装了用户基本资料的对象，至少需要封装用户的id和用户名，可选择性的封装用户的手机号码、电子邮箱、年龄等
	 * @throws UserNotFoundException 尝试访问的用户数据不存在，或者被标记为已删除
	 * @throws UpdateException 更新用户数据失败
	 */
	void changeInfo(User user) 
		throws UserNotFoundException, UpdateException;
	
}









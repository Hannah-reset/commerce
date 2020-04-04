package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.tedu.store.entity.User;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameDuplicateException;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public void reg(User user) throws UsernameDuplicateException, InsertException {
		String username = user.getUsername();
		User result = userMapper.findByUsername(username);
		if (result != null) {
			throw new UsernameDuplicateException(
				"注册失败！尝试注册的用户名(" + username + ")已经被占用！");
		}

		//System.err.println("reg() > password=" + user.getPassword());
		String salt = UUID.randomUUID().toString().toUpperCase();
		String md5Password = getMd5Password(user.getPassword(), salt);
		user.setSalt(salt);
		user.setPassword(md5Password);
		//System.err.println("reg() > salt=" + salt);
		//System.err.println("reg() > md5Password=" + md5Password);
		
		user.setIsDelete(0);

		Date now = new Date();
		user.setCreatedUser(username);
		user.setCreatedTime(now);
		user.setModifiedUser(username);
		user.setModifiedTime(now);

		Integer rows = userMapper.insert(user);
		if (rows != 1) {
			throw new InsertException(
				"注册失败！写入数据时出现未知错误！");
		}
	}
	
	@Override
	public User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException {
		User result = userMapper.findByUsername(username);
		if (result == null) {
			throw new UserNotFoundException(
				"登录失败！用户名不存在！");
		}

		if (result.getIsDelete() == 1) {
			throw new UserNotFoundException(
				"登录失败！用户名不存在！");
		}

		String salt = result.getSalt();
		String md5Password = getMd5Password(password, salt);
		if (!result.getPassword().equals(md5Password)) {
			throw new PasswordNotMatchException(
				"登录失败！密码错误！");
		}

		result.setPassword(null);
		result.setSalt(null);
		result.setIsDelete(null);
		return result;
	}
	
	@Override
	public void changePassword(Integer uid, String username, String oldPassword, String newPassword)
			throws UserNotFoundException, PasswordNotMatchException, UpdateException {
		//System.err.println("changePassword() ---> BEGIN:");
		//System.err.println("changePassword() 原密码=" + oldPassword);
		//System.err.println("changePassword() 新密码=" + newPassword);
		
		User result = userMapper.findByUid(uid);
		if (result == null) {
			throw new UserNotFoundException(
				"修改密码失败！用户名不存在！");
		}

		if (result.getIsDelete() == 1) {
			throw new UserNotFoundException(
				"修改密码失败！用户名不存在！");
		}

		String salt = result.getSalt();
		String oldMd5Password = getMd5Password(oldPassword, salt);
		//System.err.println("changePassword() 盐值=" + salt);
		//System.err.println("changePassword() 原密码加密=" + oldMd5Password);
		//System.err.println("changePassword() 正确密码=" + result.getPassword());
		if (!result.getPassword().equals(oldMd5Password)) {
			throw new PasswordNotMatchException(
				"修改密码失败！原密码错误！");
		}

		String newMd5Password = getMd5Password(newPassword, salt);
		//System.err.println("changePassword() 新密码加密=" + newMd5Password);
		Date now = new Date();
		Integer rows = userMapper.updatePassword(uid, newMd5Password, username, now);
		if (rows != 1) {
			// 抛出：UpdateException
			throw new UpdateException(
				"修改密码失败！更新密码时出现未知错误！");
		}
		
		//System.err.println("changePassword() ---> END.");
	}

	@Override
	public void changeAvatar(Integer uid, String username, String avatar)
			throws UserNotFoundException, UpdateException {
		User result = userMapper.findByUid(uid);
		if (result == null) {
			throw new UserNotFoundException(
				"修改头像失败！用户名不存在！");
		}

		if (result.getIsDelete() == 1) {
			throw new UserNotFoundException(
				"修改头像失败！用户名不存在！");
		}
		
		Date now = new Date();
		Integer rows = userMapper.updateAvatar(uid, avatar, username, now);
		if (rows != 1) {
			throw new UpdateException(
				"修改头像失败！更新头像时出现未知错误！");
		}
	}
	
	@Override
	public User getByUid(Integer uid) {
		User result = userMapper.findByUid(uid);

		if (result != null) {
			result.setPassword(null);
			result.setSalt(null);
			result.setIsDelete(null);
		}

		return result;
	}

	@Override
	public void changeInfo(User user) throws UserNotFoundException, UpdateException {
		User result = userMapper.findByUid(user.getUid());
		if (result == null) {
			throw new UserNotFoundException(
				"修改个人资料失败！用户数据不存在！");
		}

		if (result.getIsDelete() == 1) {
			throw new UserNotFoundException(
				"修改个人资料失败！用户数据不存在！");
		}

		Date now = new Date();
		user.setModifiedUser(user.getUsername());
		user.setModifiedTime(now);
		Integer rows = userMapper.updateInfo(user);
		if (rows != 1) {
			throw new UpdateException(
				"修改个人资料失败！更新用户数据时出现未知错误！");
		}
	}
	
	/**
	 * 对密码进行加密
	 * @param password 原始密码
	 * @param salt 盐值
	 * @return 加密后的密码
	 */
	private String getMd5Password(String password, String salt) {
		String str = password + salt;
		for (int i = 0; i < 3; i++) {
			str = DigestUtils
				.md5DigestAsHex(str.getBytes()).toUpperCase();
		}
		return str;
	}

	
	
}




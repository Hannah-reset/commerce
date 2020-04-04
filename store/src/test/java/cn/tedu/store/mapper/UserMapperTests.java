package cn.tedu.store.mapper;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTests {

	@Autowired
	private UserMapper mapper;	

	@Test
	public void insert() {
		User user = new User();
		user.setUsername("root");
		user.setPassword("1234");
		Integer rows = mapper.insert(user);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void updateInfo() {
		User user = new User();
		user.setUid(7);
		user.setPhone("13800138007");
		user.setEmail("root@qq.com");
		user.setGender(1);
		Integer rows = mapper.updateInfo(user);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void updatePassword() {
		Integer uid = 9;
		String password = "888888";
		String modifiedUser = "超级管理员";
		Date modifiedTime = new Date();
		Integer rows = mapper.updatePassword(uid, password, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void updateAvatar() {
		Integer uid = 10;
		String avatar = "这是一个头像路径";
		String modifiedUser = "超级管理员";
		Date modifiedTime = new Date();
		Integer rows = mapper.updateAvatar(uid, avatar, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}

	@Test
	public void findByUid() {
		Integer uid = 7;
		User user = mapper.findByUid(uid);
		System.err.println(user);
	}
	
	@Test
	public void findByUsername() {
		String username = "root";
		User user = mapper.findByUsername(username);
		System.err.println(user);
	}
	
}








package cn.tedu.store.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.Order;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTests {

	@Autowired
	private IOrderService service;
	
	@Test
	public void create() {
		Integer aid = 20;
		Integer[] cids = {7,9,12,13};
		Integer uid = 7;
		String username = "超级管理员";
		Order order = service.create(aid, cids, uid, username);
		System.err.println(order);
	}
	
}







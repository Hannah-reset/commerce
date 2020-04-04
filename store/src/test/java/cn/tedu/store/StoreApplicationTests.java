package cn.tedu.store;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	DataSource dataSource;
	
	@Test
	public void getConnection() throws SQLException {
		Connection conn = dataSource.getConnection();
		System.err.println(conn);
	}
	
	@Test
	public void messageDigest() {
		String str = "000000";
		String salt = UUID.randomUUID().toString();
		System.err.println(salt);
		
		for (int i = 0; i < 3; i++) {
			str = salt + str;
			str = DigestUtils.md5Hex(str);
			System.err.println(str);
		}
		
	}
	
}











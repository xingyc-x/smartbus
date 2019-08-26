package com.pcis.smartbus.db;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.dao.UserManualMapper;
import com.pcis.smartbus.db.domain.SmartbusUser;

/**
* @author qishan
* @version 创建时间：2019年8月19日 下午3:16:07
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfiguration.class)
//@ContextConfiguration(lo)
@EnableAutoConfiguration
public class BDTest {
	
//	@Autowired
//	SmartbusUserMapper userMapper;
//	
	@Autowired
	UserManualMapper userMapper;
	
	@Test
	public void Test1() {
		
		SmartbusUser user = userMapper.selectByUserName("admin");
		assertEquals(user.getUserName(), "admin");
	}

}

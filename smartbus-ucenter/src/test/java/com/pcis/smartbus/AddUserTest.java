package com.pcis.smartbus;

import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.controller.AddUserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={UCenterApplication.class})
public class AddUserTest {
    @Autowired
    AddUserController addUserController;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SmartbusUserMapper smartbusUserMapper;

    @Test
    public void validateUserNameTest() throws Exception {
        String temp = addUserController.validateUserName("root", "unknownParam");
        logger.info(temp);
    }

    @Test
    public void Test1() throws Exception {
        SmartbusUser smartbusUser = smartbusUserMapper.selectByPrimaryKey(1);
        System.out.println(smartbusUser);
    }

    @Test
    public void Test2() throws Exception {
        SmartbusUser smartbusUser = new SmartbusUser();
        smartbusUser.setUserName("zhangsan");
        smartbusUser.setRealName("张三");
        smartbusUser.setPassword("password");
        smartbusUser.setPhone("123");
        smartbusUser.setEmail("123");
        smartbusUser.setCapacity(1);
        smartbusUser.setCompanyId(2);
        LocalDateTime localDateTime = LocalDateTime.now();
        smartbusUser.setCreated(localDateTime);
        smartbusUser.setUpdated(localDateTime);
        smartbusUserMapper.insert(smartbusUser);
        System.out.println(smartbusUser);
    }
}

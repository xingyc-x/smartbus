package com.pcis.smartbus;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.db.dao.CompanyManualMapper;
import com.pcis.smartbus.db.dao.CompanyMapper;
import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.domain.Company;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.CompanyService;
import com.pcis.smartbus.ucenter.service.UserService;
import com.pcis.smartbus.ucenter.utils.UserUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={UCenterApplication.class})
public class GetUserTest {
    @Autowired
    private UserService userService;
    @Autowired
    UserUtils userUtils;
    @Autowired
    CompanyService companyService;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    public  String getRandomString(int length){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(2);
            long result=0;
            switch(number){
                case 0:
                    result=Math.round(Math.random()*25+65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }

    @Test
    public void registerTest() throws Exception {
            //String userName = "root";
           // String realName = "I do not know";
            //String password = "password";
            //String phone = "non";
            //String email = "non";
            for (int i = 0; i < 23; ++i) {
                String userName = getRandomString(5);
                String realName = getRandomString(5);
                String password = getRandomString(5);
                String phone = getRandomString(5);
                String email = getRandomString(5);
                int capacity = 1;
                int companyId = 10;
                int flag = userService.register(userName, realName, password, phone, email, capacity, companyId);
            }


    }

    @Test
    public void mybaitsTest() throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        SmartbusUserMapper smartbusUserMapper = session.getMapper(SmartbusUserMapper.class);
        String userName = "root";
        CompanyMapper companyMapper = session.getMapper(CompanyMapper.class);
        CompanyManualMapper companyManualMapper = session.getMapper(CompanyManualMapper.class);
        for (int j =0; j < 10000; ++j) {
        for (int i = 0; i < 100000; ++i) {
            Company company = new Company();
            company.setCompanyName(getRandomString(30));
            company.setCompanyInfo(getRandomString(200));
            company.setImgPath("/1.gif");
            company.setShortName(getRandomString(5));
            companyMapper.insert(company);
        }
        session.commit();}
        List<Company> companies = companyManualMapper.getCompanyIdBySearch("%s%");
        logger.info(String.valueOf(companies));
        //List<SmartbusUser> smartbusUsers = smartbusUserMapper.getAPageUserByCapacity(5, 10, "id", "asc",0);
        //System.out.println(smartbusUsers);
    }

    @Test
    public void test4() throws Exception {
        SmartbusUser smartbusUser = userService.geUserByName("root");
        JSONObject userInfo = userUtils.getResponseUserInfoJson(smartbusUser, false,false, false);
        logger.info(String.valueOf(userInfo));
        logger.info(String.valueOf(companyService));
    }

    @Test
    public void test5() throws Exception {
        int num = userService.getUserNum();
        System.out.println(num);
        List<SmartbusUser> smartbusUsers = userService.getAPageUser(1, 10, "id", "desc");
        logger.info(String.valueOf(smartbusUsers));
    }

    @Test
    public void test6() throws Exception {
        SmartbusUser smartbusUser = userService.geUserByName("unknownName");
        logger.info(String.valueOf(smartbusUser));
    }


}

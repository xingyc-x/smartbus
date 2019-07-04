package com.pcis.smartbus.ucenter.service.impl;

import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.UserService;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


@Service
public class UserServiceImpl implements UserService {

    private SqlSession session;

    public UserServiceImpl(){
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String getRandomString2(int length){
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

    @Override
    public boolean register(String name, String password) throws Exception {
        System.out.println("I am here!");
        SmartbusUser smartbusUser = new SmartbusUser();
        smartbusUser.setUsername(getRandomString2(10));
        smartbusUser.setPassword(password);
        LocalDateTime localDateTime = LocalDateTime.now();
        smartbusUser.setCreated(localDateTime);
        smartbusUser.setUpdated(localDateTime);
        smartbusUser.setId((long)2);
        SmartbusUserMapper smartbusUserMapper = session.getMapper(SmartbusUserMapper.class);
        try {
            while (true) {
                smartbusUser.setUsername(getRandomString2(10));
                int result = smartbusUserMapper.insert(smartbusUser);
                session.commit();

            }
            //session.commit();
            //session.commit();
            //List<SmartbusUser> smartbusUserList = session.selectList("selectAll");
            //System.out.println(smartbusUserList);
            //System.out.println("every things is ok!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
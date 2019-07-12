package com.pcis.smartbus.ucenter.service.impl;

import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.UserService;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


@Service
public class UserServiceImpl implements UserService {

    private SqlSession session;
    private SmartbusUserMapper smartbusUserMapper;
    private Logger logger;

    public UserServiceImpl(){
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            smartbusUserMapper = session.getMapper(SmartbusUserMapper.class);
            logger = Logger.getLogger(UserServiceImpl.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public boolean register(String userName, String realName, String password, String phone, String email, int capacity, int companyId) throws Exception {
        SmartbusUser smartbusUser = new SmartbusUser();
        smartbusUser.setUserName(userName);
        smartbusUser.setRealName(realName);
        smartbusUser.setPassword(password);
        smartbusUser.setPhone(phone);
        smartbusUser.setEmail(email);
        smartbusUser.setCapacity(capacity);
        smartbusUser.setCompanyId(companyId);
        LocalDateTime localDateTime = LocalDateTime.now();
        smartbusUser.setCreated(localDateTime);
        smartbusUser.setUpdated(localDateTime);
        try {
                int result = smartbusUserMapper.insert(smartbusUser);
                session.commit();

            } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return true;
    }


    @Override
    public SmartbusUser geUserByName(String userName) {
        try {
            SmartbusUser smartbusUser = smartbusUserMapper.selectByUserName(userName);
            return smartbusUser;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public  int getUserNum() {
        return smartbusUserMapper.getUserNum();
    }

    @Override
    public List<SmartbusUser> getAPageUser(int pageNo, int pageSize, String sortBy, String direction) {
        if (pageNo == 0 ) {
            return null;
        }
        int startNo = (pageNo - 1) * pageSize;
        return smartbusUserMapper.getAPageUser(startNo, pageSize, sortBy, direction);
//        if (direction.equals("desc")) {
//            return smartbusUserMapper.getAPageUserDesc(startNo, pageSize, sortBy);
//        } else if (direction.equals("asc")) {
//            return smartbusUserMapper.getAPageUserAsc(startNo, pageSize, sortBy);
//        }
//        return null;
    }

    @Override
    public  List<SmartbusUser> getAPageUserBySearch(int pageNo, int pageSize, String sortBy, String direction, int searchIf, String searchInput) {
        if (pageNo == 0 ) {
            return null;
        }
        int startNo = (pageNo - 1) * pageSize;

        switch (searchIf) {
            //查询类型为姓名
            case 1:
                return smartbusUserMapper.getAPageUserByNameSearch(startNo, pageSize, sortBy, direction, "%" + searchInput + "%");

            //查询类型为单位；
            case 2:
                return smartbusUserMapper.getAPageUserByCompanySearch(startNo, pageSize, sortBy, direction, "%" + searchInput + "%");

                //查询类型为权限等级
             case 3:
                return smartbusUserMapper.getAPageUserByCapacity(startNo, pageSize, sortBy, direction, Integer.valueOf(searchInput));
            default:
                return null;

        }
    }

    @Override
    public int getUserNumBySearch(int searchIf, String searchInput) {
        switch (searchIf) {
            //查询类型为姓名
            case 1:
                return smartbusUserMapper.getUserNumByNameSearch("%" + searchInput + "%");

            //查询类型为单位；
            case 2:
                return smartbusUserMapper.getUserNumByCompanySearch("%" + searchInput + "%");

            //查询类型为权限等级
            case 3:
                return smartbusUserMapper.getUserNumByCapacity(Integer.valueOf(searchInput));
            default:
                return 0;
        }
    }

}
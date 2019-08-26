package com.pcis.smartbus.ucenter.service.impl;

import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.dao.UserManualMapper;
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

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


@Service
public class UserServiceImpl implements UserService {

    //private SqlSession session;
    @Autowired
    private SmartbusUserMapper smartbusUserMapper;
    @Autowired
    private UserManualMapper userManualMapper;
    private Logger logger = Logger.getLogger(UserServiceImpl.class);

//    public UserServiceImpl(){
//        try {
//            String resource = "mybatis-config1.xml";
//            InputStream inputStream = Resources.getResourceAsStream(resource);
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//            session = sqlSessionFactory.openSession();
//            smartbusUserMapper = session.getMapper(SmartbusUserMapper.class);
//            logger = Logger.getLogger(UserServiceImpl.class);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }

    @Override
    public int register(String userName, String realName, String password, String phone, String email, int capacity, int companyId) throws Exception {
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
                return result;

            } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public int register(SmartbusUser smartbusUser) {
        LocalDateTime localDateTime = LocalDateTime.now();
        smartbusUser.setCreated(localDateTime);
        smartbusUser.setUpdated(localDateTime);
        try {
            int result = smartbusUserMapper.insert(smartbusUser);
            return result;
            //session.commit();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return 0;
    }


    @Override
    public SmartbusUser geUserByName(String userName) {
        try {
            //System.out.println(userManualMapper);
            SmartbusUser smartbusUser = userManualMapper.selectByUserName(userName);
            return smartbusUser;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public  int getUserNum() {
        return userManualMapper.getUserNum();
    }

    @Override
    public List<SmartbusUser> getAPageUser(int startNo, int pageSize, String sortBy, String direction, String companyIdString) {
        return userManualMapper.getAPageUser(startNo, pageSize, sortBy, direction, companyIdString);
    }

    @Override
    public  List<SmartbusUser> getAPageUserBySearch(int startNo, int pageSize, String sortBy, String direction, int searchIf, String searchInput, String companyIdString) {
        switch (searchIf) {
            //查询类型为姓名
            case 1:
                return userManualMapper.getAPageUserByNameSearch(startNo, pageSize, sortBy, direction, "%" + searchInput + "%", companyIdString);

            //查询类型为单位；
            case 2:
                return userManualMapper.getAPageUserByCompanySearch(startNo, pageSize, sortBy, direction, "%" + searchInput + "%", companyIdString);

                //查询类型为权限等级
             case 3:
                return userManualMapper.getAPageUserByCapacity(startNo, pageSize, sortBy, direction, Integer.valueOf(searchInput), companyIdString);
            default:
                return null;

        }
    }

    @Override
    public int getUserNumBySearch(int searchIf, String searchInput) {
        switch (searchIf) {
            //查询类型为姓名
            case 1:
                return userManualMapper.getUserNumByNameSearch("%" + searchInput + "%");

            //查询类型为单位；
            case 2:
                return userManualMapper.getUserNumByCompanySearch("%" + searchInput + "%");

            //查询类型为权限等级
            case 3:
                return userManualMapper.getUserNumByCapacity(Integer.valueOf(searchInput));
            default:
                return 0;
        }
    }

    public SmartbusUser getUserById(int id) {
        return smartbusUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean reviseUser(SmartbusUser user) {
        SmartbusUser user1 = smartbusUserMapper.selectByPrimaryKey(user.getId());
        user.setId(user1.getId());
        user.setCreated(user1.getCreated());
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setUpdated(localDateTime);
        smartbusUserMapper.updateByPrimaryKey(user);
        return true;
    }


}
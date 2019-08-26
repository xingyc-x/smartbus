package com.pcis.smartbus.db.dao;

import com.pcis.smartbus.db.domain.SmartbusUser;

import java.util.List;

// this file wrote by hand, differ from auto generated
//dao下*Mapper.java 与 *Mapper.xml必须一致，否则找不到映射路径
public interface UserManualMapper {
    //This is wrote by hand.
    SmartbusUser selectByUserName(String userName);

    Integer getUserNum();

    //这两个接口没有用到
    List<SmartbusUser> getAPageUserDesc(int startNo, int pageSize, String sortBy);
    List<SmartbusUser> getAPageUserAsc(int startNo, int pageSize, String sortBy);

    //获取一个页面的用户，威腾公司时companyIdString = '%'
    List<SmartbusUser> getAPageUser(int startNo, int pageSize, String sortBy, String direction, String companyIdString);

    //通过搜索的方式获取用户列表, 威腾公司时companyIdString = '%'
    List<SmartbusUser> getAPageUserByNameSearch(int startNo, int pageSize, String sortBy, String direction, String searchContent, String companyIdString);
    List<SmartbusUser> getAPageUserByCapacity(int startNo, int pageSize, String sortBy, String direction, int capacity, String companyIdString);
    List<SmartbusUser> getAPageUserByCompanySearch(int startNo, int pageSize, String sortBy, String direction, String searchContent, String companyIdString);

    Integer getUserNumByNameSearch(String searchContent);
    Integer getUserNumByCapacity(int capacity);
    Integer getUserNumByCompanySearch(String searchContent);

}

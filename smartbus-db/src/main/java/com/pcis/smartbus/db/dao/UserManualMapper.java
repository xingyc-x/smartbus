package com.pcis.smartbus.db.dao;

import com.pcis.smartbus.db.domain.SmartbusUser;

import java.util.List;

// this file wrote by hand, differ from auto generated
//dao下*Mapper.java 与 *Mapper.xml必须一致，否则找不到映射路径
public interface UserManualMapper {
    //This is wrote by hand.
    SmartbusUser selectByUserName(String userName);

    Integer getUserNum();

    List<SmartbusUser> getAPageUserDesc(int startNo, int pageSize, String sortBy);
    List<SmartbusUser> getAPageUserAsc(int startNo, int pageSize, String sortBy);
    List<SmartbusUser> getAPageUser(int startNo, int pageSize, String sortBy, String direction);

    List<SmartbusUser> getAPageUserByNameSearch(int startNo, int pageSize, String sortBy, String direction, String searchContent);
    List<SmartbusUser> getAPageUserByCapacity(int startNo, int pageSize, String sortBy, String direction, int capacity);
    List<SmartbusUser> getAPageUserByCompanySearch(int startNo, int pageSize, String sortBy, String direction, String searchContent);

    Integer getUserNumByNameSearch(String searchContent);
    Integer getUserNumByCapacity(int capacity);
    Integer getUserNumByCompanySearch(String searchContent);
}

package com.pcis.smartbus.ucenter.service;


import com.pcis.smartbus.db.domain.SmartbusUser;

import java.util.List;

public interface UserService {
    int register(String userName, String realName, String password, String phone, String email, int capacity, int companyId) throws Exception;
    int register(SmartbusUser smartbusUser);
    //如果没有该用户，则返回null
    SmartbusUser geUserByName(String userName);
    int getUserNum();
    List<SmartbusUser> getAPageUser(int startNo, int pageSize, String sortBy, String direction, String companyIdString);
    List<SmartbusUser> getAPageUserBySearch(int startNo, int pageSize, String sortBy, String direction, int searchIf, String searchInput, String companyIdString);
    int getUserNumBySearch(int searchIf, String searchInput);
    SmartbusUser getUserById(int id);

    boolean reviseUser(SmartbusUser user);
}

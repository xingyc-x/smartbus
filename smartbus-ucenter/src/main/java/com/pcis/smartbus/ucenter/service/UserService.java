package com.pcis.smartbus.ucenter.service;


import com.pcis.smartbus.db.domain.SmartbusUser;

import java.util.List;

public interface UserService {
    boolean register(String userName, String realName, String password, String phone, String email, int capacity, int companyId) throws Exception;
    SmartbusUser geUserByName(String userName);
    int getUserNum();
    List<SmartbusUser> getAPageUser(int pageNo, int pageSize, String sortBy, String direction);
    List<SmartbusUser> getAPageUserBySearch(int pageNo, int pageSize, String sortBy, String direction, int searchIf, String searchInput);
    int getUserNumBySearch(int searchIf, String searchInput);
}

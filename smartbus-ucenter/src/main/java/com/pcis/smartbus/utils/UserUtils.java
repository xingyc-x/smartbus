package com.pcis.smartbus.utils;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.db.domain.Company;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private CompanyService companyService;
    private final boolean IS_SHOW_PASSWORD = false;

    public JSONObject getResponseUserInfoJson(SmartbusUser smartbusUser) {
        JSONObject object = new JSONObject();
        object.put("userLoginName", smartbusUser.getUserName());
        if (IS_SHOW_PASSWORD) {
            object.put("userPassword", smartbusUser.getPassword());
        } else {
            object.put("userPassword", "null");
        }
        object.put("userName", smartbusUser.getRealName());
        object.put("userSex", "null");
        String companyName = companyService.getCompanyName(smartbusUser.getCompanyId());
        object.put("userOrganization", companyName);
        object.put("userTel", smartbusUser.getPhone());
        object.put("userEmail", smartbusUser.getEmail());
        object.put("userLevel", smartbusUser.getCapacity());
        object.put("userEmployTime", smartbusUser.getCreated());
        object.put("userEducation","null");
        object.put("userExtraInfo","null");
        return object;
    }
}

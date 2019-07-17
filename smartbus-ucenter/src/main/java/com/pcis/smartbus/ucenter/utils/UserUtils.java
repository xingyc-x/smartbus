package com.pcis.smartbus.ucenter.utils;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private CompanyService companyService;
    //private final boolean IS_SHOW_PASSWORD = false;
    private final String[] CAPACITY_NAMES = {"威腾管理人员", "区域业务员", "客户公司管理人员", "客户公司运维人员"};
    //CAPACITY is 1,2,3,4


    public JSONObject getResponseUserInfoJson(SmartbusUser smartbusUser, boolean isGivePassword, boolean isGiveLevelNum) {
        JSONObject object = new JSONObject();
        object.put("userId", smartbusUser.getId());
        object.put("userLoginName", smartbusUser.getUserName());
        if (isGivePassword) {
            object.put("userPassword", smartbusUser.getPassword());
        } else {
            object.put("userPassword", "null");
        }
        object.put("userName", smartbusUser.getRealName());
        //object.put("userSex", "null");
        String companyName = companyService.getCompanyName(smartbusUser.getCompanyId());
        object.put("userOrganization", companyName);
        object.put("userTel", smartbusUser.getPhone());
        object.put("userEmail", smartbusUser.getEmail());
        if (isGiveLevelNum) {
            object.put("userLevel", smartbusUser.getCapacity());
        } else {
            object.put("userLevel", CAPACITY_NAMES[smartbusUser.getCapacity() - 1]);
        }
        object.put("createTime", smartbusUser.getCreated());
        object.put("updateTime", smartbusUser.getUpdated());
        //object.put("userEducation","null");
        //object.put("userExtraInfo","null");
        return object;
    }


    public boolean validateAuthority(int myId, int myCapacity, SmartbusUser operatedUser) {
        //1 威腾公司管理员
        //2 区域业务员
        //3 客户公司管理人员
        //4 客户公司运维人员
        //同等权限可以改自己和删自己
        //可以改和删比自己权限低的

        if (operatedUser.getCapacity() < myCapacity ||
                operatedUser.getId() == myId) {
            return true;
        } else {
            return false;
        }
    }

}

package com.pcis.smartbus.ucenter.utils;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.CompanyService;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private CompanyService companyService;
    //private final boolean IS_SHOW_PASSWORD = false;
    private final String[] CAPACITY_NAMES = {"威腾管理人员", "区域业务员", "客户公司管理人员", "客户公司运维人员"};
    //CAPACITY is 1,2,3,4
    private Logger logger = Logger.getLogger(UserUtils.class);


    public JSONObject getResponseUserInfoJson(SmartbusUser smartbusUser, boolean isGivePassword, boolean isGiveLevelNum, boolean isGiveCompanyId) {
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
        if (isGiveCompanyId) {
            object.put("userOrganization", smartbusUser.getCompanyId());
        } else {
            String companyName = companyService.getCompanyName(smartbusUser.getCompanyId());
            object.put("userOrganization", companyName);
        }
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
        if (operatedUser == null) {
            return false;
        }
        //System.out.println(operatedUser.getId());
        //添加用户时，没有指定id ，operatedUser.getId()是null
        //第二条必须放在第三条之前，否则会出问题
        //这个权限验证的函数极其重要，是导致诸多bug的核心因素
        //但是，也是减少bug的核心因素
        //所以权限验证必须写于此，调用此，
        //基本逻辑如下：
        //S1:如果被操作用户不存在，则返回false
        //S2:如果sysUser权限大于被操作这，则返回true
        //S3:如果是WEITEN_ADMIN，则可以更改其他WEITEN_ADMIN
        //S4:如果是自己，可以改自己
        //operatedUser.getId()是null有null的可能，这里用try
        //什么时候会null，增加用户，权限为1和权限比较小时，即合法时这里不会执行到S4,所以基本逻辑满足
        try {
            if (operatedUser.getCapacity() > myCapacity ||
                myCapacity == Constant.WEITEN_ADMIN ||
                    operatedUser.getId() == myId) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

}

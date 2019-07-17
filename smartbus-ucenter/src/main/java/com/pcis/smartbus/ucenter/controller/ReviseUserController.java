package com.pcis.smartbus.ucenter.controller;

import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.UserService;
import com.pcis.smartbus.ucenter.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.pcis.smartbus.common.Constant.NO_PASS;
import static com.pcis.smartbus.common.Constant.PASS;

@RestController
public class ReviseUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    private final boolean GIVE_PASSWORD = true;
    private final boolean GIVE_LEVEL_NUM = true;

    @PostMapping(value = "api/user/validateAuthority")
    public String validateAuthorityByApi(
            @RequestParam("userId")int operatedUserId,
            HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        SmartbusUser operatedUser = userService.getUserById(operatedUserId);
        if (userUtils.validateAuthority(myId, myCapacity, operatedUser)) {
            return PASS;
        } else {
            return NO_PASS;
        }
    }

    @PostMapping(value = "api/user/getOperatedUserInfo")
    public String getOperatedUserInfo(@RequestParam("userId")int operatedUserId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        SmartbusUser operatedUser = userService.getUserById(operatedUserId);
        if (userUtils.validateAuthority(myId, myCapacity, operatedUser)) {
            return userUtils.getResponseUserInfoJson(operatedUser, GIVE_PASSWORD, GIVE_LEVEL_NUM).toJSONString();
        } else {
            return "{}";
        }
    }

    @PostMapping(value = "api/user/reviseUser")
    public String reviseUser(
        @RequestParam("sysUserId")String sysUserId,
        @RequestParam("userLoginName")String userLoginName,
        @RequestParam("userPassword")String userPassword,
        @RequestParam("userName")String userName,
        @RequestParam("userOrganization")String userOrganization,
        @RequestParam("userTel")String userTel,
        @RequestParam("userEmail")String userEmail,
        @RequestParam("userLevel")int userLevel,
        @RequestParam("userProject")String userProject,
        HttpSession session) {
            int capacity = (int) session.getAttribute(Constant.CAPACITY);
            return "null";
    }


}

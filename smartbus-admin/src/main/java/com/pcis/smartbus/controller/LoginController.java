package com.pcis.smartbus.controller;

import com.pcis.smartbus.db.dao.ProjectUserRelationManualMapper;
import com.pcis.smartbus.db.dao.ProjectUserRelationMapper;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.UserService;
import com.pcis.smartbus.ucenter.utils.MD5Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.pcis.smartbus.common.Constant.CAPACITY;
import static com.pcis.smartbus.common.Constant.USER_ID;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectUserRelationManualMapper relationDao;

    @PostMapping(value = "api/user/login")
    public String login(
            @RequestParam("name")String userName,
            @RequestParam("password")String password,
            HttpSession session) throws Exception {
        SmartbusUser smartbusUser = userService.geUserByName(userName);
        if (smartbusUser == null)
        	return "FAIL";
        //用户存在判断密码
        if(smartbusUser.getPassword().equals(MD5Util.MD5(password+smartbusUser.getSalt())))
        {
        	int myId = smartbusUser.getId();
            session.setAttribute(USER_ID, myId);
            session.setAttribute(CAPACITY, smartbusUser.getCapacity());
            return "ok";//String.valueOf(myId);
        }
        return "FAIL";
    }

    @GetMapping(value = "api/user/loginOut")
    public String loginOut(HttpSession session) {
        if (session.getAttribute(USER_ID) != null) {
            session.removeAttribute(USER_ID);
            session.removeAttribute(CAPACITY);
        }
        return "OK";
    }
}

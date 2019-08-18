package com.pcis.smartbus.controller;

import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.pcis.smartbus.common.Constant.CAPACITY;
import static com.pcis.smartbus.common.Constant.USER_ID;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;


    @PostMapping(value = "api/user/login")
    public String login(
            @RequestParam("name")String userName,
            @RequestParam("password")String password,
            HttpSession session) throws Exception {
        SmartbusUser smartbusUser = userService.geUserByName(userName);
        if (smartbusUser != null && smartbusUser.getPassword().equals(password)) {
            int myId = smartbusUser.getId();
            session.setAttribute(USER_ID, myId);
            session.setAttribute(CAPACITY, smartbusUser.getCapacity());
            return String.valueOf(myId);
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

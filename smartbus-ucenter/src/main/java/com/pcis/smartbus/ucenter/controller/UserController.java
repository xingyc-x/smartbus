package com.pcis.smartbus.ucenter.controller;


import org.springframework.beans.factory.annotation.Autowired;
import com.pcis.smartbus.ucenter.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
//@MapperScan("om.pcis.smartbus.db.dao") //mapper扫描
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "api/user/login")
    public String login(@RequestParam("name")String name,
                            @RequestParam("password")String password,
                            HttpSession session) throws Exception {
        userService.register(name, password);
        return name;
    }

}

package com.pcis.smartbus.ucenter.controller;


import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.pcis.smartbus.ucenter.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
//@MapperScan("om.pcis.smartbus.db.dao") //mapper扫描
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    UserUtils userUtils;
    public static final String USER_ID = "userId";
    public static final String CAPACITY = "capacity";

    @PostMapping(value = "api/user/login")
    public String login(@RequestParam("name")String userName,
                            @RequestParam("password")String password,
                            HttpSession session) throws Exception {
        SmartbusUser smartbusUser = userService.geUserByName(userName);
        if (smartbusUser.getPassword().equals(password)) {
            session.setAttribute(USER_ID, smartbusUser.getId());
            session.setAttribute(CAPACITY, smartbusUser.getCapacity());
            return "OK";
        }
        return "You login fail.";
    }

    @GetMapping(value = "api/user/login_out")
    public String loginOut(HttpSession session) {
        session.removeAttribute(USER_ID);
        session.removeAttribute(CAPACITY);
        return "OK";
    }

    private String changeSortLabel(String sort) {
        String sortBy;
        switch (sort) {
            case "userId":
            default:
                sortBy = "id";
        }
        return sortBy;
    }

    private String packToJson(int pageNo, int numUser,List<SmartbusUser> smartbusUsers) {
        JSONObject object = new JSONObject();
        object.put("pager.pageNo", pageNo);
        object.put("pager.totalRows", numUser);
        List<JSONObject> jsonObjects = new ArrayList<>(50);
        for (SmartbusUser smartbusUser : smartbusUsers) {
            JSONObject temp = userUtils.getResponseUserInfoJson(smartbusUser);
            jsonObjects.add(temp);
        }
        object.put("rows", jsonObjects);
        return object.toJSONString();
    }

    @PostMapping(value = "api/user/get_user_info")
    public String getUserInfo(
            @RequestParam("pager.pageNo")int pageNo,
            @RequestParam("pager.pageSize")int pageSize,
            @RequestParam("sort")String sort,
            @RequestParam("direction")String direction,
            HttpSession session
    ) {

        String sortBy = changeSortLabel(sort);
        List<SmartbusUser> smartbusUsers = userService.getAPageUser(pageNo, pageSize, sortBy, direction);
        int numUser = userService.getUserNum();
        return packToJson(pageNo, numUser, smartbusUsers);

    }


    @PostMapping(value = "api/user/get_user_info_by_search")
    public String getUserInfoBySearch(
            @RequestParam("pager.pageNo")int pageNo,
            @RequestParam("pager.pageSize")int pageSize,
            @RequestParam("sort")String sort,
            @RequestParam("direction")String direction,
            @RequestParam("searchIf")int searchIf,
            @RequestParam("searchInput")String searchInput,
            HttpSession session
    ) {
        String sortBy = changeSortLabel(sort);
        List<SmartbusUser> smartbusUsers = userService.getAPageUserBySearch(pageNo, pageSize, sortBy, direction, searchIf, searchInput);
        int numUser = userService.getUserNumBySearch(searchIf, searchInput);
        return packToJson(pageNo, numUser, smartbusUsers);

    }



}

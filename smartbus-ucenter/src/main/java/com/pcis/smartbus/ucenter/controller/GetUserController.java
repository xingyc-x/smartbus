package com.pcis.smartbus.ucenter.controller;


import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.utils.UserUtils;
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
public class GetUserController {

    @Autowired
    private UserService userService;
    @Autowired
    UserUtils userUtils;


    private String changeSortLabel(String sort) {
        String sortBy;
        switch (sort) {
            case "userLoginName":
                sortBy = "user_name";
                break;
            case "userName":
                sortBy = "real_name";
                break;
            case "userOrganization":
                sortBy = "company_id";
                break;
            case "userTel":
                sortBy = "phone";
                break;
            case "userEmail":
                sortBy = "email";
                break;
            case "userLevel":
                sortBy = "capacity";
                break;
            case "userId":
            default:
                sortBy = "id";
        }
        return sortBy;
    }

    private int getPageStartNo(int pageNo, int pageSize) {
        if (pageNo == 0 ) {
            return 0;
        }
        int startNo = (pageNo - 1) * pageSize;
        return startNo;
    }

    private String packToJson(int pageNo, int numUser,List<SmartbusUser> smartbusUsers) {
        JSONObject object = new JSONObject();
        object.put("pager.pageNo", pageNo);
        object.put("pager.totalRows", numUser);
        if (smartbusUsers != null && smartbusUsers.size() != 0) {
            List<JSONObject> jsonObjects = new ArrayList<JSONObject>(smartbusUsers.size());
            for (SmartbusUser smartbusUser : smartbusUsers) {
                JSONObject temp = userUtils.getResponseUserInfoJson(smartbusUser, false, false);
                jsonObjects.add(temp);
            }
            object.put("rows", jsonObjects);
        } else {
            object.put("rows", new ArrayList<JSONObject>(1));
        }

        return object.toJSONString();
    }

    @PostMapping(value = "api/user/getUserInfo")
    public String getUserInfo(
            @RequestParam("pager.pageNo")int pageNo,
            @RequestParam("pager.pageSize")int pageSize,
            @RequestParam("sort")String sort,
            @RequestParam("direction")String direction
    ) {

        String sortBy = changeSortLabel(sort);
        int startNo = getPageStartNo(pageNo, pageSize);
        List<SmartbusUser> smartbusUsers = userService.getAPageUser(startNo, pageSize, sortBy, direction);
        int numUser = userService.getUserNum();
        return packToJson(pageNo, numUser, smartbusUsers);

    }


    @PostMapping(value = "api/user/getUserInfoBySearch")
    public String getUserInfoBySearch(
            @RequestParam("pager.pageNo")int pageNo,
            @RequestParam("pager.pageSize")int pageSize,
            @RequestParam("sort")String sort,
            @RequestParam("direction")String direction,
            @RequestParam("searchIf")int searchIf,
            @RequestParam("searchInput")String searchInput
    ) {

        String sortBy = changeSortLabel(sort);
        int startNo = getPageStartNo(pageNo, pageSize);
        List<SmartbusUser> smartbusUsers = userService.getAPageUserBySearch(startNo, pageSize, sortBy, direction, searchIf, searchInput);
        int numUser = userService.getUserNumBySearch(searchIf, searchInput);
        return packToJson(pageNo, numUser, smartbusUsers);

    }

    @GetMapping(value = "api/user/getMyId")
    public String getMyId(HttpSession session) {
        int myId = (int)session.getAttribute(Constant.USER_ID);
        return String.valueOf(myId);
    }

}

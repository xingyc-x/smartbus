package com.pcis.smartbus.ucenter.controller;


import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.utils.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.pcis.smartbus.ucenter.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
//@MapperScan("om.pcis.smartbus.db.dao") //mapper扫描
public class GetUserController {

    @Autowired
    private UserService userService;
    @Autowired
    UserUtils userUtils;

    private Logger logger = Logger.getLogger(GetUserController.class);


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
                JSONObject temp = userUtils.getResponseUserInfoJson(smartbusUser, false, false, false);
                jsonObjects.add(temp);
            }
            object.put("rows", jsonObjects);
        } else {
            object.put("rows", new ArrayList<JSONObject>(1));
        }

        return object.toJSONString();
    }

    // 非搜索方式获取用户列表
    public String getUserInfo(int pageNo, int pageSize, String sort, String direction, String companyIdString) {

        String sortBy = changeSortLabel(sort);
        int startNo = getPageStartNo(pageNo, pageSize);
        List<SmartbusUser> smartbusUsers = userService.getAPageUser(startNo, pageSize, sortBy, direction, companyIdString);
        int numUser = userService.getUserNum();
        return packToJson(pageNo, numUser, smartbusUsers);

    }

    // 使用搜索的方法获取用户列表，此处有SQL注入漏洞
    public String getUserInfoBySearch(int pageNo, int pageSize, String sort, String direction, int searchIf, String searchInput, String companyIdString) {
        String sortBy = changeSortLabel(sort);
        int startNo = getPageStartNo(pageNo, pageSize);
        List<SmartbusUser> smartbusUsers = userService.getAPageUserBySearch(startNo, pageSize, sortBy, direction, searchIf, searchInput, companyIdString);
        int numUser = userService.getUserNumBySearch(searchIf, searchInput);
        return packToJson(pageNo, numUser, smartbusUsers);

    }

    //获取用户公司id
    private String getCompanyIdString(HttpSession session) {
        int myId = (int) session.getAttribute(Constant.USER_ID);
        SmartbusUser smartbusUser = userService.getUserById(myId);
        if (smartbusUser == null) {
            return null;
        }
        int companyId = smartbusUser.getCompanyId();
        String companyIdString;
        if (companyId == Constant.WEITEN_ID) {
            companyIdString = "'%'";
        } else {
            companyIdString = "'" + companyId + "'";
        }
        return companyIdString;
    }

    // 获取用户列表,这里才是前端的入口
    @PostMapping(value = "api/user/getUserInfo2")
    public String getUserInfo2(
            @RequestParam("pager.pageNo")int pageNo,
            @RequestParam("pager.pageSize")int pageSize,
            @RequestParam("sort")String sort,
            @RequestParam("direction")String direction,
            HttpServletRequest request
    ) {
        int searchIf = 0;
        String searchInput = "";
        HttpSession session = request.getSession();
        String companyIdString = getCompanyIdString(session);
        if (companyIdString == null) {
            return "Error: without this user!";
        }
        try {
            searchIf = Integer.valueOf(request.getParameter("searchIf"));
            searchInput = request.getParameter("searchInput");
            //System.out.println(searchInput +"21"+ searchIf);
            return getUserInfoBySearch(pageNo, pageSize, sort, direction,searchIf,searchInput, companyIdString);

        } catch (Exception e) {
            //logger.error(e.getMessage());
            return getUserInfo(pageNo, pageSize, sort, direction, companyIdString);
        }
    }

    @GetMapping(value = "api/user/getMyId")
    public String getMyId(HttpSession session) {
        int myId = (int)session.getAttribute(Constant.USER_ID);
        return String.valueOf(myId);
    }

}

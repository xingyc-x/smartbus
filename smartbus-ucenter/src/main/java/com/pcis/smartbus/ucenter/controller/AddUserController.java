package com.pcis.smartbus.ucenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import com.pcis.smartbus.ucenter.service.UserService;
import com.pcis.smartbus.ucenter.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

import static com.pcis.smartbus.common.Constant.NO_PASS;
import static com.pcis.smartbus.common.Constant.PASS;


@RestController
public class AddUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserProjectRelationService userProjectRelationService;

    // S1
    @GetMapping(value = "api/user/validateUserName")
    public String validateUserName(
            @RequestParam("validateValue")String userName,
            @RequestParam("_")String unknownParam) {
        SmartbusUser smartbusUser = userService.geUserByName(userName);
        JSONObject jsonObject = new JSONObject();
        if (smartbusUser == null) {
            //此用户还没被注册
            jsonObject.put("valid", true);
        } else {
            //此用户已被注册
            jsonObject.put("valid", false);
        }
        JSONObject temp = new JSONObject();
        temp.put("validateResult", jsonObject);
        return temp.toJSONString();
    }

    //S2 (revise user S2 use this api either)
    @GetMapping(value = "api/user/getAvailableProject")
    public String getAvailableProject(HttpSession session) {
            int capacity = (int) session.getAttribute(Constant.CAPACITY);
            switch (capacity){
                case 0:
                    //return all project
                    break;
                case 1:
                case 2:
                    //return related project
                    break;
                case 3:
                default:
                    //


        }
        return "{\"treeNodes\":[{ \"id\":\"1\", \"parentId\":\"0\", \"name\":\"员工1\"},{ \"id\":\"2\", \"parentId\":\"0\", \"name\":\"员工2\"},{ \"id\":\"3\", \"parentId\":\"0\", \"name\":\"员工3\"},{ \"id\":\"4\", \"parentId\":\"0\", \"name\":\"员工4\"}]}";
    }

    //S3
    @PostMapping(value = "api/user/register")
    public String register(
            @RequestParam("sysUserId")String sysUserId,
            @RequestParam("userLoginName")String userLoginName,
            @RequestParam("userPassword")String userPassword,
            @RequestParam("userName")String userName,
            @RequestParam("userOrganization")int companyId,
            @RequestParam("userTel")String userTel,
            @RequestParam("userEmail")String userEmail,
            @RequestParam("userLevel")int userLevel,
            @RequestParam("userProject")String userProjects,
            HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        SmartbusUser operatedUser = new SmartbusUser();
        operatedUser.setUserName(userLoginName);
        operatedUser.setPassword(userPassword);
        operatedUser.setRealName(userName);
        operatedUser.setCompanyId(companyId);
        operatedUser.setPhone(userTel);
        operatedUser.setEmail(userEmail);
        operatedUser.setCapacity(userLevel);
        if (userUtils.validateAuthority(myId, myCapacity, operatedUser)) {
            //此用户权限足以添加该用户
            if (!userService.register(operatedUser)) {
                return NO_PASS;
            } else {
                //用户注册成功，添加关系
                int operatedUserId = userService.geUserByName(userLoginName).getId();
                for(String projectIdStr : userProjects.split(",")) {
                    int projectId = Integer.valueOf(projectIdStr);
                    if (!userProjectRelationService.addRelation(operatedUserId, projectId)) {
                        return NO_PASS;
                    }
                }
            }
            return PASS;

        }
        return NO_PASS;
    }

}

package com.pcis.smartbus.ucenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.dao.ProjectMapper;
import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import com.pcis.smartbus.ucenter.service.UserService;
import com.pcis.smartbus.ucenter.service.impl.UserServiceImpl;
import com.pcis.smartbus.ucenter.utils.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.pcis.smartbus.common.Constant.NO_PASS;
import static com.pcis.smartbus.common.Constant.PASS;

@RestController
public class ReviseUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserProjectRelationService userProjectRelationService;

    @Autowired
    private SmartbusUserMapper smartbusUserMapper;

    @Autowired
    private ProjectMapper projectMapper;

    private Logger logger = Logger.getLogger(ReviseUserController.class);

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
    String packAlarmWay(SmartbusUser user, Boolean isShowName) {
        if (user == null) {
            return null;
        }
        String temp = "";
        if (user.getAlarmMail()) {
            if (isShowName) {
                temp += "邮件";
            } else {
                temp += Constant.ALARM_EMAIL;
            }
        }
        if (user.getAlarmSms()) {
            if (!temp.equals("")){
                temp += ",";
            }
            if (isShowName) {
                temp += "短信";
            } else {
                temp += Constant.ALARM_SMS;
            }

        }
        return temp;
    }

    @PostMapping(value = "api/user/getOperatedUserInfo")
    public String getOperatedUserInfo(@RequestParam("userId")int operatedUserId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        SmartbusUser operatedUser = userService.getUserById(operatedUserId);
        if (userUtils.validateAuthority(myId, myCapacity, operatedUser)) {
            JSONObject jsonObject = userUtils.getResponseUserInfoJson(operatedUser, GIVE_PASSWORD, GIVE_LEVEL_NUM, true);
            List<ProjectUserRelation> relations = userProjectRelationService.selectByUserId(operatedUser.getId());
            String temp = new String();
            for (ProjectUserRelation relation : relations) {
                temp += relation.getProjectId() + ",";
            }
            if (temp.isEmpty()) {
                jsonObject.put("userProject","");
            } else {
                String temp2 = temp.substring(0, temp.length() - 1);
                jsonObject.put("userProject", temp2);
            }
            jsonObject.put("alarmWay", packAlarmWay(operatedUser, false));
            return jsonObject.toJSONString();
        } else {
            return "{}";
        }
    }


    //你这么把它写在这？不应该写在get user 吗
    //这个函数服务与user-detail界面，上个函数服务于user-revise界面
    //两种不太在于，前者多返回数字，而后者把数字改为对应意义的字符串
    //这就是后端工程师搞全栈的悲哀，总是想搞个胖服务器
    @PostMapping(value = "api/user/getOperatedUserInfo2")
    public String getOperatedUserInfo2(@RequestParam("userId")int operatedUserId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        SmartbusUser operatedUser = userService.getUserById(operatedUserId);
        if (userUtils.validateAuthority(myId, myCapacity, operatedUser)) {
            JSONObject jsonObject = userUtils.getResponseUserInfoJson(operatedUser, false, GIVE_LEVEL_NUM, false);
            List<ProjectUserRelation> relations = userProjectRelationService.selectByUserId(operatedUser.getId());
            String temp = new String();
            for (ProjectUserRelation relation : relations) {
                temp += projectMapper.selectByPrimaryKey(relation.getProjectId()).getOrder() + ",";
            }
            if (temp.isEmpty()) {
                jsonObject.put("userProject","");
            } else {
                String temp2 = temp.substring(0, temp.length() - 1);
                jsonObject.put("userProject", temp2);
            }
            jsonObject.put("alarmWay", packAlarmWay(operatedUser, true));
            return jsonObject.toJSONString();
        } else {
            return "{}";
        }
    }

    @PostMapping(value = "api/user/reviseUser")
    public String reviseUser(
        @RequestParam("userId")int operatedUserId,
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

        SmartbusUser operatedUser = userService.getUserById(operatedUserId);
        operatedUser.setUserName(userLoginName);
        operatedUser.setPassword(userPassword);
        operatedUser.setRealName(userName);
        operatedUser.setCompanyId(companyId);
        operatedUser.setPhone(userTel);
        operatedUser.setEmail(userEmail);
        operatedUser.setCapacity(userLevel);
        if (userUtils.validateAuthority(myId, myCapacity, operatedUser)) {
            //此用户权限足以添加该用户
            userService.reviseUser(operatedUser);

            //删除旧关系，添加新关系
            userProjectRelationService.deleteByUserId(operatedUserId);
            for(String projectIdStr : userProjects.split(",")) {
                try {
                    int projectId = Integer.valueOf(projectIdStr);
                    userProjectRelationService.addRelation(operatedUserId, projectId);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }

            }
            return PASS;
        }
        return NO_PASS;
    }

    //你怎么能把删除写在这里，应该另起文件
    @PostMapping(value = "api/user/deleteUser")
    public String deleteUser(@RequestParam("userId")int operatedUserId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        //System.out.println(operatedUserId);
        SmartbusUser operatedUser = userService.getUserById(operatedUserId);
        if (userUtils.validateAuthority(myId, myCapacity, operatedUser)) {
            smartbusUserMapper.deleteByPrimaryKey(operatedUserId);
            userProjectRelationService.deleteByUserId(operatedUserId);
            return PASS;
        } else {
            return NO_PASS;
        }
    }


}

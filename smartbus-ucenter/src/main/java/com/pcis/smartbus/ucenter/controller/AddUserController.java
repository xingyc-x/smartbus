package com.pcis.smartbus.ucenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import com.pcis.smartbus.ucenter.service.UserService;
import com.pcis.smartbus.ucenter.utils.MD5Util;
import com.pcis.smartbus.ucenter.utils.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

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

    private Logger logger = Logger.getLogger(ReviseUserController.class);

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

    //添加关系是否有成功的可能性

    private boolean addRelation(String userLoginName, String userProjects) {
        //用户注册成功，添加关系
        int operatedUserId = userService.geUserByName(userLoginName).getId();

        if (userProjects.equals("")){
            return true;
        }
        for(String projectIdStr : userProjects.split(",")) {
            try {
                int projectId = Integer.valueOf(projectIdStr);
                userProjectRelationService.addRelation(operatedUserId, projectId);
            } catch (Exception e) {
                logger.error(e.getMessage());
                //这里可能使得坏字符串部分关系被添加了
                return false;
            }
        }
        return true;
    }

    //第一：return语句并不是函数的最终出口，如果有finally语句，这在return之后还会执行finally（return的值会暂存在栈里面，等待finally执行后再返回）
    //第二：finally里面不建议放return语句，根据需要，return语句可以放在try和catch里面和函数的最后。可行的做法有四种：
    //你为什么要把上述东西放在这里
    //1表示邮件，2表示短信， 空表示两这都没有
    public boolean changeAlarmWay(String alarmWays, SmartbusUser user) {
        //System.out.println(alarmWays);
        if (user == null) {
            return false;
        }
        Boolean aMail = false;
        Boolean aSms = false;
        if (alarmWays.equals("")){
            user.setAlarmMail(aMail);
            user.setAlarmSms(aSms);
            return true;
        }
        //如果alarmWays = ""，需要的是将两种都有置之为false
        //而如果执行一下代码，则会运行到catch
        //应为""有一个字串叫""
        //所以for循环会执行一次，而""无法解析成数字
        for(String alarmWay : alarmWays.split(",")) {
            try {
                int temp = Integer.valueOf(alarmWay);
                if(temp == 1) {
                    aMail = true;
                } else if(temp == 2){
                    aSms = true;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }
        user.setAlarmMail(aMail);
        user.setAlarmSms(aSms);
        return true;
    }

    //S3
    //修改了此处companyId,原为int，现改为String,因为没有新建公司得界面
    @PostMapping(value = "api/user/register")
    public String register(
            @RequestParam("userLoginName")String userLoginName,
            @RequestParam("userPassword")String userPassword,
            @RequestParam("userName")String userName,
            @RequestParam("userOrganization")String companyId,//改1
            @RequestParam("userTel")String userTel,
            @RequestParam("userEmail")String userEmail,
            @RequestParam("userLevel")int userLevel,
            @RequestParam("userProject")String userProjects,
            @RequestParam("alarmWay")String alarmWays,
            HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        SmartbusUser operatedUser = new SmartbusUser();
        String salt = UUID.randomUUID().toString().substring(0, 5);
        operatedUser.setUserName(userLoginName);
        operatedUser.setSalt(salt);
        //加入加盐值，进行MD5运算
        operatedUser.setPassword(MD5Util.MD5(userPassword + salt));
        operatedUser.setRealName(userName);
        operatedUser.setCompanyId(1);
        operatedUser.setPhone(userTel);
        operatedUser.setEmail(userEmail);
        operatedUser.setCapacity(userLevel);
        JSONObject jsonObject = new JSONObject();
        //添加报警方式
        if (!changeAlarmWay(alarmWays, operatedUser)) {
            jsonObject.put("status", false);
            jsonObject.put("info", "添加用户失败！报警方式解析错误");
            return jsonObject.toJSONString();
        }
        //System.out.println(operatedUser);
        if (userUtils.validateAuthority(myId, myCapacity, operatedUser)) {
            //此用户权限足以添加该用户
            //添加成功，返回添加的行数，这里是1
            try {
                int temp = userService.register(operatedUser);
                //表示用户添加成功
                if (temp == 1) {
                    jsonObject.put("status", true);
                    //添加用户和项目的映射表，因为可能是n:n的关系
                    if (addRelation(userLoginName, userProjects)) {
                        jsonObject.put("info", "添加用户成功！");
                    } else {
                        jsonObject.put("info", "用户成功添加，用户与项目关系没有成功添加");
                    }
                } else {
                    jsonObject.put("status", false);
                    jsonObject.put("info", "用户添加失败，请检查手机号和邮箱以及用户名是否与他人重复");
                }
            } catch (Exception e) {
                //手机号和邮箱以及用户名重复时会运行至此
                jsonObject.put("status", false);
                jsonObject.put("info", "用户添加失败，请检查手机号和邮箱以及用户名是否与他人重复");
            }

        } else {
            jsonObject.put("status", false);
            jsonObject.put("info", "权限不足，用户添加失败！");
        }
        return jsonObject.toJSONString();
    }

}

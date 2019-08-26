package com.pcis.smartbus.pcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.pcenter.service.ProjectService;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import com.pcis.smartbus.utils.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pcis.smartbus.common.Constant.NO_PASS;
import static com.pcis.smartbus.common.Constant.PASS;

@RestController
public class ReviseProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserProjectRelationService userProjectRelationService;

    @Autowired
    RedisUtil redisUtil;
    //怎么能把删除写在这儿，不是应该另起文件吗
    @PostMapping(value = "api/project/deleteProject")
    public String deleteUser(@RequestParam("projectId")int projectId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        Boolean flag = projectService.validateUserToProject(myId, myCapacity, projectId);

        if ((myCapacity == Constant.WEITEN_SALESMAN || myCapacity == Constant.WEITEN_ADMIN) && flag) {
            if(projectService.deleteProjectById(projectId) == 1) {
                userProjectRelationService.deleteByProjectId(projectId);
                //此处在redis里面将project删除掉
                redisUtil.Delete("project:" + projectId);
                return PASS;
                


            } else {
                return NO_PASS;
            }
        } else {
            return NO_PASS;
        }
    }

    @PostMapping(value = "api/project/reviseProject")
    public String addProject(
            @RequestParam("id")int projectId,
            @RequestParam("order")String order,
            @RequestParam("company")int companyId,
            @RequestParam("location")String address,
            @RequestParam("info")String info,
            @RequestParam("lng")float lng,
            @RequestParam("lat")float lat,
            HttpSession session) {
        System.out.println(projectId + " " + order+ " " + companyId +" "+ address +" "+lng+" " + lat);
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        JSONObject jsonObject = new JSONObject();
        if (myCapacity == Constant.WEITEN_ADMIN || myCapacity == Constant.WEITEN_SALESMAN) {
            Project project = projectService.getProjectById(projectId);
            if (project == null) {
                jsonObject.put("status", false);
                jsonObject.put("info", "修改项目失败，该项目不存在！");
            }
            project.setOrder(order);
            project.setCompanyId(1);
            project.setLocation(address);
            project.setIntroduction(info);
            //LocalDateTime localDateTime = LocalDateTime.now();
            //project.setCreateTime(localDateTime);
            project.setLatitude(lat);
            project.setLongitude(lng);
            boolean temp = projectService.reviseProject(project);
            if (temp) {
                jsonObject.put("status", true);
                jsonObject.put("info", "修改项目成功！");
                //在redis里面修改project对象
                redisUtil.ComSet("project:"+projectId, project);
            } else {
                jsonObject.put("status", false);
                jsonObject.put("info", "修改项目失败！");
            }

        } else {
            jsonObject.put("status", false);
            jsonObject.put("info", "权限不足，项目修改失败！");
        }
        return jsonObject.toJSONString();
    }

}

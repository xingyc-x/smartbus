package com.pcis.smartbus.pcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.pcenter.service.ProjectService;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import com.pcis.smartbus.utils.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AddProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    UserProjectRelationService userProjectRelationService;

    @Autowired
    RedisUtil redisUtil;
    @GetMapping(value = "api/project/validateProjectOrder")
    public String validateProjectOrder(
            @RequestParam("validateValue")String order,
            @RequestParam("_")String unknownParam) {
        Project project = projectService.getProjectByOrder(order);
        JSONObject jsonObject = new JSONObject();
        if (project == null) {
            //此项目还没被注册
            jsonObject.put("valid", true);
        } else {
            //此项目已被注册
            jsonObject.put("valid", false);
        }
        JSONObject temp = new JSONObject();
        temp.put("validateResult", jsonObject);
        return temp.toJSONString();
    }

    //此处将companuId从int改为String,因为没有新建公司的选项
    @PostMapping(value = "api/project/addProject")
    public String addProject(
            @RequestParam("order")String order,
            @RequestParam("company")String companyId,
            @RequestParam("address")String address,
            @RequestParam("info")String info,
            @RequestParam("lng")float lng,
            @RequestParam("lat")float lat,
            HttpSession session) {
        //System.out.println(order+ " " + company +" "+ address +" "+lng+" " + lat);
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        JSONObject jsonObject = new JSONObject();
        if (myCapacity == Constant.WEITEN_ADMIN || myCapacity == Constant.WEITEN_SALESMAN) {
            Project project = new Project();
            project.setOrder(order); 
            project.setCompanyId(1);
            project.setLocation(address);
            project.setIntroduction(info);
            LocalDateTime localDateTime = LocalDateTime.now();
            project.setCreateTime(localDateTime);
            project.setLatitude(lat);
            project.setLongitude(lng);
            int temp = projectService.insert(project);
            if (temp == 1) {
                jsonObject.put("status", true);
                jsonObject.put("info", "添加项目成功！");
                
                //在redis中添加project
                redisUtil.ComSet("project:"+project.getId(), project);
            } else {
                jsonObject.put("status", false);
                jsonObject.put("info", "添加项目失败！");
            }

        } else {
            jsonObject.put("status", false);
            jsonObject.put("info", "权限不足，项目添加失败！");
        }
        return jsonObject.toJSONString();
    }


}

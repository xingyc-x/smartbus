package com.pcis.smartbus.pcenter.controller;


import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.common.Utils;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.pcenter.service.ProjectService;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import com.pcis.smartbus.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    UserProjectRelationService userProjectRelationService;
    @Autowired
    UserService userService;

    //S2 (revise user S2 use this api either)
    @GetMapping(value = "api/project/getAvailableProject")
    public String getAvailableProject(HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        List<Project> projects;
        if (myCapacity == Constant.WEITEN_ADMIN) {
            projects = projectService.getAllProject();
        } else {
            List<ProjectUserRelation> relations = userProjectRelationService.selectByUserId(myId);
            projects = new ArrayList<>(relations.size());
            for (ProjectUserRelation relation : relations) {
                Project project = projectService.getProjectById(relation.getProjectId());
                if (project != null) {
                    projects.add(project);
                }
            }
        }
        Map<Integer, String> map = new HashMap<>(projects.size());
        for (Project project : projects) {
            map.put(project.getId(), project.getOrder());
        }
        String temp = Utils.packTreeNodes(map).toJSONString();
        //System.out.println(temp);
        return temp;
    }

    // 验证用户是否有权限删除与添加项目
    @GetMapping(value = "api/project/validateUserAuthority")
    public String validateUserAuthority(HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        //int myId = (int) session.getAttribute(Constant.USER_ID);

//        List<ProjectUserRelation> relations = userProjectRelationService.selectByUserId(myId);
//        for (ProjectUserRelation relation : relations) {
//
//        }
        if (myCapacity == Constant.WEITEN_ADMIN || myCapacity == Constant.WEITEN_SALESMAN) {
            return Constant.PASS;
        } else {
            return Constant.NO_PASS;
        }
    }

    //获取用于项目编辑的信息
    @PostMapping(value = "api/project/getAProjectInfoForEdit")
    public String getAProjectInfoForEdit(@RequestParam("projectId")int projectId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        Boolean flag = projectService.validateUserToProject(myId, myCapacity, projectId);
        if ((myCapacity == Constant.WEITEN_SALESMAN ||
                myCapacity == Constant.WEITEN_ADMIN ) && flag) {
            Project project = projectService.getProjectById(projectId);
            JSONObject jsonObject = projectService.packResponseProjectInfo(project, true);
            jsonObject.put("info", project.getIntroduction());
            String temp = jsonObject.toJSONString();
            System.out.println(temp);
            return temp;
        } else {
            return "{}";
        }
    }

    // 获取项目详细信息
    @PostMapping(value = "api/project/getAProjectInfoForDetail")
    public String getAProjectInfoForDetail(@RequestParam("projectId")int projectId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        Boolean flag = projectService.validateUserToProject(myId, myCapacity, projectId);
        //System.out.println(flag);
        if (flag) {
            Project project = projectService.getProjectById(projectId);
            JSONObject jsonObject = projectService.packResponseProjectInfo(project, false);
            jsonObject.put("info", project.getIntroduction());
            return jsonObject.toJSONString();
        } else {
            return "{}";
        }
    }

    //for project list 获得项目列表，前端的入口
    /*这里需要注意数据库瓶颈问题
    对于获取数据库表中项目的数量、项目搜索操作无法使用索引增加检索速度
    这使得数据库存在极大的性瓶颈
    根据实际测试，当数据库表有数万条数据时，搜索操作耗时十秒以上
    */
    @PostMapping(value = "api/project/getProjectInfo")
    public String getProjectInfo(
            @RequestParam("pager.pageNo")int pageNo,
            @RequestParam("pager.pageSize")int pageSize,
            @RequestParam("sort")String sort,
            @RequestParam("direction")String direction,
            HttpServletRequest request
    ) {
        int searchIf = 0;
        String searchInput = "";
        int myId = (int)request.getSession().getAttribute(Constant.USER_ID);

        try {
            searchIf = Integer.valueOf(request.getParameter("searchIf"));
            searchInput = request.getParameter("searchInput");
            //System.out.println(searchInput +"21"+ searchIf);
            return projectService.getProjectInfoBySearch(myId, pageNo, pageSize, sort, direction, searchIf, searchInput);

        } catch (Exception e){
            //logger.error(e.getMessage());
            return projectService.getProjectInfo(myId, pageNo, pageSize, sort, direction);
        }
    }
}

package com.pcis.smartbus.pcenter.controller;


import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.common.Utils;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.pcenter.service.ProjectService;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
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

    @PostMapping(value = "api/project/getAProjectInfoForEdit")
    public String getAProjectInfoForEdit(@RequestParam("projectId")int projectId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        Boolean flag = projectService.validateUserToProject(myId, myCapacity, projectId);
        if ((myCapacity == Constant.WEITEN_SALESMAN || myCapacity == Constant.WEITEN_ADMIN) && flag) {
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

    @PostMapping(value = "api/project/getAProjectInfoForDetail")
    public String getAProjectInfoForDetail(@RequestParam("projectId")int projectId, HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);
        Boolean flag = projectService.validateUserToProject(myId, myCapacity, projectId);
        if ((myCapacity == Constant.WEITEN_SALESMAN || myCapacity == Constant.WEITEN_ADMIN) && flag) {
            Project project = projectService.getProjectById(projectId);
            JSONObject jsonObject = projectService.packResponseProjectInfo(project, false);
            jsonObject.put("info", project.getIntroduction());
            return jsonObject.toJSONString();
        } else {
            return "{}";
        }
    }

    //for project list
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

        } catch (Exception e) {
            //logger.error(e.getMessage());
            return projectService.getProjectInfo(myId, pageNo, pageSize, sort, direction);
        }
    }
}

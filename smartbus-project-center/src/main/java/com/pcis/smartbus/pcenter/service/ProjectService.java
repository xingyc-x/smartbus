package com.pcis.smartbus.pcenter.service;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.db.domain.Project;

import java.util.List;

public interface ProjectService {
    boolean addProject(Project project);
    Project getProjectById(int id);
    List<Project> getAllProject();
    Project getProjectByOrder(String order);
    JSONObject packResponseProjectInfo(Project project, boolean isShowCompanyId);
    String getProjectInfoBySearch(int userId, int pageNo, int pageSize, String sort, String direction,int searchIf, String searchInput);
    String getProjectInfo(int userId, int pageNo, int pageSize, String sort, String direction);

    int insert(Project project);

    int deleteProjectById(int projectId);

    Boolean validateUserToProject(int userId, int userCapacity,int projectId);

    boolean reviseProject(Project project);

}

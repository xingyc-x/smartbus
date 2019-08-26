package com.pcis.smartbus.db.dao;

import com.pcis.smartbus.db.domain.Project;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectManualMapper {

    List<Project> getAllProject();
    Project getProjectByOrder(String order);
    Project getProjectBy(String label, String content);

    List<Project> getAPageProject(int startNo, int pageSize, String sortBy, String direction);
    Integer getProjectNum();

    List<Project> getAPageProjectByOrderSearch(int startNo, int pageSize, String sortBy, String direction, String searchContent);
    List<Project> getAPageProjectByCompanySearch(int startNo, int pageSize, String sortBy, String direction, String searchContent);

    Integer getProjectNumByOrderSearch(String searchContent);
    Integer getProjectNumByCompanySearch(String searchContent);

    Integer deleteProjectById(int projectId);
    List<Project> getProjectsByUserIDSearch(int userID);
}

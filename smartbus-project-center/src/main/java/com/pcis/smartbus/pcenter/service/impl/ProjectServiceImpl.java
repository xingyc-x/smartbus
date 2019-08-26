package com.pcis.smartbus.pcenter.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.dao.ProjectManualMapper;
import com.pcis.smartbus.db.dao.ProjectMapper;
import com.pcis.smartbus.db.dao.ProjectUserRelationManualMapper;
import com.pcis.smartbus.db.domain.Company;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.pcenter.service.ProjectService;
import com.pcis.smartbus.ucenter.service.CompanyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ProjectManualMapper projectManualMapper;
    @Autowired
    CompanyService companyService;

    @Autowired
    ProjectUserRelationManualMapper projectUserRelationManualMapper;

    private Logger logger = Logger.getLogger(ProjectServiceImpl.class);

    @Override
    public boolean addProject(Project project) {
        //使用泛型来做！！！
        int num = projectMapper.insert(project);
        if (num > 0) return true;
        return false;
    }

    @Override
    public Project getProjectById(int id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Project> getAllProject() {
        return projectManualMapper.getAllProject();
    }

    @Override
    public Project getProjectByOrder(String order) {
        return projectManualMapper.getProjectByOrder(order);
    }

    @Override
    public JSONObject packResponseProjectInfo(Project project, boolean isShowCompanyId) {
        JSONObject object = new JSONObject();
        object.put("id", project.getId());
        object.put("order", project.getOrder());
        if (isShowCompanyId) {
            object.put("company", project.getCompanyId());
        } else {
            Company company = companyService.getCompanyById(project.getCompanyId());
            if (company != null) {
                object.put("company", company.getCompanyName());
            } else {
                object.put("company", "No this company.");
            }
        }
        object.put("create_time", project.getCreateTime());
        object.put("location", project.getLocation());
        object.put("lng", project.getLongitude());
        object.put("lat", project.getLatitude());
        return object;
    }

    private String changeSortLabel(String sort) {
        String sortBy;
        switch (sort) {
            case "company":
                sortBy = "company_id";
                break;
             //order 是sql关键字，作为字段名时要用``括起来
            case "order":
                sortBy = "`order`";
                break;
            case "location":
                sortBy = "location";
                break;
            case "create_time":
                sortBy = "create_time";
                break;
            case "id":
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
    private String packToJson(int pageNo, int numProject, List<Project> projects) {
        JSONObject object = new JSONObject();
        object.put("pager.pageNo", pageNo);
        object.put("pager.totalRows", numProject);
        if (projects != null && projects.size() != 0) {
            List<JSONObject> jsonObjects = new ArrayList<JSONObject>(projects.size());
            for (Project project : projects) {
                JSONObject temp = packResponseProjectInfo(project, false);
                jsonObjects.add(temp);
            }
            object.put("rows", jsonObjects);
        } else {
            object.put("rows", new ArrayList<JSONObject>(1));
        }
        return object.toJSONString();
    }

    @Override
    public String getProjectInfoBySearch(int userId, int pageNo, int pageSize, String sort, String direction, int searchIf, String searchInput) {
        String sortBy = changeSortLabel(sort);
        int startNo = getPageStartNo(pageNo, pageSize);
        if (searchIf == 1) {
            List<Project> projects = projectManualMapper.getAPageProjectByOrderSearch(startNo, pageSize, sortBy, direction,"%" + searchInput + "%");
            int numProject = projectManualMapper.getProjectNumByOrderSearch("%" + searchInput + "%");
            return packToJson(pageNo, numProject, projects);
        }
        if (searchIf == 2) {
            List<Project> projects = projectManualMapper.getAPageProjectByCompanySearch(startNo, pageSize, sortBy, direction,"%" + searchInput + "%");
            int numProject = projectManualMapper.getProjectNumByCompanySearch("%" + searchInput + "%");
            return packToJson(pageNo, numProject, projects);
        }
        return null;
    }

    @Override
    public String getProjectInfo(int userId, int pageNo, int pageSize, String sort, String direction) {
        String sortBy = changeSortLabel(sort);
        int startNo = getPageStartNo(pageNo, pageSize);
        List<Project> projects = projectManualMapper.getAPageProject(startNo, pageSize, sortBy, direction);
        int numProject = projectManualMapper.getProjectNum();
        return packToJson(pageNo, numProject, projects);
    }

    @Override
    public int insert(Project project) {
        return projectMapper.insert(project);

    }

    @Override
    public int deleteProjectById(int projectId) {
        return projectManualMapper.deleteProjectById(projectId);
    }

    @Override
    public Boolean validateUserToProject(int userId, int userCapacity, int projectId) {
        Boolean flag = false;
        if (userCapacity == Constant.WEITEN_ADMIN) {
            Project project = projectMapper.selectByPrimaryKey(projectId);
            if (project != null) {
                flag = true;
            }
        } else {
            List<ProjectUserRelation> relations = projectUserRelationManualMapper.selectByUserId(userId);
            for (ProjectUserRelation relation : relations) {
                Project project = getProjectById(relation.getProjectId());
                if (project != null && project.getId() == projectId) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean reviseProject(Project project) {
        try {
            projectMapper.updateByPrimaryKey(project);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;

        }

    }
}

package com.pcis.smartbus.ucenter.service;

import com.pcis.smartbus.db.domain.ProjectUserRelation;

import java.util.List;

public interface UserProjectRelationService {
    boolean addRelation(int userId, int projectId);
    List<ProjectUserRelation> selectByUserId(int userId);
    int deleteByUserId(int userId);
    int deleteByProjectId(int projectId);
}

package com.pcis.smartbus.db.dao;

import com.pcis.smartbus.db.domain.ProjectUserRelation;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectUserRelationManualMapper {
    List<ProjectUserRelation> selectByUserId(Integer userId);
    int deleteByUserId(Integer userId);
    int deleteByProjectId(Integer projectId);
}

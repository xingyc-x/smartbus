package com.pcis.smartbus.ucenter.service.impl;

import com.pcis.smartbus.db.dao.ProjectUserRelationManualMapper;
import com.pcis.smartbus.db.dao.ProjectUserRelationMapper;
import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class UserProjectRelationServiceImpl implements UserProjectRelationService {

    //private SqlSession session;

    @Autowired
    private ProjectUserRelationMapper projectUserRelationMapper;
    @Autowired
    private ProjectUserRelationManualMapper projectUserRelationManualMapper;
    private Logger logger =logger = Logger.getLogger(UserProjectRelationServiceImpl.class);

//    public UserProjectRelationServiceImpl(){
//        try {
//            String resource = "mybatis-config1.xml";
//            InputStream inputStream = Resources.getResourceAsStream(resource);
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//            session = sqlSessionFactory.openSession();
//            projectUserRelationMapper = session.getMapper(ProjectUserRelationMapper.class);
//            logger = Logger.getLogger(UserProjectRelationServiceImpl.class);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }

    @Override
    public boolean addRelation(int userId, int projectId) {
        ProjectUserRelation projectUserRelation = new ProjectUserRelation();
        projectUserRelation.setUserId(userId);
        projectUserRelation.setProjectId(projectId);
        int temp = projectUserRelationMapper.insert(projectUserRelation);
        //session.commit();
        //System.out.println(temp);
        if (temp > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<ProjectUserRelation> selectByUserId(int userId) {
        return projectUserRelationManualMapper.selectByUserId(userId);
    }

    @Override
    public int deleteByUserId(int userId) {
        return projectUserRelationManualMapper.deleteByUserId(userId);
    }

    @Override
    public int deleteByProjectId(int projectId) {
        return projectUserRelationManualMapper.deleteByProjectId(projectId);
    }

}

package com.pcis.smartbus;

import com.pcis.smartbus.db.dao.ProjectManualMapper;
import com.pcis.smartbus.db.dao.ProjectUserRelationManualMapper;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.ProjectUserRelation;
import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={UCenterApplication.class})
public class UserProjectRelationTest {
    @Autowired
    UserProjectRelationService userProjectRelationService;
    @Autowired
    ProjectUserRelationManualMapper projectUserRelationManualMapper;

    @Test
    public void addRelationTest() throws Exception {
        userProjectRelationService.addRelation(1,1);
    }

    @Test
    public void getRelationTest(){
        List<ProjectUserRelation> rs =  projectUserRelationManualMapper.selectByUserId(76);
        System.out.println(rs);
    }
}

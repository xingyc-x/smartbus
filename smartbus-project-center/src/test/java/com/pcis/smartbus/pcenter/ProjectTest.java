package com.pcis.smartbus.pcenter;

import com.pcis.smartbus.PCenterApplication;
import com.pcis.smartbus.db.dao.ProjectManualMapper;
import com.pcis.smartbus.db.dao.ProjectMapper;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.pcenter.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={PCenterApplication.class})
public class ProjectTest {
    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectManualMapper projectManualMapper;

    @Test
    public void addTest(){
        Project project = new Project();
        project.setOrder("3233");
        project.setLocation("南京");
        project.setCompanyId(2);
        LocalDateTime localDateTime = LocalDateTime.now();
        project.setCreateTime(localDateTime);
        project.setIntroduction("");
        projectService.addProject(project);
    }

    @Test
    public void getProjectByOrderTest() {
        //System.out.println(projectService.getProjectByOrder("11"));
        System.out.println(projectManualMapper.getProjectBy("`order`","11") );
    }
}

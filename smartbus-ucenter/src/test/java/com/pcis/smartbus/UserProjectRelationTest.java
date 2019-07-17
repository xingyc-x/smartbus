package com.pcis.smartbus;

import com.pcis.smartbus.ucenter.service.UserProjectRelationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={UCenterApplication.class})
public class UserProjectRelationTest {
    @Autowired
    UserProjectRelationService userProjectRelationService;

    @Test
    public void addRelationTest() throws Exception {
        userProjectRelationService.addRelation(1,1);
    }
}

package com.pcis.smartbus;

import com.pcis.smartbus.ucenter.service.CompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={UCenterApplication.class})
public class CompanyTest {
    @Autowired
    CompanyService companyService;

    @Test
    public void addCompanyTest() {
        boolean sign = companyService.addCompany("test company", "my com", "/1.png", "");

        System.out.println(sign);
    }
}

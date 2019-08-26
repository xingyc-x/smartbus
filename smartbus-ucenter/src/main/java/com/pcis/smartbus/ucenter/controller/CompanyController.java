package com.pcis.smartbus.ucenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.common.Utils;
import com.pcis.smartbus.db.domain.Company;
import com.pcis.smartbus.ucenter.service.CompanyService;
import com.pcis.smartbus.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/api/user/getAvailableCompany")
    public String getAvailableCompany(HttpSession session) {
        int myCapacity = (int) session.getAttribute(Constant.CAPACITY);
        int myId = (int) session.getAttribute(Constant.USER_ID);

        if (myCapacity == Constant.CUSTOMER_ADMIN || myCapacity == Constant.CUSTOMER_MAINTENANCE) {
            int myCompanyId = userService.getUserById(myId).getCompanyId();
            String companyName = companyService.getCompanyName(myCompanyId);
            Map<Integer, String> map = new HashMap<>(1);
            map.put(myCompanyId, companyName);
            JSONObject object = Utils.packTreeNodes(map);
            return object.toJSONString();
        } else if (myCapacity == Constant.WEITEN_ADMIN || myCapacity == Constant.WEITEN_SALESMAN) {
            List<Company> companies = companyService.getAllCompany();
            Map<Integer, String> map = new HashMap<>(companies.size());
            for(Company company : companies) {
                map.put(company.getId(), company.getCompanyName());
            }
            JSONObject object = Utils.packTreeNodes(map);
            return object.toJSONString();
        } else {
            return  Utils.packTreeNodes(new HashMap<>()).toJSONString();
        }
    }

}

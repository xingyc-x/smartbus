package com.pcis.smartbus.ucenter.service;

import com.pcis.smartbus.db.domain.Company;

import java.util.List;

public interface CompanyService {
    String getCompanyName(int id);
    Company getCompanyById(int id);
    boolean addCompany(String name, String shortName, String imgPath, String introduction);
    boolean addCompany(Company company);

    List<Company> getAllCompany();
}

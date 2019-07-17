package com.pcis.smartbus.db.dao;

import com.pcis.smartbus.db.domain.Company;

import java.util.List;

public interface CompanyManualMapper {
    //Here is wrote by hand.
    List<Company> getCompanyIdBySearch(String searchContent);
}

package com.pcis.smartbus.ucenter.service.impl;

import com.pcis.smartbus.db.dao.CompanyManualMapper;
import com.pcis.smartbus.db.dao.CompanyMapper;
import com.pcis.smartbus.db.domain.Company;
import com.pcis.smartbus.ucenter.service.CompanyService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.smartcardio.CommandAPDU;
import java.io.InputStream;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    //private SqlSession session;
    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyManualMapper companyManualMapper;
//    public CompanyServiceImpl(){
//        try {
//            String resource = "mybatis-config1.xml";
//            InputStream inputStream = Resources.getResourceAsStream(resource);
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//            session = sqlSessionFactory.openSession();
//            logger = Logger.getLogger(UserServiceImpl.class);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }

    public String getCompanyName(int id) {
        Company company = companyMapper.selectByPrimaryKey(id);
        if (company != null) {
            return company.getCompanyName();
        } else {
            return "null";
        }

    }

    @Override
    public Company getCompanyById(int id) {
        return companyMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean addCompany(String name, String shortName, String imgPath, String introduction) {
        Company company = new Company();
        company.setCompanyName(name);
        company.setShortName(shortName);
        company.setImgPath(imgPath);
        company.setCompanyInfo(introduction);
        return addCompany(company);
    }

    @Override
    public boolean addCompany(Company company) {
        //CompanyMapper companyMapper = session.getMapper(CompanyMapper.class);
        int num = companyMapper.insert(company);
        if (num > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Company> getAllCompany() {
        return companyManualMapper.getAllCompany();
    }


}

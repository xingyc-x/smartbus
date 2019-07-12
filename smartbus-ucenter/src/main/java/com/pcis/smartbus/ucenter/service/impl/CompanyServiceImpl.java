package com.pcis.smartbus.ucenter.service.impl;

import com.pcis.smartbus.db.dao.CompanyMapper;
import com.pcis.smartbus.db.domain.Company;
import com.pcis.smartbus.ucenter.service.CompanyService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.smartcardio.CommandAPDU;
import java.io.InputStream;

@Service
public class CompanyServiceImpl implements CompanyService {

    private SqlSession session;
    private Logger logger;

    public CompanyServiceImpl(){
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            logger = Logger.getLogger(UserServiceImpl.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public String getCompanyName(int id) {
        CompanyMapper companyMapper = session.getMapper(CompanyMapper.class);
        Company company = companyMapper.selectByPrimaryKey(id);
        if (company != null) {
            return company.getCompanyName();
        } else {
            return "null";
        }

    }


}

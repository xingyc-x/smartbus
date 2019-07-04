package com.pcis.smartbus.db.dao;

import com.pcis.smartbus.db.domain.OperationLog;
import com.pcis.smartbus.db.domain.OperationLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OperationLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    long countByExample(OperationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    int deleteByExample(OperationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    int insert(OperationLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    int insertSelective(OperationLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    OperationLog selectOneByExample(OperationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    OperationLog selectOneByExampleSelective(@Param("example") OperationLogExample example, @Param("selective") OperationLog.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    List<OperationLog> selectByExampleSelective(@Param("example") OperationLogExample example, @Param("selective") OperationLog.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    List<OperationLog> selectByExample(OperationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    OperationLog selectByPrimaryKeySelective(@Param("id") Long id, @Param("selective") OperationLog.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    OperationLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") OperationLog record, @Param("example") OperationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") OperationLog record, @Param("example") OperationLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(OperationLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table operation_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(OperationLog record);
}
package nivance.jpa.mysql.admin.mapper;

import java.util.List;

import nivance.jpa.mysql.entity.AdminOperator;
import nivance.jpa.mysql.entity.AdminOperatorExample;
import nivance.jpa.mysql.entity.AdminOperatorKey;
import nivance.jpa.mysql.jpa.iface.StaticTableDaoSupport;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface AdminOperatorMapper extends StaticTableDaoSupport<AdminOperator, AdminOperatorExample, AdminOperatorKey> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @SelectProvider(type=AdminOperatorSqlProvider.class, method="countByExample")
    int countByExample(AdminOperatorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @DeleteProvider(type=AdminOperatorSqlProvider.class, method="deleteByExample")
    int deleteByExample(AdminOperatorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @Delete({
        "delete from T_ADMIN_OPERATOR",
        "where USERNAME = #{username,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(AdminOperatorKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @Insert({
        "insert into T_ADMIN_OPERATOR (USERNAME, PASSWORD, ",
        "REALNAME, RETRY, ",
        "STATUS, CREATEDATE, ",
        "LASTDATE, ROLECODE)",
        "values (#{username,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, ",
        "#{realname,jdbcType=VARCHAR}, #{retry,jdbcType=DECIMAL}, ",
        "#{status,jdbcType=DECIMAL}, #{createdate,jdbcType=DECIMAL}, ",
        "#{lastdate,jdbcType=DECIMAL}, #{rolecode,jdbcType=VARCHAR})"
    })
    int insert(AdminOperator record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @InsertProvider(type=AdminOperatorSqlProvider.class, method="insertSelective")
    int insertSelective(AdminOperator record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @SelectProvider(type=AdminOperatorSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="USERNAME", property="username", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="PASSWORD", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="REALNAME", property="realname", jdbcType=JdbcType.VARCHAR),
        @Result(column="RETRY", property="retry", jdbcType=JdbcType.DECIMAL),
        @Result(column="STATUS", property="status", jdbcType=JdbcType.DECIMAL),
        @Result(column="CREATEDATE", property="createdate", jdbcType=JdbcType.DECIMAL),
        @Result(column="LASTDATE", property="lastdate", jdbcType=JdbcType.DECIMAL),
        @Result(column="ROLECODE", property="rolecode", jdbcType=JdbcType.VARCHAR)
    })
    List<AdminOperator> selectByExample(AdminOperatorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @Select({
        "select",
        "USERNAME, PASSWORD, REALNAME, RETRY, STATUS, CREATEDATE, LASTDATE, ROLECODE",
        "from T_ADMIN_OPERATOR",
        "where USERNAME = #{username,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="USERNAME", property="username", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="PASSWORD", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="REALNAME", property="realname", jdbcType=JdbcType.VARCHAR),
        @Result(column="RETRY", property="retry", jdbcType=JdbcType.DECIMAL),
        @Result(column="STATUS", property="status", jdbcType=JdbcType.DECIMAL),
        @Result(column="CREATEDATE", property="createdate", jdbcType=JdbcType.DECIMAL),
        @Result(column="LASTDATE", property="lastdate", jdbcType=JdbcType.DECIMAL),
        @Result(column="ROLECODE", property="rolecode", jdbcType=JdbcType.VARCHAR)
    })
    AdminOperator selectByPrimaryKey(AdminOperatorKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @UpdateProvider(type=AdminOperatorSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") AdminOperator record, @Param("example") AdminOperatorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @UpdateProvider(type=AdminOperatorSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") AdminOperator record, @Param("example") AdminOperatorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @UpdateProvider(type=AdminOperatorSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AdminOperator record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Wed Aug 27 15:23:08 CST 2014
     */
    @Update({
        "update T_ADMIN_OPERATOR",
        "set PASSWORD = #{password,jdbcType=VARCHAR},",
          "REALNAME = #{realname,jdbcType=VARCHAR},",
          "RETRY = #{retry,jdbcType=DECIMAL},",
          "STATUS = #{status,jdbcType=DECIMAL},",
          "CREATEDATE = #{createdate,jdbcType=DECIMAL},",
          "LASTDATE = #{lastdate,jdbcType=DECIMAL},",
          "ROLECODE = #{rolecode,jdbcType=VARCHAR}",
        "where USERNAME = #{username,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(AdminOperator record);
}
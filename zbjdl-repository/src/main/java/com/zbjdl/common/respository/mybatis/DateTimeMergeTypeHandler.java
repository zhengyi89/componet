package com.zbjdl.common.respository.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * <p>Title: 日期时间字段类型合并处理类</p>
 * <p>Description: 查询返回结果时用于将表中特定日期字段和时间字段，合并成一个Date对象返回<br/>
 * 	  如: 实体属性RegisterDate，对应表中两个字段REGISTER_DATE(DATE)和REGISTER_TIME(TIME)，<br/>
 *   查询返回结果时，要将表中两个字段的日期时间组合成Date对象返回设置到属性RegisterDate中
 * </p>
 */
public class DateTimeMergeTypeHandler implements TypeHandler {

	/* (non-Javadoc)
	 * @see org.apache.ibatis.type.TypeHandler#setParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
	 */ 
	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
		if(parameter != null) {
			ps.setObject(i, parameter);
		}
	}	

	/* (non-Javadoc)
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, java.lang.String)
	 */
	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		Date date = rs.getDate(columnName);
		Date time = rs.getTime(columnName.replace("_DATE", "_TIME"));
		if(time != null) {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(time);
			
			calendar1.set(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
			calendar1.set(Calendar.MINUTE, calendar2.get(Calendar.MINUTE));
			calendar1.set(Calendar.SECOND, calendar2.get(Calendar.SECOND));
			calendar1.set(Calendar.MILLISECOND, calendar2.get(Calendar.MILLISECOND));
			date = calendar1.getTime();
		}
		return date;
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.CallableStatement, int)
	 */
	@Override
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String columnName = cs.getParameterMetaData().getParameterClassName(columnIndex);
		return getResult(cs.getResultSet(), columnName);
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		ResultSetMetaData data=rs.getMetaData(); 
		String columnName = data.getColumnName(columnIndex);
		return getResult(rs, columnName);
	}

}

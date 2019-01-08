
package com.zbjdl.utils.query.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.utils.query.QueryException;


public class JdbcParamSetter {
	
	public static void setParams(PreparedStatement stmt, List sqlParams){
		if(CheckUtils.isEmpty(sqlParams)){
			return;
		}
		int index = 1;
		for(Object obj : sqlParams){
			CheckUtils.notNull(obj, "sqlParam");
			try {
				setParam(stmt, index++, obj);
			} catch (SQLException e) {
				throw new QueryException("sql exceptioni", e);
			}
		}
	}
	
	private static void setParam(PreparedStatement stmt, int index, Object obj) throws SQLException{
		if(obj instanceof String){
			stmt.setString(index, (String)obj);
		}
		else if(obj instanceof Integer){
			stmt.setInt(index, (Integer)obj);
		}
		else if(obj instanceof Long){
			stmt.setLong(index, (Long)obj);
		}
		else if(obj instanceof Double){
			stmt.setDouble(index, (Double)obj);
		}
		else if(obj instanceof Byte){
			stmt.setByte(index, (Byte)obj);
		}
		else if(obj instanceof Short){
			stmt.setShort(index, (Short)obj);
		}
		else if(obj instanceof Float){
			stmt.setFloat(index, (Float)obj);
		}
		else if(obj instanceof BigInteger){
			stmt.setBigDecimal(index,  new BigDecimal((BigInteger) obj));
		}
		else if(obj instanceof BigDecimal){
			stmt.setBigDecimal(index, (BigDecimal)obj);
		}
		else if(obj instanceof Boolean){
			stmt.setBoolean(index, (Boolean)obj);
		}
		else if(obj instanceof Character){
			stmt.setString(index, String.valueOf(obj));
		}
		else if(obj instanceof java.sql.Date){
			stmt.setDate(index, (java.sql.Date)obj);
		}
		else if(obj instanceof java.sql.Time){
			stmt.setTime(index, (java.sql.Time)obj);
		}
		else if(obj instanceof Timestamp){
			stmt.setTimestamp(index, (Timestamp)obj);
		}
		else if(obj instanceof java.util.Date){
			stmt.setTimestamp(index, new Timestamp(((java.util.Date)obj).getTime()));
		}
		else if(obj instanceof byte[]){
			stmt.setBytes(index, (byte[])obj);
		}
		else if(obj instanceof Blob){
			stmt.setBlob(index, (Blob)obj);
		}
		else if(obj instanceof Clob){
			stmt.setClob(index, (Clob)obj);
		}
		else if(obj instanceof Enum){
			stmt.setString(index, ((Enum)obj).name());
		}
		else{
			throw new QueryException("unsupport sql param type : " + obj.getClass());
		}
	}
}

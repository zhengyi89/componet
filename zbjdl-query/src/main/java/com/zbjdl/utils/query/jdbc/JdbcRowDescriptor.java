
package com.zbjdl.utils.query.jdbc;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zbjdl.utils.query.QueryException;

public class JdbcRowDescriptor {
	private static Logger logger = LoggerFactory.getLogger(JdbcRowDescriptor.class);
	private List<String> columnNames;
	private ResultSet resultSet;
	private Integer startIndex;
	private Integer maxSize;
	
	public JdbcRowDescriptor(ResultSet rs, Integer start, Integer size) throws SQLException{
		resultSet = rs;
		maxSize = size;
		startIndex = start;
		ResultSetMetaData metadata = resultSet.getMetaData();
		int n = metadata.getColumnCount();
		
		columnNames = new ArrayList<String>();
		for(int i=1; i<=n; i++){
			columnNames.add(metadata.getColumnLabel(i));
		}
	}
	
	private Map getResultObject(ResultSet rs) throws SQLException{
		Map map = new JdbcResultMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = columnNames.size(); i > 0; i--){
			//通过ResultSetMetaData类，可判断该列数据类型
            try {
				if(rsmd.getColumnTypeName(i).equals("BLOB")){
				    Blob bb = rs.getBlob(i);
				    byte[] b = bb.getBytes(1, (int)bb.length());
				    String value = new String(b,"utf-8");
				    map.put(columnNames.get(i-1), value);
				}else{
				    //不是则按原来逻辑运算
					map.put(columnNames.get(i-1), rs.getObject(i));
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(),e);
			}
			
	
		}
		return map;
	}
	
	public List getResult() throws SQLException{
		List result = new ArrayList();
		int size = 0;
		boolean flag = false;
		if(startIndex!=null){
			flag = resultSet.absolute(startIndex);
		}else{
			flag = resultSet.next();
		}
		while(flag){
			result.add(getResultObject(resultSet));
			size++;
			if(maxSize!=null && size>=maxSize){
				break;
			}
			flag = resultSet.next();
		}
		return result;
	}
	
	public Map getUniqueResult(boolean checkUnique) throws SQLException{
		Map result = null;
		if(resultSet.next()){
			result =  getResultObject(resultSet);
		}
		if(checkUnique && resultSet.next()){
			throw new QueryException("get none unique result!");
		}
		return result;
	}
}

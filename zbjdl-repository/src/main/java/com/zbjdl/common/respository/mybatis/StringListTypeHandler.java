package com.zbjdl.common.respository.mybatis;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * title: 将 String List 转为 用半角分号间隔的String<br/>
 * description: 描述<br/>
 * Copyright: Copyright (c)2014<br/>
 * Company: 云宝金服<br/>
 *
 */
public class StringListTypeHandler implements TypeHandler {

    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        if (o != null) {
            List<String> list = (List<String>) o;
            String s = StringUtils.join(list, ";");
            preparedStatement.setString(i, s);
        } else {
            preparedStatement.setNull(i, Types.VARCHAR);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getResult(ResultSet resultSet, String s) throws SQLException {
        String toSplitStr = resultSet.getString(s);
        if (toSplitStr == null) {
            return null;
        }

        return new ArrayList(Arrays.asList(StringUtils.split(toSplitStr, ";")));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        String toSplitStr = callableStatement.getString(i);
        if (toSplitStr == null) {
            return null;
        }

        return new ArrayList(Arrays.asList(StringUtils.split(toSplitStr, ";")));
    }

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        String toSplitStr = rs.getString(columnIndex);
        if (toSplitStr == null) {
            return null;
        }

        return new ArrayList(Arrays.asList(StringUtils.split(toSplitStr, ";")));
	}

}

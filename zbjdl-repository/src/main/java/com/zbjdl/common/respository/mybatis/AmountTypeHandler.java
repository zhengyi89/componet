package com.zbjdl.common.respository.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.zbjdl.common.amount.Amount;

import java.math.BigDecimal;
import java.sql.*;

/**
 * title: 金额的 TypeHandler<br/>
 * description: 描述<br/>
 */
public class AmountTypeHandler implements TypeHandler {

    @Override
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            Amount param = (Amount) parameter;
            ps.setBigDecimal(i, param.getValue());
        } else {
            ps.setNull(i, Types.DECIMAL);
        }
    }

    @Override
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        BigDecimal number = rs.getBigDecimal(columnName);
        if (number == null) {
            return null;
        } else {
            return new Amount(number);
        }
    }

    @Override
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        BigDecimal number = cs.getBigDecimal(columnIndex);
        if (number == null) {
            return null;
        }
        return new Amount(number);
    }

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        BigDecimal number = rs.getBigDecimal(columnIndex);
        if (number == null) {
            return null;
        } else {
            return new Amount(number);
        }		
	}

}

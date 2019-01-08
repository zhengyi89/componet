package com.zbjdl.common.utils.config.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ConvertBlobTypeHandler extends BaseTypeHandler {
    private static final String DEFAULT_CHARSET = "utf-8";

    public ConvertBlobTypeHandler() {
    }

    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1L, (int)blob.length());
        }

        try {
            return new String(returnValue, "utf-8");
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }

    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1L, (int)blob.length());
        }

        try {
            return new String(returnValue, "utf-8");
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }

    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        String paraStr = (String)parameter;

        ByteArrayInputStream bis;
        try {
            bis = new ByteArrayInputStream(paraStr.getBytes("utf-8"));
        } catch (UnsupportedEncodingException var8) {
            throw new RuntimeException("Blob Encoding Error!");
        }

        ps.setBinaryStream(i, bis, paraStr.length());
    }

    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Blob blob = rs.getBlob(columnIndex);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1L, (int)blob.length());
        }

        try {
            return new String(returnValue, "utf-8");
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }
}


package com.zbjdl.utils.query;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.zbjdl.common.utils.CheckUtils;


/**
 * 简单的SQL参数转化器
 */
public class SQLParamConverter {

	private final static long DAY_TIME = 3600000*24;
	private static String DATE_FORMAT_DATEONLY = "yyyy-MM-dd"; // 年/月/日
	private static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"; // 年/月/日


	public enum Type{
		BOOLEAN(new String[]{"boolean"}),
		STRING(new String[]{"string"}),
		INT(new String[]{"integer"}),
		LONG(new String[]{"long"}),
		DOUBLE(new String[]{"double"}),
		DATE(new String[]{"date","sqldate"}),
		NEXTDATE(new String[]{"nextdate"}),
		LASTDATE(new String[]{"lastdate"}),
		TIMESTAMP(new String[]{"timestamp"}),
		MAXTIMESTAMP(new String[]{"maxtimestamp"}),
		MINTIMESTAMP(new String[]{"mintimestamp"}),
		NEXTTIMESTAMP(new String[]{"nexttimestamp"}),
		DECIMAL(new String[]{"decimal"});
		private String[] names;
		private Type(String[] names){
			this.names = names;
		}
		
		public static Type parseType(String name){
			if(name!=null){
				name = name.toLowerCase();
			}
			for(Type type : Type.values()){
				for(String n : type.names){
					if(n.equals(name)){
						return type;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 将 value 转换为 toType 类型
	 *
	 * @param toType
	 * @param value
	 * @return
	 */
	public static Object convert(String toType, String value){
		Type type = Type.parseType(toType);
		if(type==null){
			throw new RuntimeException("unsupport type : "+toType);
		}
		return convert(type, value);
	}

	/**
	 * 将 value 转换为 toType 类型的数组
	 *
	 * @param toType
	 * @param value
	 * @return
	 */
	public static Object[] convertArray(String toType, String value){
		CheckUtils.notNull(toType, "toType");
		if(value==null){
			return null;
		}
		String[] values = value.split(",");
		Object[] result = new Object[values.length];

		Type type = Type.parseType(toType);
		if(type==null){
			throw new RuntimeException("unsupport type : "+toType);
		}
		for(int i=0; i<values.length; i++){
			result[i] = convert(type, values[i]);
		}
		return result;
	}

	/**
	 * 将 value 转换为 toType 类型
	 *
	 * @param toType
	 * @param value
	 * @return
	 */
	public static Object convert(Type toType, String value){
		CheckUtils.notNull(toType, "toType");
		if(Type.STRING.equals(toType)){
			return value;
		}
		if(CheckUtils.isEmpty(value)){
			return null;
		}
		switch(toType){
			case INT : return Integer.parseInt(value);
			case LONG : return Long.parseLong(value);
			case DOUBLE : return Double.parseDouble(value);
			case BOOLEAN : return Boolean.parseBoolean(value);
			case DECIMAL : return new BigDecimal(value);
			case DATE : {
				try {
					return new java.sql.Date(DateUtils.parseDate(value, new String[]{DATE_FORMAT_DATEONLY}).getTime());
				} catch (ParseException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
			case NEXTDATE : {
				try {
					return new java.sql.Date(DateUtils.parseDate(value, new String[]{DATE_FORMAT_DATEONLY}).getTime()+DAY_TIME);
				} catch (ParseException e) {
					throw new RuntimeException(e.getMessage(), e);
				} 
			}
			case LASTDATE : {
				try {
					return new java.sql.Date(DateUtils.parseDate(value, new String[]{DATE_FORMAT_DATEONLY}).getTime()-DAY_TIME);
				} catch (ParseException e) {
					throw new RuntimeException(e.getMessage(), e);
				} 
			}
			case TIMESTAMP : {
				try {
					return new java.sql.Timestamp(DateUtils.parseDate(value, new String[]{DATE_FORMAT_DATETIME, DATE_FORMAT_DATEONLY}).getTime());
				} catch (ParseException e) {
					throw new RuntimeException(e.getMessage(), e);
				} 
			}
			case MAXTIMESTAMP : {
				try {
					return new java.sql.Timestamp(getDayEnd(DateUtils.parseDate(value, new String[]{DATE_FORMAT_DATETIME, DATE_FORMAT_DATEONLY})).getTime());
				} catch (ParseException e) {
					throw new RuntimeException(e.getMessage(), e);
				} 
			}
			case MINTIMESTAMP : {
				try {
					return new java.sql.Timestamp(getDayStart(DateUtils.parseDate(value, new String[]{DATE_FORMAT_DATETIME, DATE_FORMAT_DATEONLY})).getTime());
				} catch (ParseException e) {
					throw new RuntimeException(e.getMessage(), e);
				} 
			}
			case NEXTTIMESTAMP : {
				try {
					return new java.sql.Timestamp(getDayStart(DateUtils.parseDate(value, new String[]{DATE_FORMAT_DATETIME, DATE_FORMAT_DATEONLY})).getTime()+DAY_TIME);
				} catch (ParseException e) {
					throw new RuntimeException(e.getMessage(), e);
				} 
			}
			default : throw new RuntimeException("unsupport type : "+toType);
		}
	}
	
	public static String convertToString(Object value){
		if(value==null){
			return null;
		}
		if(value instanceof java.sql.Date){
			return toString((java.sql.Date)value, DATE_FORMAT_DATEONLY);
		}
		if(value instanceof java.util.Date){
			return toString((java.util.Date)value, DATE_FORMAT_DATETIME);
		}
		return value.toString().trim();
	}
	private static String toString(java.util.Date dt, String sFmt) {
		if (dt == null || sFmt == null || "".equals(sFmt)) {
			return "";
		}
		return toString(dt, new SimpleDateFormat(sFmt));
	}

	private static String toString(java.util.Date dt, SimpleDateFormat formatter) {
		String sRet = null;

		try {
			sRet = formatter.format(dt).toString();
		} catch (Exception e) {
			e.printStackTrace();
			sRet = null;
		}

		return sRet;
	}	
	/**
	 * 得到day的终止时间点.
	 * 
	 * @param date
	 * @return
	 */
	private static Date getDayEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	/**
	 * 得到day的起始时间点。
	 * 
	 * @param date
	 * @return
	 */
	private static Date getDayStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}

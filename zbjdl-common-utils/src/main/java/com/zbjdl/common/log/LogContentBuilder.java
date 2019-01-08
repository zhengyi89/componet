package com.zbjdl.common.log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.zbjdl.common.annotation.LogColumn;
import com.zbjdl.common.annotation.LogColumn.SubType;
import com.zbjdl.common.utils.DateUtils;
import com.zbjdl.common.utils.StringUtils;


/**
 * 日志信息构造器
 *
 */
public class LogContentBuilder {

	/**
	 * 列名分隔符
	 */
	private static final String COLUMN_NAME_SEPARATOR = "_";

	/**
	 * 默认跳过的属性
	 */
	private static final String[] SKIP_FIELDS = { "serialVersionUID" };

	/**
	 * 构建日志数据表列-值Map<br>
	 * 如果有@LogColumn注解，则对象中无注解的字段将被跳过<br>
	 * 如果无@LogColumn注解，则对象中所有字段自动构造为数据库的列<br>
	 * 列名=java驼峰命名自动根据大写字符加下划线
	 */
	public static Map<String, Object> build(Object message) {
		Map<String, Object> columns = new LinkedHashMap<String, Object>();
		Class<?> clazz = message.getClass();
		boolean hasColumnAnnotation = hasColumnAnnotation(clazz);
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (isSkip(field.getName())) {
				continue;
			}
			LogColumn column = field.getAnnotation(LogColumn.class);
			if (column != null) {
				doCreateFieldMsg(field, columns, column, message);
			} else if (!hasColumnAnnotation) {
				String columnName = getColumnName(field.getName());
				doCreateFieldMsg(field, columns, columnName, message);
			}
		}
		return columns;
	}

	private static boolean hasColumnAnnotation(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			LogColumn column = field.getAnnotation(LogColumn.class);
			if (column != null) {
				return true;
			}
		}
		return false;
	}

	private static void doCreateFieldMsg(Field field,
			Map<String, Object> columns, LogColumn column, Object message) {
		try {
			String columnName = column.name().toUpperCase();
			field.setAccessible(true);
			Object value = getFieldValue(field, message);
			if (column.length() > 0 && value instanceof String) {
				String strVal = (String) value;
				if (column.subtype() == SubType.CHAR
						&& strVal.length() > column.length()) {
					value = strVal.substring(0, column.length());
				} else if (column.subtype() == SubType.BYTE) {
					value = StringUtils.substringByByte(strVal, 0,
							column.length());
				}
			}
			columns.put(columnName, value);
		} catch (Throwable e) {
		}
	}

	private static void doCreateFieldMsg(Field field,
			Map<String, Object> columns, String columnName, Object message) {
		try {
			field.setAccessible(true);
			columns.put(columnName, getFieldValue(field, message));
		} catch (Throwable e) {
		}
	}

	private static String getColumnName(String fieldName) {
		StringBuilder columnName = new StringBuilder();
		for (int i = 0; i < fieldName.length(); i++) {
			char c = fieldName.charAt(i);
			if (Character.isUpperCase(c)) {
				columnName.append(COLUMN_NAME_SEPARATOR);
			}
			columnName.append(c);
		}
		return columnName.toString().toUpperCase();
	}

	private static Object getFieldValue(Field field, Object message)
			throws IllegalArgumentException, IllegalAccessException,
			SQLException {
		Class<?> fieldType = field.getType();
		Object fieldValue = field.get(message);
		if (fieldValue == null) {
			return fieldValue;
		}
		if (fieldType.isArray()) {
			return getArrayFieldValue(fieldValue);
		}
		return getNormalFieldValue(fieldType, fieldValue);
	}

	private static String getBlobFieldValue(Object fieldValue)
			throws NumberFormatException, SQLException {
		Blob blob = (Blob) fieldValue;
		byte[] datas = blob.getBytes(0L,
				Integer.parseInt(String.valueOf(blob.length())));
		return new String(datas);
	}

	private static String getClobFieldValue(Object fieldValue)
			throws NumberFormatException, SQLException {
		Clob clob = (Clob) fieldValue;
		return clob.getSubString(0L,
				Integer.parseInt(String.valueOf(clob.length())));
	}

	private static Object getArrayFieldValue(Object fieldValue)
			throws NumberFormatException, SQLException {
		String result = null;
		StringBuffer sb = new StringBuffer();
		int length = Array.getLength(fieldValue);
		for (int i = 0; i < length; i++) {
			Object value = Array.get(fieldValue, i);
			Class<?> valueType = value.getClass();
			Object fvalue = getNormalFieldValue(valueType, value);
			sb.append(fvalue);
		}
		result = sb.toString();
		return result;
	}

	private static Object getNormalFieldValue(Class<?> fieldType,
			Object fieldValue) throws NumberFormatException, SQLException {
		if (fieldValue == null || fieldValue instanceof String) {
			return fieldValue;
		}
		if (fieldValue instanceof Enum) {
			return ((Enum<?>) fieldValue).name();
		}
		if (fieldValue instanceof Blob) {
			return getBlobFieldValue(fieldValue);
		}
		if (fieldValue instanceof Clob) {
			return getClobFieldValue(fieldValue);
		}
		if (fieldValue instanceof Date) {
			return getDateFieldValue(fieldValue);
		}
		if (fieldValue instanceof Timestamp) {
			return getTimestampFieldValue(fieldValue);
		}
		if (fieldValue instanceof Boolean) {
			return getBooleanFieldValue(fieldValue);
		}
		if (fieldType.getName().startsWith("com.zbjdl")) {
			return StringUtils.trim(ToStringBuilder
					.reflectionToString(fieldValue,
							ToStringStyle.SHORT_PREFIX_STYLE)
					.replace("\n", " ").replace("\t", "  "));
		}
		return fieldValue;
	}

	private static Object getDateFieldValue(Object fieldValue) {
		Date date = (Date) fieldValue;
		return DateUtils.toString(date, DateUtils.DATE_FORMAT_DATETIME);
	}

	private static Object getTimestampFieldValue(Object fieldValue) {
		Timestamp stamp = (Timestamp) fieldValue;
		return DateUtils.toSqlTimestampString(stamp,
				DateUtils.DATE_FORMAT_DATETIME);
	}

	private static Object getBooleanFieldValue(Object fieldValue) {
		Boolean b = (Boolean) fieldValue;
		return b ? 1 : 0;
	}

	private static Boolean isSkip(String fieldName) {
		for (String name : SKIP_FIELDS) {
			if (name.equalsIgnoreCase(fieldName)) {
				return true;
			}
		}
		return false;
	}
}

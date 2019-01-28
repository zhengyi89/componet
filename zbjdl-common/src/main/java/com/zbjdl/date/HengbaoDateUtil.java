package com.zbjdl.date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public class HengbaoDateUtil {
	/**
	 * 比较日期大小
	 * @param largeDt
	 * @param littleDt
	 * @return
	 */
	public static final int compareDateTime(Date largeDt, Date littleDt){
		return (new DateTime(largeDt)).toString("yyyyMMddHHmmss")
				.compareTo((new DateTime(littleDt)).toString("yyyyMMddHHmmss"));
	}
	
	/**
	 * 是否同一天
	 * @return
	 */
	public static final boolean isSameDay(Date date1, Date date2){
		return (new DateTime(date1)).toString("yyyyMMdd")
				.compareTo((new DateTime(date2)).toString("yyyyMMdd")) == 0;
	}
	
	/**
	 * 获取到分钟格式的日期字符串
	 * @param date
	 * @return
	 */
	public static final String getMinuteTime(Date date){
		return new DateTime(date).toString("yyyyMMddHHmm");
	}
	
	/**
	 * 获取日格式的日期字符串
	 * @param date
	 * @return
	 */
	public static final String getDayTime(Date date){
		return new DateTime(date).toString("yyyyMMdd");
	}
	
	/**
	 * 获取到秒格式的日期字符串
	 * @param date
	 * @return
	 */
	public static final String getSecondTime(Date date){
		return new DateTime(date).toString("yyyyMMddHHmmss");
	}
	
	/**
	 * 返回指定日期的零点整日期
	 * @param date
	 * @return
	 */
	public static final Date getDayZeroTime(Date date){
		DateTime dt = new DateTime(date);
		return (new DateTime(dt.getYear(), dt.getMonthOfYear(),dt.getDayOfMonth(), 0, 0, 0)).toDate();
	}
	
	/**
	 * 返回指定日期的23:59:59日期
	 * @param date
	 * @return
	 */
	public static final Date getDayFinalTime(Date date){
		DateTime dt = new DateTime(date);
		return (new DateTime(dt.getYear(), dt.getMonthOfYear(),dt.getDayOfMonth(), 23, 59, 59)).toDate();
	}
	
	/**
	 * 获取前一日的同一时间
	 * @param date
	 * @return
	 */
	public static final Date getLastDayTime(Date date){
		DateTime dt = new DateTime(date);
		return dt.minusDays(1).toDate();
	}
	
	/**
	 * 获取几分钟前的同一时间
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static final Date getPastMinutesTime(Date date, int minutes){
		DateTime dt = new DateTime(date);
		return dt.minusMinutes(minutes).toDate();
	}
	
	/**
	 * 当前时间加指定天数
	 * @param date
	 * @param days
	 * @return
	 */
	public static final Date plusDays(Date date, int days){
		DateTime dt = new DateTime(date);
		return dt.plusDays(days).toDate();
	}
	
	/**
	 * 当前时间加指定小时数
	 * @param date
	 * @param hours
	 * @return
	 */
	public static final Date plusHours(Date date, int hours){
		DateTime dt = new DateTime(date);
		return dt.plusHours(hours).toDate();
	}
	
	/**
	 * 当前时间加指定分钟数
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static final Date plusMinutes(Date date, int minutes){
		DateTime dt = new DateTime(date);
		return dt.plusMinutes(minutes).toDate();
	}

	/**
	 * 获取当月第一天日期
	 * @return
	 */
	public static final Date getTimesMonthmorning() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return HengbaoDateUtil.getDayZeroTime(c.getTime());
	}
	
	/**
	 * 获取当月最后一天日期
	 * @return
	 */
	public static final Date getTimesMonthNight() {
		Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		return HengbaoDateUtil.getDayFinalTime(ca.getTime());
	}
	
	/**
	 * 字符串转日期
	 * @param str
	 * @return
	 */
	public static Date StrToDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 字符串转日期   - 精确到秒
	 * @param str
	 * @return
	 */
	public static Date StrToDate_S(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取指定时间的那天 23:59:59.999 的时间
	 *
	 * @param date
	 * @return
	 */
	public static Date dayEnd(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	
	/**
	 * 获取****年**月**日**时**分**秒格式的日期字符串
	 * @param date
	 * @return
	 */
	public static String getTimeWithUnit(Date date){
		return new DateTime(date).toString("yyyy年MM月dd日HH时mm分ss秒");
	}
	
	public static String dateToSpecialFormatStr(Date date,String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	public static boolean isRightTime(Date star,Date end){
		boolean result = false;
	    try{
	        long time = System.currentTimeMillis();
	        if(time>=star.getTime()&& time<=end.getTime()){
	        	result = true ;
	        }
	    }catch(Exception e){
	    	
	    }
	    return result;
	}
}

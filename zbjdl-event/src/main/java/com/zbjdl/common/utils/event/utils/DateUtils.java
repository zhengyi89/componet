//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils extends org.apache.commons.lang.time.DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public static long ONE_DAY_SECONDS = 86400L;
    public static String shortFormat = "yyyyMMdd";
    public static String longFormat = "yyyyMMddHHmmss";
    public static String webFormat = "yyyy-MM-dd";
    public static String timeFormat = "HHmmss";
    public static String monthFormat = "yyyyMM";
    public static String chineseDtFormat = "yyyy年MM月dd日";
    public static String newFormat = "yyyy-MM-dd HH:mm:ss";
    public static String newFormat2 = "yyyy-MM-dd HH:mm";
    public static String newFormat3 = "yyyy-MM-dd HH";
    public static long ONE_DAY_MILL_SECONDS = 86400000L;

    public DateUtils() {
    }

    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df;
    }

    public static String format(Date date, String format) {
        return date == null ? null : (new SimpleDateFormat(format)).format(date);
    }

    public static String formatByLong(long date, String format) {
        return (new SimpleDateFormat(format)).format(new Date(date));
    }

    public static String formatByString(String date, String format) {
        return StringUtils.isNotBlank(date) ? (new SimpleDateFormat(format)).format(new Date(NumberUtils.toLong(date))) : "";
    }

    public static String formatShortFormat(Date date) {
        return date == null ? null : (new SimpleDateFormat(shortFormat)).format(date);
    }

    public static Date parseDateNoTime(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(shortFormat);
        if (sDate != null && sDate.length() >= shortFormat.length()) {
            if (!StringUtils.isNumeric(sDate)) {
                throw new ParseException("not all digit", 0);
            } else {
                return dateFormat.parse(sDate);
            }
        } else {
            throw new ParseException("length too little", 0);
        }
    }

    public static Date parseDateNoTime(String sDate, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("Null format. ", 0);
        } else {
            DateFormat dateFormat = new SimpleDateFormat(format);
            if (sDate != null && sDate.length() >= format.length()) {
                return dateFormat.parse(sDate);
            } else {
                throw new ParseException("length too little", 0);
            }
        }
    }

    public static Date parseDateNoTimeWithDelimit(String sDate, String delimit) throws ParseException {
        sDate = sDate.replaceAll(delimit, "");
        DateFormat dateFormat = new SimpleDateFormat(shortFormat);
        if (sDate != null && sDate.length() == shortFormat.length()) {
            return dateFormat.parse(sDate);
        } else {
            throw new ParseException("length not match", 0);
        }
    }

    public static Date parseDateLongFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(longFormat);
        Date d = null;
        if (sDate != null && sDate.length() == longFormat.length()) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException var4) {
                return null;
            }
        }

        return d;
    }

    public static Date parseDateNewFormat(String sDate) {
        Date d = parseDateHelp(sDate, newFormat);
        if (null != d) {
            return d;
        } else {
            d = parseDateHelp(sDate, newFormat2);
            if (null != d) {
                return d;
            } else {
                d = parseDateHelp(sDate, newFormat3);
                if (null != d) {
                    return d;
                } else {
                    d = parseDateHelp(sDate, webFormat);
                    if (null != d) {
                        return d;
                    } else {
                        try {
                            DateFormat dateFormat = new SimpleDateFormat(newFormat);
                            return dateFormat.parse(sDate);
                        } catch (ParseException var3) {
                            return null;
                        }
                    }
                }
            }
        }
    }

    private static Date parseDateHelp(String sDate, String format) {
        if (sDate != null && sDate.length() == format.length()) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.parse(sDate);
            } catch (ParseException var3) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Date addHours(Date date, long hours) {
        return addMinutes(date, hours * 60L);
    }

    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, minutes * 60L);
    }

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + secs * 1000L);
    }

    public static boolean isValidHour(String hourStr) {
        if (!StringUtils.isEmpty(hourStr) && StringUtils.isNumeric(hourStr)) {
            int hour = (new Integer(hourStr)).intValue();
            if (hour >= 0 && hour <= 23) {
                return true;
            }
        }

        return false;
    }

    public static boolean isValidMinuteOrSecond(String str) {
        if (!StringUtils.isEmpty(str) && StringUtils.isNumeric(str)) {
            int hour = (new Integer(str)).intValue();
            if (hour >= 0 && hour <= 59) {
                return true;
            }
        }

        return false;
    }

    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, days * ONE_DAY_SECONDS);
    }

    public static Date addDaysFromNow(long days) {
        return addSeconds(new Date(System.currentTimeMillis()), days * ONE_DAY_SECONDS);
    }

    public static String getTomorrowDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);
        aDate = addSeconds(aDate, ONE_DAY_SECONDS);
        return getDateString(aDate);
    }

    public static String getLongDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(longFormat);
        return getDateString(date, dateFormat);
    }

    public static String getNewFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(newFormat);
        return getDateString(date, dateFormat);
    }

    public static String getDateString(Date date, DateFormat dateFormat) {
        return date != null && dateFormat != null ? dateFormat.format(date) : null;
    }

    public static String getYesterDayDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);
        aDate = addSeconds(aDate, -ONE_DAY_SECONDS);
        return getDateString(aDate);
    }

    public static String getDateString(Date date) {
        DateFormat df = getNewDateFormat(shortFormat);
        return df.format(date);
    }

    public static String getWebDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(webFormat);
        return getDateString(date, dateFormat);
    }

    public static String getChineseDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(chineseDtFormat);
        return getDateString(date, dateFormat);
    }

    public static String getTodayString() {
        DateFormat dateFormat = getNewDateFormat(shortFormat);
        return getDateString(new Date(), dateFormat);
    }

    public static String getTimeString(Date date) {
        DateFormat dateFormat = getNewDateFormat(timeFormat);
        return getDateString(date, dateFormat);
    }

    public static String getBeforeDayString(int days) {
        Date date = new Date(System.currentTimeMillis() - ONE_DAY_MILL_SECONDS * (long)days);
        DateFormat dateFormat = getNewDateFormat(shortFormat);
        return getDateString(date, dateFormat);
    }

    public static long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000L;
    }

    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 60000L;
    }

    public static long getDiffDays(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 86400000L;
    }

    public static String getBeforeDayString(String dateString, int days) {
        DateFormat df = getNewDateFormat(shortFormat);

        Date date;
        try {
            date = df.parse(dateString);
        } catch (ParseException var5) {
            date = new Date();
        }

        date = new Date(date.getTime() - ONE_DAY_MILL_SECONDS * (long)days);
        return df.format(date);
    }

    public static boolean isValidShortDateFormat(String strDate) {
        if (strDate.length() != shortFormat.length()) {
            return false;
        } else {
            try {
                Integer.parseInt(strDate);
            } catch (Exception var4) {
                return false;
            }

            DateFormat df = getNewDateFormat(shortFormat);

            try {
                df.parse(strDate);
                return true;
            } catch (ParseException var3) {
                return false;
            }
        }
    }

    public static boolean isValidShortDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");
        return isValidShortDateFormat(temp);
    }

    public static boolean isValidLongDateFormat(String strDate) {
        if (strDate.length() != longFormat.length()) {
            return false;
        } else {
            try {
                Long.parseLong(strDate);
            } catch (Exception var4) {
                return false;
            }

            DateFormat df = getNewDateFormat(longFormat);

            try {
                df.parse(strDate);
                return true;
            } catch (ParseException var3) {
                return false;
            }
        }
    }

    public static boolean isValidLongDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");
        return isValidLongDateFormat(temp);
    }

    public static String getShortDateString(String strDate) {
        return getShortDateString(strDate, "-|/");
    }

    public static String getShortDateString(String strDate, String delimiter) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        } else {
            String temp = strDate.replaceAll(delimiter, "");
            return isValidShortDateFormat(temp) ? temp : null;
        }
    }

    public static String getShortFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();
        cal.setTime(dt);
        cal.set(5, 1);
        DateFormat df = getNewDateFormat(shortFormat);
        return df.format(cal.getTime());
    }

    public static String getWebTodayString() {
        DateFormat df = getNewDateFormat(webFormat);
        return df.format(new Date());
    }

    public static String getWebFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();
        cal.setTime(dt);
        cal.set(5, 1);
        DateFormat df = getNewDateFormat(webFormat);
        return df.format(cal.getTime());
    }

    public static String convert(String dateString, DateFormat formatIn, DateFormat formatOut) {
        try {
            Date date = formatIn.parse(dateString);
            return formatOut.format(date);
        } catch (ParseException var4) {
            logger.warn("convert() --- orign date error: " + dateString);
            return "";
        }
    }

    public static String convert2WebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(webFormat);
        return convert(dateString, df1, df2);
    }

    public static String convert2ChineseDtFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(chineseDtFormat);
        return convert(dateString, df1, df2);
    }

    public static String convertFromWebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(webFormat);
        return convert(dateString, df2, df1);
    }

    public static boolean webDateNotLessThan(String date1, String date2) {
        DateFormat df = getNewDateFormat(webFormat);
        return dateNotLessThan(date1, date2, df);
    }

    public static boolean dateNotLessThan(String date1, String date2, DateFormat format) {
        try {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);
            return !d1.before(d2);
        } catch (ParseException var5) {
            logger.debug("dateNotLessThan() --- ParseException(" + date1 + ", " + date2 + ")");
            return false;
        }
    }

    public static String getEmailDate(Date today) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        String todayStr = sdf.format(today);
        return todayStr;
    }

    public static String getSmsDate(Date today) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");
        String todayStr = sdf.format(today);
        return todayStr;
    }

    public static String formatTimeRange(Date startDate, Date endDate, String format) {
        if (endDate != null && startDate != null) {
            String rt = null;
            long range = endDate.getTime() - startDate.getTime();
            long day = range / 86400000L;
            long hour = range % 86400000L / 3600000L;
            long minute = range % 3600000L / 60000L;
            if (range < 0L) {
                day = 0L;
                hour = 0L;
                minute = 0L;
            }

            rt = format.replaceAll("dd", String.valueOf(day));
            rt = rt.replaceAll("hh", String.valueOf(hour));
            rt = rt.replaceAll("mm", String.valueOf(minute));
            return rt;
        } else {
            return null;
        }
    }

    public static String formatMonth(Date date) {
        return date == null ? null : (new SimpleDateFormat(monthFormat)).format(date);
    }

    public static Date getBeforeDate() {
        Date date = new Date();
        return new Date(date.getTime() - ONE_DAY_MILL_SECONDS);
    }

    public static String currentTime(String format) {
        return StringUtils.isBlank(format) ? format(new Date(), newFormat) : format(new Date(), format);
    }

    public static boolean isValidDateRange(Date startDate, Date endDate, boolean equalOK) {
        if (startDate != null && endDate != null) {
            if (equalOK && startDate.equals(endDate)) {
                return true;
            } else {
                return endDate.after(startDate);
            }
        } else {
            return false;
        }
    }

    public static boolean isToday(long time) {
        return isSameDay(new Date(time), new Date());
    }

    public static boolean isYesterday(long time) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(System.currentTimeMillis());
        return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) + 1 == cal2.get(6);
    }

    public static boolean isTomorrow(long time) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(System.currentTimeMillis());
        return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) - 1 == cal2.get(6);
    }

    public static boolean compareWithNow(long time, long interval) {
        return System.currentTimeMillis() - time > interval;
    }

    public static long getDiffDaysWithNow(long target) {
        long t1 = target - System.currentTimeMillis();
        return t1 < 0L ? -1L : t1 / 86400000L;
    }

    public static long getPastDaysWithNow(long target) {
        long t1 = System.currentTimeMillis() - target;
        return t1 < 0L ? -1L : t1 / 86400000L;
    }

    public static String getDynamicLeftTime(long target) {
        long t1 = target - System.currentTimeMillis();
        if (t1 < 0L) {
            return "0";
        } else {
            long days = t1 / 86400000L;
            if (days > 0L) {
                return days + "天";
            } else {
                long hours = t1 / 3600000L;
                if (hours > 0L) {
                    return hours + "小时";
                } else {
                    long minutes = t1 / 60000L;
                    if (minutes > 0L) {
                        return minutes + "分钟";
                    } else {
                        long seconds = t1 / 1000L;
                        return seconds > 0L ? seconds + "秒" : "0";
                    }
                }
            }
        }
    }
}

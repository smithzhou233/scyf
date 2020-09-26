package com.hngf.api.common.utils;
import cn.hutool.core.date.DateUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
public class DateUtils {
    public static final String DATE_JFP_STR = "yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
    public static String DEFAULT_FORMAT = "yyyy-MM-dd";

    public DateUtils() {
    }

    public static String getNowTime(String format) {
        return DateUtil.format(DateUtil.date(), format);
    }

    public static String getTime() {
        return DateUtil.format(DateUtil.date(), "HH:mm:ss");
    }

    public static String getDate() {
        return DateUtil.format(DateUtil.date(), DEFAULT_FORMAT);
    }

    public static String getDateTime() {
        return DateUtil.now();
    }

    public static String dateFormat(Date date, String format) {
        return date == null ? "" : DateUtil.format(date, format);
    }

    public static boolean checkDate(String vl) {
        try {
            DateFormat format = DateFormat.getDateInstance();
            format.parse(vl);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static String getDateTimeStrByTimeMillis(long timeMillis) {
        Date date = new Date(timeMillis);
        return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static long getTimestampByDateTimeStr(String dateTimeStr) {
        Date date = DateUtil.parse(dateTimeStr, "yyyy-MM-dd HH:mm:ss");
        return date.getTime();
    }

    public static List<String> getTimeLine(int beforeDay, int afterDay) {
        List<String> list = new ArrayList();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();

        for(int i = beforeDay; i > 0; --i) {
            calendar.setTime(now);
            calendar.add(5, -i);
            Date date = calendar.getTime();
            String timeStr = DateUtil.format(date, DEFAULT_FORMAT);
            list.add(timeStr);
        }

        String nowStr = DateUtil.today();
        list.add(nowStr);

        for(int j = 1; j < afterDay; ++j) {
            calendar.setTime(now);
            calendar.add(5, j);
            Date date = calendar.getTime();
            String timeStr = DateUtil.format(date, DEFAULT_FORMAT);
            list.add(timeStr);
        }

        return list;
    }

    public static String getBeforeDay(int dayNum) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(5, -dayNum);
        return DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
    }

    public static String getAfterDay(int dayNum) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(5, dayNum);
        return DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
    }

    public static String formatDate(Date date) {
        return DateUtil.format(date, DEFAULT_FORMAT);
    }

    public static String formatDateS(Date date) {
        return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(1);
        return getYearFirst(currentYear);
    }

    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(1);
        return getYearLast(currentYear);
    }

    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1, year);
        calendar.roll(6, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    public static Date getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        cal.set(5, cal.getActualMaximum(5));
        return cal.getTime();
    }

    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        cal.set(5, cal.getMinimum(5));
        return cal.getTime();
    }

    public static Date getLastDayOfWeek(int year, int week) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(3, week);
        cal.setFirstDayOfWeek(2);
        cal.set(7, cal.getFirstDayOfWeek() + 6);
        return cal.getTime();
    }

    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(3, week);
        cal.set(7, 2);
        return cal.getTime();
    }

    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(2);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(3);
    }

    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, 11, 31, 23, 59, 59);
        return getWeekOfYear(c.getTime());
    }

    public static int getDayNumOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        cal.set(5, cal.getMinimum(5));
        return cal.getActualMaximum(5);
    }

    public static List<Date> getDaysOfMonth(int year, int month) {
        List<Date> list = new ArrayList();
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        cal.set(5, cal.getMinimum(5));
        int maxDay = cal.getActualMaximum(5);
        int minDay = cal.getActualMinimum(5);

        for(int i = minDay; i <= maxDay; ++i) {
            cal.set(year, month - 1, i);
            list.add(cal.getTime());
        }

        return list;
    }

    public static int getDayNumOfYear(int year) {
        Calendar d = Calendar.getInstance();
        d.set(1, year);
        return d.getActualMaximum(6);
    }

    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(11, 0);
        todayStart.set(12, 0);
        todayStart.set(13, 0);
        todayStart.set(14, 0);
        return todayStart.getTime();
    }

    public static Date getDayStartTime(Date startTime) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(startTime);
        todayStart.set(11, 0);
        todayStart.set(12, 0);
        todayStart.set(13, 0);
        todayStart.set(14, 0);
        return todayStart.getTime();
    }

    public static Date getnowEndTime(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(11, 23);
        todayEnd.set(12, 59);
        todayEnd.set(13, 59);
        todayEnd.set(14, 999);
        return todayEnd.getTime();
    }

    public static Date toDate(String source, String dateFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.parse(source);
    }

    public static LocalDateTime toLocalDateTime(String source, String dateFormat) throws ParseException {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDateTime.parse(source, dtf2);
    }

    public static String dateToStr(Date source, String dateFormat) {
        return DateUtil.format(source, dateFormat);
    }

    public static Date strToDate(String source, String dateFormat) throws Exception {
        return DateUtil.parse(source, dateFormat);
    }

    public static Integer dateReYear(Date ago, Date rear) {
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(ago);
        aft.setTime(rear);
        int surplus = aft.get(5) - bef.get(5);
        int result = aft.get(2) - bef.get(2);
        int year = aft.get(1) - bef.get(1);
        if (result < 0) {
            result = 1;
        } else if (result == 0) {
            result = surplus <= 0 ? 1 : 0;
        } else {
            result = 0;
        }

        return Math.abs(year) + result;
    }
}

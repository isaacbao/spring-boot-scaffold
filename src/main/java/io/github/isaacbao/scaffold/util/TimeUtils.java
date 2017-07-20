package io.github.isaacbao.scaffold.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 */
public class TimeUtils {
    private static Logger logger = LogManager.getLogger();

    public static final String BASE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String TILL_NOW = "至今";
    public static final String END_OF_DAY = "end";
    public static final String START_OF_DAY = "start";

    public static Date getDate(String date) {

        if (date == null || date.equals(TILL_NOW) || StringUtil.isEmpty(date))
            return null;
        if(StringUtil.isDigit(date)){
            return new Date(Long.parseLong(date));
        }
        if (date.length() >= 18) {
            try {
                int year = Integer.parseInt(date.substring(0, 4));
                int month = Integer.parseInt(date.substring(5, 7));
                int dat = Integer.parseInt(date.substring(8, 10));
                int hour = Integer.parseInt(date.substring(11, 13));
                int min = Integer.parseInt(date.substring(14, 16));
                int second = Integer.parseInt(date.substring(17, 19));
                return new Date(year - 1900, month - 1, dat, hour, min, second);
            } catch (NumberFormatException e) {
                logger.error("",e);
                return null;
            }
        } else {
            String year = RegexUtils.getFirstMatch("\\d{4}\\D?", date);
            if (year == null)
                return null;
            date = date.substring(year.length());
            year = year.substring(0, 4);
            List<String> timeNums = RegexUtils.getMatch("\\d{1,2}", date);
            int len = timeNums.size();
            String[] nums = {"1", "1", "0", "0", "0"};
            for (int i = 0; i < len && i < 5; i++) {
                String str = timeNums.get(i);
                //str = str.substring(1);
                nums[i] = str;
            }
            LocalDateTime ldt = LocalDateTime.of(parseInt(year), parseInt(nums[0]), parseInt(nums[1]), parseInt(nums[2]), parseInt(nums[3]), parseInt(nums[4]));
            return Date.from(ldt.toInstant(ZoneOffset.ofHours(8)));
        }
    }

    private static int parseInt(String num) {
        if (num == null)
            return 0;
        else
            return Integer.parseInt(num);
    }

    public static Date parseDate(String time) {
        return parseDate(time, BASE_PATTERN);
    }

    public static Date parseDate(String time, String pattern) throws IllegalArgumentException {
        try {
            if (time == null)
                return null;
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("时间格式不合法");
        }
    }

    public static String getTimeInDailyStyle(int time) {
        try {
            time = time / 1000;
            int second = time % 60;
            int mins = time / 60;
            int min = mins % 60;
            int hour = mins / 60;
            String hourStr = (hour == 0 ? "" : hour + "时");
            String minStr = (min == 0 ? "" : min + "分");
            return hourStr + minStr + second + "秒";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTimeBySecond(int time) {
        try {
            time = time / 1000;
            return time + "秒";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formateDate(Date date) {
        return formateDate(date, BASE_PATTERN);
    }

    public static String formateDate(Date date, String pattern) {
        if (date == null)
            return null;

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }


    public static String dateCaculate(String date, int days) {
        String str = date;
        SimpleDateFormat formatter = new SimpleDateFormat(BASE_PATTERN);
        try {
            Date myDate = formatter.parse(str);
            return dateCaculate(myDate, days);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }


    public static String dateCaculate(Date date, int days) {
        SimpleDateFormat formatter = new SimpleDateFormat(BASE_PATTERN);
        try {
            date = caculateDate(date, days);
            return formatter.format(date);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public static Date caculateDate(Date date, int days) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, days);
            return c.getTime();
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * 根据日期类型计算时间
     *
     * @param date  原时间
     * @param field 日期类型（周年月日）（必须使用Calendar里定义的常量）
     * @param value 要增减的值
     */
    public static Date caculateDateByDateType(Date date, int field, int value) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(field, value);
            return c.getTime();
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * 以 BASE_PATTERN 格式还回当前日期字符串
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(BASE_PATTERN);
        return formatter.format(new Date());
    }

    public static String formateEndTimeString(String time) {
        return formateTimeString(time, "end");
    }

    public static String formateTimeString(String time, String type) {
        if (time == null || time.length() < 10) {
            return "";
        }
        if (time.length() < 14) {
            if (type.toLowerCase().equals(END_OF_DAY)) {
                time += " 23:59:59";
            } else {
                time += " 00:00:00";
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat(BASE_PATTERN);
        Date myDate;
        try {
            myDate = formatter.parse(time);
            String timeStr = formatter.format(myDate);
            return timeStr;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formateTimeString(String time) {
        return formateTimeString(time, "start");
    }

    /**
     * 计算日期
     * @param date 要计算的日期
     * @param type 日期是一日的开始还是一日的结束
     * @param days 要增减的日数
     */
    public static Date caculateDate(Date date, String type, int days) {
        if (date == null)
            date = new Date();
        date = caculateDate(date, days);
        if (type == null)
            return date;
        String dateStr = formateDate(date, "yyyy-MM-dd");
        if (type.equals(END_OF_DAY))
            dateStr += " 23:59:59";
        else if (type.equals(START_OF_DAY))
            dateStr += " 00:00:00";
        return parseDate(dateStr, BASE_PATTERN);
    }

    /**
     * 比较date1 和 date2 哪个更接近未来，如果date2新，return true，否则，return false
     */
    public static boolean compareDate(Date date1, Date date2) {
        if (null == date1) {
            if (null != date2) {
                return true;
            }
        } else if ((null != date2) && date2.after(date1)) {
            return true;
        }
        return false;
    }

}

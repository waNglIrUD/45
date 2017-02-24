package Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ppg on 2016/2/19.
 */
public class TimeUtil {

    public static String[] WEEK = new String[]{"天", "一", "二", "三", "四", "五", "六"};

    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;

    /**
     * String 转换 Date
     *
     * @param str
     * @param format
     * @return
     */
    public static Date string2Date(String str, String format) {
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * Date（long） 转换 String
     *
     * @param time
     * @param format
     * @return
     */
    public static String date2String(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(time);
        return s;
    }

    /**
     * long 去除 时分秒
     * 时分秒全部为0
     *
     * @param date
     * @return
     */
    public static long getYearMonthDay(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    // a integer to xx:xx:xx
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        if (time <= 60)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                timeStr="00:"+minute;
            } else {
                hour = minute / 60;
                minute = minute % 60;
                timeStr=pad(hour)+":"+pad(minute);
            }
        }
        return timeStr;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

//    /**
//     * 获取目标时间和当前时间之间的差距
//     *
//     * @param date
//     * @return
//     */
//    public static String getTimestampString(Date date) {
//        Date curDate = new Date();
//        long splitTime = curDate.getTime() - date.getTime();
//        if (splitTime &lt; (30 * ONE_DAY)) {
//            if (splitTime &lt; ONE_MINUTE) {
//                return "刚刚";
//            }
//            if (splitTime &lt; ONE_HOUR) {
//                return String.format("%d分钟前", splitTime / ONE_MINUTE);
//            }
//
//            if (splitTime &lt; ONE_DAY) {
//                return String.format("%d小时前", splitTime / ONE_HOUR);
//            }
//
//            return String.format("%d天前", splitTime / ONE_DAY);
//        }
//        String result;
//        result = "M月d日 HH:mm";
//        return (new SimpleDateFormat(result, Locale.CHINA)).format(date);
//    }

//    /**
//     * 24小时制 转换 12小时制
//     *
//     * @param time
//     * @return
//     */
//    public static String time24To12(String time) {
//        String str[] = time.split(":");
//        int h = Integer.valueOf(str[0]);
//        int m = Integer.valueOf(str[1]);
//        String sx;
//        if (h &lt; 1) {
//            h = 12;
//            sx = "上午";
//        } else if (h &lt; 12) {
//            sx = "上午";
//        } else if (h &lt; 13) {
//            sx = "下午";
//        } else {
//            sx = "下午";
//            h -= 12;
//        }
//        return String.format("%d:%02d%s", h, m, sx);
//    }

    /**
     * Date 转换 HH
     *
     * @param date
     * @return
     */
    public static String date2HH(Date date) {
        return new SimpleDateFormat("HH").format(date);
    }

    /**
     * Date 转换 HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String date2HHmm(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    /**
     * Date 转换 HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String date2HHmmss(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }
    /**
     * Date 转换 YY:HH:mm
     *
     * @param date
     * @return
     */
    public static String date2YYHHmm(Date date) {
        return new SimpleDateFormat("YY:HH:mm").format(date);
    }

    /**
     * Date 转换 MM.dd
     *
     * @param date
     * @return
     */
    public static String date2MMdd(Date date) {
        return new SimpleDateFormat("MM.dd").format(date);
    }

    /**
     * Date 转换 yyyy.MM.dd
     *
     * @param date
     * @return
     */
    public static String date2yyyyMMdd(Date date) {
        return new SimpleDateFormat("yyyy.MM.dd").format(date);
    }

    /**
     * Date 转换 MM月dd日 星期
     *
     * @param date
     * @return
     */
    public static String date2MMddWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return new SimpleDateFormat("MM月dd日 星期").format(date) + WEEK[dayOfWeek - 1];
    }

    /**
     * Date 转换 yyyy年MM月dd日 星期
     *
     * @param date
     * @return
     */
    public static String date2yyyyMMddWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return new SimpleDateFormat("yyyy年MM月dd日 星期").format(date) + WEEK[dayOfWeek - 1];
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String currentTime() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR)).append("/")
                .append(calendar.get(Calendar.MONTH) + 1).append("/")
                .append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ")
                .append(calendar.get(Calendar.HOUR_OF_DAY)).append(":")
                .append(calendar.get(Calendar.MINUTE)).append(":")
                .append(calendar.get(Calendar.SECOND));
        return sb.toString();
    }

    /**
     * 是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(long date1, long date2) {
        long days1 = date1 / (1000 * 60 * 60 * 24);
        long days2 = date2 / (1000 * 60 * 60 * 24);
        return days1 == days2;
    }

    /**
     * 计算两个日期型的时间相差多少时间
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return
     */
    public static   String twoDateDistance(Date startDate,Date endDate){

        if(startDate == null ||endDate == null){
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong<60*1000)
            return timeLong/1000 + "秒前";
        else if (timeLong<60*60*1000){
            timeLong = timeLong/1000 /60;
            return timeLong + "分钟前";
        }
        else if (timeLong<60*60*24*1000){
            timeLong = timeLong/60/60/1000;
            return timeLong+"小时前";
        }
        else if (timeLong<60*60*24*1000*7){
            timeLong = timeLong/1000/ 60 / 60 / 24;
            return timeLong + "天前";
        }
        else if (timeLong<60*60*24*1000*7*4){
            timeLong = timeLong/1000/ 60 / 60 / 24/7;
            return timeLong + "周前";
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }

}

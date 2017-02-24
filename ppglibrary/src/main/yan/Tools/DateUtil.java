package Tools;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;


public class DateUtil {	
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_DATETIME_yMdHm = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_MONTH = "yyyy-MM";
	public static final String FORMAT_HHMM = "HH:mm";


	public static long SECOND_MILLIS = 1000L;
	public static long MINUTE_MILLIS = 60 * SECOND_MILLIS;
	public static long HOUR_MILLIS = 60 * MINUTE_MILLIS;
	public static long DAY_MILLIS = 24 * HOUR_MILLIS;
	public static long WEEK_MILLIS = 7 * DAY_MILLIS;



	/**
	 * 用户自己定义日期和格式，进行格式化
	 * @param date 用户指定的日期
	 * @param pattern 用户指定的时间格式
	 * @return 返回指定的格式化后的时间字符串
	 */
	public static String formatDate(Date date, String pattern){
		if(date == null || TextUtils.isEmpty(pattern)){
			return null;
		}
		SimpleDateFormat datePattern = new SimpleDateFormat(pattern);
		
		return datePattern.format(date);
	}
	
	/**
	 * 对指定的日期，使用 yyyy-MM 形式进行格式化
	 * @param date 指定的日期
	 * @return 返回 yyyy-MM 格式的字符串
	 */
	public static String getMonthStr(Date date) {
		if(date == null){
			return null;
		}
		return new SimpleDateFormat(FORMAT_MONTH).format(date);
	}
	
	/**
	 * 对指定的日期，使用 yyyy-MM-dd 形式进行格式化
	 * @param date 指定的日期
	 * @return 返回 yyyy-MM-dd 格式的字符串
	 */
	public static String getDateStr(Date date){
		if(date == null){
			return null;
		}
		return new SimpleDateFormat(FORMAT_DATE).format(date);
	}

	/**
	 * 对指定的日期，使用 yyyy-MM-dd 形式进行格式化
	 * @param date 指定的日期
	 * @return 返回 yyyy-MM-dd 格式的字符串
	 */
	public static String getDateStrHHMM(Date date){
		if(date == null){
			return "";
		}
		return new SimpleDateFormat(FORMAT_HHMM).format(date);
	}
	
	/**
	 * 对指定的毫秒数，使用 yyyy-MM-dd 形式进行格式化
	 * @param timeMillis 指定的毫秒数
	 * @return 返回 yyyy-MM-dd 格式的字符串
	 */
	public static String getDateStr(long timeMillis) {
		return getDateStr(new Date(timeMillis));
	}

	/**
	 * 对指定的毫秒数，使用 HH-mm 形式进行格式化
	 * @param timeMillis 指定的毫秒数
	 * @return 返回HH-mm 格式的字符串
	 */
	public static String getDateStrMMHH(long timeMillis) {
		return getDateStrHHMM(new Date(timeMillis));
	}
	
	/**
	 * 对指定的日期，使用 yyyy-MM-dd HH:mm:ss 形式进行格式化
	 * @param date 指定的日期
	 * @return 返回 yyyy-MM-dd HH:mm:ss 格式的字符串
	 */
	public static String getDateTimeStr(Date date){
		if(date == null){
			return null;
		}
		return new SimpleDateFormat(FORMAT_DATETIME).format(date);
	}
	
	/**
	 * 对指定的日期，使用 yyyy-MM-dd HH:mm 形式进行格式化
	 * @param date 指定的日期
	 * @return 返回 yyyy-MM-dd HH:mm 格式的字符串
	 */
	public static String getDateTimeyMdHmStr(Date date){
		if(date == null){
			return null;
		}
		return new SimpleDateFormat(FORMAT_DATETIME_yMdHm).format(date);
	}
	
	/**
	 * 对指定的毫秒数，使用 yyyy-MM-dd HH:mm:ss 形式进行格式化
	 * @param date 指定的毫秒数
	 * @return 返回 yyyy-MM-dd HH:mm:ss 格式的字符串
	 */
	public static String getDateTimeStr(long timeMillis) {
		return getDateTimeStr(new Date(timeMillis));
	}

	/**
	 * 
	 * @return 返回当前时间的 yyyy-MM-dd 格式的字符串
	 */
	public static String getCurrentDateStr() {
		return getDateStr(new Date());
	}
	
	/**
	 * 
	 * @return 返回当前时间的 yyyy-MM-dd 格式的字符串
	 */
	public static Date getCurrentDate() {
		return convert(getCurrentTimestamp());
	}

	/**
	 * 
	 * @return 返回当前时间的 yyyy-MM-dd HH:mm:ss 格式的字符串
	 */
	public static String getCurrentDateTimeStr() {
		return getDateTimeStr(new Date());
	}
	
	/**
	 * 
	 * @return 返回当前时间的 yyyy-MM 格式的字符串
	 */
	public static String getCurrentMonthStr() {
		return getMonthStr(new Date());
	}
	
	/**
	 * 在指定的日期的基础上添加指定单位的数值，然后格式化成 yyyy-MM-dd HH:mm:ss 的字符串后返回
	 * @param date 指定的日期
	 * @param diffTime 指定的时间数值（如果需要减，则使用负数即可）
	 * @param unit 指定的时间单位
	 * @return 返回 yyyy-MM-dd HH:mm:ss 格式的字符串
	 */
	public static String timeAddToStr(Date date, long diffTime, TimeUnit unit) {
		if(date == null){
			return null;
		}
		long resultTime = date.getTime() + unit.toMillis(diffTime);

		return getDateTimeStr(resultTime);
	}
	
	
	/**
	 * 在指定的日期的基础上添加指定单位的数值，并返回
	 * @param date 指定的日期
	 * @param diffTime 指定的时间数值，可以为负数
	 * @param unit 指定的时间单位
	 * @return 返回计算之后的日期
	 */
	public static Date timeAdd(Date date, long diffTime, TimeUnit unit) {
		if(date == null){
			return null;
		}
		long resultTime = date.getTime() + unit.toMillis(diffTime);

		return new Date(resultTime);
	}
	
	/**
	 * 在指定的日期上添加指定days天数，然后返回
	 * @param date 指定的日期
	 * @param days 需要添加的天数，可以为负数
	 * @return 在指定的日期上添加指定days天数，然后返回
	 */
	@SuppressLint("NewApi")
	public static Date timeAddByDays(Date date, int days) {
		return timeAdd(date, days, TimeUnit.DAYS);
	}
	
	/**
	 * 
	 * @param date 日期
	 * @param months 需要添加的月份数，可以为负数
	 * @return 在指定的日期上添加指定months个月，然后返回
	 */
	public static Date timeAddByMonth(Date date, int months){
		if(date == null) {
			return null;
		}
		if(months == 0){
			return date;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		
		return cal.getTime();
	}
	
	/**
	 * 返回指定日期所在月份的第一天的日期
	 * @param date
	 * @return 返回指定日期所在月份的第一天的日期
	 */
	public static String getFirstDayOfMonth(Date date){
		if(date == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		return getDateStr(cal.getTime());
	}
	
	/**
	 * 
	 * @return 返回昨天的日期字符串，格式为 yyyy-MM-dd
	 */
	@SuppressLint("NewApi")
	public static String getYestoday(){
		return timeAddToStr(new Date(), -1, TimeUnit.DAYS).split(" ")[0];
	}
	

	/**
	 * 按照 yyyy-MM-dd 的格式解析给定的日期字符串
	 * @param dateStr 给定的日期字符串
	 * @return 返回解析后的日期，如果解析失败，则返回null
	 */
	public static Date parseDate(String dateStr){
		try {
			return new SimpleDateFormat(FORMAT_DATE).parse(dateStr);
		} catch (ParseException e) {
			Log.e("dateUtil", "parseDate exception : ", e);
		}
		
		return null;
	}
	
	/**
	 * 按照 yyyy-MM-dd HH:mm:ss 的格式解析给定的日期字符串
	 * @param dateTimeStr 给定的日期字符串
	 * @return 返回解析后的日期，如果解析失败，则返回null
	 */
	public static Date parseDateTime(String dateTimeStr){
		try {
			return new SimpleDateFormat(FORMAT_DATETIME).parse(dateTimeStr);
		} catch (ParseException e) {
			Log.e("dateUtil", "parseDateTime exception : ", e);
		}
		
		return null;
	}

	/**
	 * 按照 yyyy-MM-dd HH:mm:ss 的格式解析给定的日期字符串
	 * @param dateTimeStr 给定的日期字符串
	 * @return 返回解析后的日期，如果解析失败，则返回null
	 */
	public static Date parseDateTimeHHMM(String dateTimeStr){
		try {
			return new SimpleDateFormat(FORMAT_HHMM).parse(dateTimeStr);
		} catch (ParseException e) {
			Log.e("dateUtil", "parseDateTime exception : ", e);
		}

		return null;
	}
	
	/**
	 * 按照指定的format格式解析给定的日期字符串
	 * @param dateStr 给定的日期字符串
	 * @param format 指定的日期格式
	 * @return 将日期字符串解析成Date对象
	 */
	public static Date parseToDate(String dateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			Log.e("dateUtil", "parseToDate exception : ", e);
		}
		return date;
	}
	
	/**
	 * 将给定的日期字符串按照 yyyy-MM-dd HH:mm:ss 格式解析成Timestamp对象
	 * @param dateTimeStr 给定的日期字符串
	 * @return 返回解析成功后的Timestamp对象
	 */
	public static Timestamp parseTimestamp(String dateTimeStr){
		Date date = parseDateTime(dateTimeStr);
		
		return convert(date);
	}
	
    /**
     * 
     * @return 返回当前时间的Timestamp对象
     */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 
	 * @param date 指定的Date对象
	 * @return 将指定的Date对象转换成Timestamp对象
	 */
	public static Timestamp convert(Date date) {
		if(date == null){
			return null;
		}
		return new Timestamp(date.getTime());
	}

	/**
	 * 
	 * @param timestamp 指定的Timestamp对象
	 * @return 将指定的Timestamp对象转换成Date对象
	 */
	public static Date convert(Timestamp timestamp) {
		if(timestamp == null){
			return null;
		}
		return new Date(timestamp.getTime());
	}

	/**
	 * 对给定的两个日期进行比较，如果date1 比 date2 大，则返回1；如果相等，则返回0；否则返回-1
	 * @param date1
	 * @param date2
	 * @return 对给定的两个日期进行比较，如果date1 比 date2 大，则返回1；如果相等，则返回0；否则返回-1
	 */
	public static int compare(Date date1, Date date2){
		if(date1 == null){
			return -1;
		}
		if(date2 == null){
			return 1;
		}
		long timeDiff = date1.getTime() - date2.getTime();
		
		return timeDiff == 0 ? 0 : (int)(timeDiff/Math.abs(timeDiff));
	}
	


	
	
	
	// （版本2）得出的结果是1天，我们一般用的方法都会推荐版本2

	public static int daysOfTwo(Date fDate, Date oDate) {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(fDate);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(oDate);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return day2 - day1;

	}

	/**
	 * 获取当前系统之前N天的时间 times几天前
	 * 
	 * @return 返回字符串型的时间 "yyyy-MM-dd HH:mm:ss"
	 */
	public static String getYesterdayTime(int times) {
		Calendar c = Calendar.getInstance();// 把当前时间中的data自减1
		c.add(Calendar.DAY_OF_MONTH, -times);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mDateTime = formatter.format(c.getTime());
		return mDateTime;
	}

	/**
	 * day 表示几天前的日期 获取当前系统之前n天的时间
	 * 
	 * @return 返回字符串型的时间 "yyyy-MM-dd"
	 */
	public static String getWeekDate(int day) {
		Calendar c = Calendar.getInstance();// 把当前时间中的data自减1
		c.add(Calendar.DAY_OF_MONTH, -(day));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String mDateTime = formatter.format(c.getTime());
		String strStart = mDateTime.substring(0, 10);
		return strStart;
	}
	
	public static DateDetail interval(Date beginDate, Date endDate) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = df.parse(getDateTimeStr(endDate));
			Date date = df.parse(getDateTimeStr(beginDate));
			DateDetail detail = new DateDetail();
			long l = now.getTime() - date.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			detail.day = day;
			detail.hour = hour;
			detail.minute = min;
			detail.second = s;
			return detail;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DateDetail countDown(DateDetail detail) {
		if(detail.day == 0 && detail.hour == 0
				&& detail.minute == 0 && detail.second == 0)
			return detail;
		else if(detail.day > 0 && detail.hour == 0
				&& detail.minute == 0 && detail.second == 0) {
			detail.day -= 1;
			detail.hour = 23;
			detail.minute = 59;
			detail.second = 59;
			return detail;
		}
		detail.second -= 1;
		if(detail.second < 0) {
			detail.minute -= 1;
			detail.second = 59;
		}
		if(detail.minute < 0) {
			detail.hour -= 1;
			detail.minute = 59;
		}
		if(detail.hour < 0) {
			detail.hour = 0;
		}
		return detail;
	}
	
	public static class DateDetail {
		public long day;
		public long hour;
		public long minute;
		public long second;
	}
}

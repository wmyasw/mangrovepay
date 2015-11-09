package com.jdjt.mangrovepay.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

	// 格式化日期为字符串 "yyyy-MM-dd   hh:mm"
	public static String formatDateTime(Date basicDate, String strFormat) {
		SimpleDateFormat df = new SimpleDateFormat(strFormat);
		return df.format(basicDate);
	}
	
	
	/**
	 * 根据传入日期 和 转换格式转换时间  例： 2014-06-06 | yyyy-MM-dd
	 * @param basicDate
	 * @param strFormat
	 * @return 
	 * 2014-8-11下午9:49:47 
	 * @author wangmingyu
	 */
	public static String formatStringDate(String basicDate, String strFormat) {
		Date date=DateUtil.convertStringToDate(basicDate);
		SimpleDateFormat df = new SimpleDateFormat(strFormat);
		return df.format(date);
	}
	
	/**
	 * 根据传入日期 和 转换格式转换时间  例： 2014-06-06 20:03:08 | yyyy-MM-dd HH:mm:ss
	 * @param basicDate
	 * @param strFormat
	 * @return 
	 * 2014-8-11下午9:49:47 
	 * @author wangmingyu
	 */
	public static String formatStringDateTime(String basicDateTime, String strFormat) {
		Date date=DateUtil.convertStringToDateTime(basicDateTime);
		SimpleDateFormat df = new SimpleDateFormat(strFormat);
		return df.format(date);
	}
	// 格式化日期为字符串 "yyyy-MM-dd   hh:mm"
	public static String formatDateTime(String basicDate, String strFormat) {
		SimpleDateFormat df = new SimpleDateFormat(strFormat);
		Date tmpDate = null;
		try {
			tmpDate = df.parse(basicDate);
		} catch (Exception e) {
			// 日期型字符串格式错误
		}
		return df.format(tmpDate);
	}

	// 当前日期加减n天后的日期，返回String (yyyy-mm-dd)
	public static String tomorrowDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DAY_OF_MONTH, +1);
		return df.format(rightNow.getTime());
	}

	// 当前日期加减n天后的日期，返回String (yyyy-mm-dd)
	public static String nDaysAftertoday(int n) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		// rightNow.add(Calendar.DAY_OF_MONTH,-1);
		rightNow.add(Calendar.DAY_OF_MONTH, +n);
		return df.format(rightNow.getTime());
	}

	// 当前日期加减n天后的日期，返回String (yyyy-mm-dd)
	public static Date nDaysAfterNowDate(int n) {
		Calendar rightNow = Calendar.getInstance();
		// rightNow.add(Calendar.DAY_OF_MONTH,-1);
		rightNow.add(Calendar.DAY_OF_MONTH, +n);
		return rightNow.getTime();
	}

	// 给定一个日期型字符串，返回加减n天后的日期型字符串
	public static String nDaysAfterOneDateString(String basicDate, int n) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date tmpDate = null;
		try {
			tmpDate = df.parse(basicDate);
		} catch (Exception e) {
			// 日期型字符串格式错误
		}
		long nDay = (tmpDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n)
				* (24 * 60 * 60 * 1000);
		tmpDate.setTime(nDay);

		return df.format(tmpDate);
	}

	// 给定一个日期，返回加减n天后的日期
	public static Date nDaysAfterOneDate(Date basicDate, int n) {
		long nDay = (basicDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n)
				* (24 * 60 * 60 * 1000);
		basicDate.setTime(nDay);

		return basicDate;
	}

	// 计算两个日期相隔的天数
	public static int nDaysBetweenTwoDate(Date firstDate, Date secondDate) {
		int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return nDay;
	}

	// 计算两个日期相隔的天数
	public static int nDaysBetweenTwoDate(String firstString,
			String secondString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = df.parse(firstString);
			secondDate = df.parse(secondString);
		} catch (Exception e) {
			// 日期型字符串格式错误
		}

		int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return nDay;
	}

	public static Date convertStringToDate(String date_s, String parten) {
		SimpleDateFormat df = new SimpleDateFormat(parten);
		try {
			return df.parse(date_s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date convertStringToDate(String date_s) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(date_s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Date convertStringToDateTime(String date_s) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(date_s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/* 将字符串转换为java.sql.Date类型 */
	public static java.sql.Timestamp dateFormatsqlutil(String s) {
		java.text.DateFormat format1 = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		java.util.Date timeDate = null;
		try {
			timeDate = format1.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(timeDate);
		Timestamp ts = Timestamp.valueOf(time);
		return ts;
	}

	/* 将字符串转换为Date类型 */
	public static java.sql.Timestamp dateFormatsqlutil(Date timeDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(timeDate);
		Timestamp ts = Timestamp.valueOf(time);
		return ts;
	}

	/**
	 * 取得时间段内时间list 注：list不包含 outdate这一天
	 * 
	 * @param indate
	 *            2012-06-12
	 * @param outdate
	 *            2012-06-22
	 * @return 2012-06-12 2012-06-13.. 集合
	 */
	public static List<String> getDateStringList(String indate, String outdate) {
		List<String> list = new ArrayList<String>();
		// 取时间段的天数
		int length = nDaysBetweenTwoDate(indate, outdate);
		list.add(indate);
		for (int i = 0; i < length; i++) {
			String nextDate = DateUtil.nDaysAfterOneDateString(indate, 1);
			String _a = formatDateTime(convertStringToDate(nextDate),
					"yyyy-MM-dd");
			String _b = formatDateTime(convertStringToDate(outdate),
					"yyyy-MM-dd");
			if (!_a.equals(_b)) {
				list.add(nextDate);
				indate = nextDate;
			} else
				break;
		}
		return list;
	}

	/**
	 * @Title: nowDateCheck
	 * @Description: TODO(判断输入的日期 是否在 4-1 ~4-15 之间 包括 1号 及 15号)
	 * @param @param now
	 * @param @return 设定文件
	 * @return boolean 如果在这之间 返回 true ，否则 返回false
	 * @throws
	 * @author 王明雨
	 * @date 2014-1-9 上午10:06:08
	 */
	public static boolean nowDateCheck(Date now) {
		Date start = DateUtil.convertStringToDate("04-08", "MM-dd");
		Date end = DateUtil.convertStringToDate("04-15", "MM-dd");
		now = DateUtil.convertStringToDate(
				DateUtil.formatDateTime(now, "MM-dd"), "MM-dd");
		if (now.after(start) && now.before(end) || now.equals(start)
				|| now.equals(end)) {
			return true;
		}
		return false;
	}

	/**
	 * 验证判断 一个时间是否在 startDate 很 enddate 之间，
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param checkDate
	 *            验证时间
	 * @return 2014-7-4下午1:55:15
	 * @author wangmingyu
	 */
	public static boolean checkedDateBetwen(String startDate, String endDate,
			String checkDate) {
		Date start = DateUtil.convertStringToDate(startDate, "yyyy-MM-dd");
		Date end = DateUtil.convertStringToDate(endDate, "yyyy-MM-dd");
		Date check = DateUtil.convertStringToDate(checkDate, "yyyy-MM-dd");
		if (check.after(start) && check.before(end) || check.equals(start)
				|| check.equals(end)) {
			return true;
		}
		return false;
	}

	/**
	 * @Title: isDateBetweenToDay
	 * @Description: TODO(判断2个日期 是否大于60天 )
	 * @param @param now
	 * @param @param end
	 * @param @return 设定文件
	 * @return boolean 大于60天 返回true ，否则返回false
	 * @throws
	 * @author 王明雨
	 * @date 2014-1-15 下午01:41:22
	 */
	public static boolean isDateBetweenToDay(Date now, Date end) {
		int days = nDaysBetweenTwoDate(now, end);
		if (days > 60) {
			return true;
		}
		return false;
	}

	public static Date formatDate(String d, String formatStr) {
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		Date nowd = null;
		try {
			nowd = df.parse(d);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return nowd;
	}
	private static long getTimer(long t){
		long time=t;
		return  time < 0 ? time  = 0 :(time=time);
	}
	private static String getTimerString(String t){
		String str="";
		return  t.toString().length() < 2 ? str = "0" + t.toString() : (str = t);
	}
	
	/**
	 * 传入参数单位 为秒 
	 * @param time
	 * @return
	 */
	public static String timer(long time)
	{
		long h,m,s,hstr;
		String mstr,sstr,timestr;
		long etime =time; //总秒数s
		h = Long.valueOf(etime / 3600); //时
		m =Long.valueOf(etime / 60) % 60; //分
		s =Long.valueOf(etime % 60); //秒
		h = getTimer(h);
		m  = getTimer(m);
		s = getTimer(s);


	timestr = getTimerString(h+"") + ":" + getTimerString(m+"") + ":" + getTimerString(s+"");


		//etime = etime - 1;
		return timestr;
	}
	/*************************************************
	 @Title: formatDateToDay 
	 @Description: TODO(根据传入日期 判断当前时间并拼接字符串)
	  1、当前年份的 当天 和昨天 添加 文字标明
	  2、当前年份的 非当天和昨天的 显示月份和时间
	  3、全年分时间显示
	 @param date
	 @return    设定文件 
	 @return String    返回类型 
	 @throws 
	 @date  2015-2-6
	*************************************************/
	public static String  formatDateToDay(String date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dFormat = new SimpleDateFormat("MM-dd HH:mm");
		String timeS=date;
		Date now=new Date();
		int year=now.getYear();
		int day=now.getDay();
		try {
			Date format=df.parse(date);
			int y=format.getYear();
			int d=format.getDay();
			if(year==y&&d==day){
				timeS ="今天 "+format.getHours()+":"+format.getMinutes();
			}
			if(year==y&&(day-1)==d){
				timeS ="昨天 "+format.getHours()+":"+format.getMinutes();
			}
			if(year==y&&day!=d&&(day-1)!=d){
				timeS =dFormat.format(format);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return timeS;
		}
		return timeS;
	}
	public static void main(String[] args) {

		/*
		 * System.out.println(DateUtil.formatDateTime(DateUtil.convertStringToDate
		 * ("1-2", "MM-dd"), "MM-dd")); Date
		 * start=DateUtil.convertStringToDate("04-16", "MM-dd");
		 * nowDateCheck(start);
		 */
		System.out.println(DateUtil.formatDate("2012-09-18", "MM月dd日"));
		System.out.println(DateUtil.nDaysBetweenTwoDate("2012-09-18",
				"2013-01-01"));
	}
}
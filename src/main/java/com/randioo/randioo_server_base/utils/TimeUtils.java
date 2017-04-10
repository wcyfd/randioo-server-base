package com.randioo.randioo_server_base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	private static final String yearFormat = "yyyy-MM-dd";
	private static final DateFormat dateFormat = new SimpleDateFormat(yearFormat);

	public static int getNowTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	/**
	 * 获得今天的固定秒时间,timeStr必须是符合format格式的字符串
	 * 
	 * @param nowTime
	 * @param timeStr
	 * @param format
	 * @return
	 * @author wcy 2016年8月29日
	 */
	public static int getTodayTimePointByRegex(int nowTime, String timeStr, String format) {
		long time = (long) nowTime * 1000;
		Date d = new Date(time);
		String regex = yearFormat + format;

		SimpleDateFormat sdf = new SimpleDateFormat(regex);
		String yearStr = dateFormat.format(d);
		try {
			d = sdf.parse(yearStr + timeStr);
			return (int) (d.getTime() / 1000);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 获得当日时间
	 * 
	 * @return
	 * @author wcy 2017年3月10日
	 */
	public static String getCurrentTimeStr() {
		String date = dateFormat.format(new Date());
		return date;
	}

	public static String getTimeStr(long milliseconds) {
		Date date = new Date(milliseconds);
		return dateFormat.format(date);
	}

	/**
	 * 转换成长整型值
	 * 
	 * @param time
	 * @return
	 * @author wcy 2017年3月31日
	 */
	public static long parseInt2Long(int time) {
		return ((long) time) * 1000;
	}
}

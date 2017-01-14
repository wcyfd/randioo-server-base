package com.randioo.randioo_server_base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	private static final String yearFormat = "yyyy-MM-dd";
	private static final DateFormat dateFormat = new SimpleDateFormat(yearFormat);
	
	public static int getNowTime(){
		return (int)(System.currentTimeMillis()/1000);
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
}

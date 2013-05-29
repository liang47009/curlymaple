package com.yunfeng.protocol.util;

import java.util.Calendar;

/**
 * 日期工具
 * 
 * @author xialiangliang
 * 
 */
public class DateUtil {

	/**
	 * 当前月份的最后一天
	 * 
	 * @param format
	 * @return
	 */
	public static long curMonthLastDay() {
		Calendar ca = Calendar.getInstance();// 获取当前日期
		ca.set(Calendar.DAY_OF_MONTH,
				ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}

	/**
	 * 当前月份的第一天
	 * 
	 * @param format
	 * @return
	 */
	public static long curMonthFirstDay() {
		Calendar ca = Calendar.getInstance();// 获取当前日期
		ca.add(Calendar.MONTH, 0);
		ca.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}

	/**
	 * 上个月份的第一天
	 * 
	 * @param format
	 * @return
	 */
	public static long lastMonthLastDay() {
		Calendar ca = Calendar.getInstance();// 获取当前日期
		ca.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}

	/**
	 * 上个月份的第一天
	 * 
	 * @param format
	 * @return
	 */
	public static long lastMonthFirstDay() {
		Calendar ca = Calendar.getInstance();// 获取当前日期
		ca.add(Calendar.MONTH, -1);
		ca.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}

	/**
	 * 获取当天起始时间
	 * @return
	 */
	public static Long getStartTime() {
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.HOUR, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime().getTime();
	}

	/**
	 * 获取当天结束时间
	 * @return
	 */
	public static Long getEndTime() {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime().getTime();
	}

}

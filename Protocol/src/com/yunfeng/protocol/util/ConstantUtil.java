package com.yunfeng.protocol.util;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.dom4j.Attribute;
import org.dom4j.Element;

public class ConstantUtil {
	private static final Random random = new Random();

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static <T> List<T> readPojoByElement(Class<T> T,
			Element rootElement, Class<?> subElementClass) {
		List<T> objectList = new ArrayList<T>();
		for (Iterator<?> elementIterator = rootElement
				.elementIterator(getClassShortName(T)); elementIterator
				.hasNext();) {
			try {
				Element pojoElement = (Element) elementIterator.next();
				T object = (T) T.newInstance();
				for (Iterator<?> attributeIterator = pojoElement
						.attributeIterator(); attributeIterator.hasNext();) {
					Attribute attribute = (Attribute) attributeIterator.next();
					injectValueByString(attribute.getName(),
							attribute.getValue(), object, T);
				}
				if (subElementClass != null) {
					List<?> subObjectList = readPojoByElement(subElementClass,
							pojoElement, null);
					injectValue(getClassListName(subElementClass),
							subObjectList, object, T);
				}
				objectList.add(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return objectList;
	}

	public static <T> List<T> readPojoByElement(Class<T> T, Element rootElement) {
		List<T> objectList = new ArrayList<T>();
		for (Iterator<?> elementIterator = rootElement
				.elementIterator(getClassShortName(T)); elementIterator
				.hasNext();) {
			try {
				Element pojoElement = (Element) elementIterator.next();
				T object = T.newInstance();
				for (Iterator<?> attributeIterator = pojoElement
						.attributeIterator(); attributeIterator.hasNext();) {
					Attribute attribute = (Attribute) attributeIterator.next();
					injectValueByString(attribute.getName(),
							attribute.getValue(), object, T);
				}

				if (pojoElement.elements().size() != 0) {
					for (Object obj : pojoElement.elements()) {
						Element element = (Element) obj;
						String getterMethodName = getFieldGetterMethodName(element
								.getName() + "List");
						String paramClassName = T.getMethod(getterMethodName)
								.toGenericString().split("<")[1].split(">")[0];
						Class<?> paramClass = Class.forName(paramClassName);
						List<?> paramList = readPojoByElement(paramClass,
								pojoElement);
						String setterMethodName = getFieldSetterMethodName(element
								.getName() + "List");
						Method setterMethod = T.getMethod(setterMethodName, T
								.getMethod(getterMethodName).getReturnType());
						setterMethod.invoke(object, paramList);
					}
				}
				objectList.add(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return objectList;
	}

	public static void injectValueByString(String injectFieldName,
			String injectFieldValue, Object injectObject, Class<?> injectClass) {
		try {
			Method getterMethod = injectClass
					.getMethod(getFieldGetterMethodName(injectFieldName));
			Class<?> returnType = getterMethod.getReturnType();
			Object injectFieldObject = translateObjectToDefaultType(returnType,
					injectFieldValue);
			Method setterMethod = injectClass.getMethod(
					getFieldSetterMethodName(injectFieldName), returnType);
			setterMethod.invoke(injectObject, injectFieldObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void injectValue(String injectFieldName,
			Object injectFieldValue, Object injectObject, Class<?> injectClass) {
		try {
			Method getterMethod = injectClass
					.getMethod(getFieldGetterMethodName(injectFieldName));
			Class<?> returnType = getterMethod.getReturnType();
			Method setterMethod = injectClass.getMethod(
					getFieldSetterMethodName(injectFieldName), returnType);
			setterMethod.invoke(injectObject, injectFieldValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getFieldGetterMethodName(String fieldName) {
		return "get" + getStringHeadUp(fieldName);
	}

	public static String getFieldSetterMethodName(String fieldName) {
		return "set" + getStringHeadUp(fieldName);
	}

	public static String getStringHeadUp(String s) {
		char[] charArray = s.toCharArray();
		char headChar = charArray[0];
		if (headChar >= 'a' && headChar <= 'z') {
			headChar -= 32;
		}
		charArray[0] = headChar;
		return new String(charArray);
	}

	public static String getStringHeadDown(String s) {
		char[] charArray = s.toCharArray();
		char headChar = charArray[0];
		if (headChar >= 'A' && headChar <= 'Z') {
			headChar += 32;
		}
		charArray[0] = headChar;
		return new String(charArray);
	}

	/**
	 * 类型转换，吧string换成其他类型
	 */
	public static Object translateObjectToDefaultType(Class<?> type, Object s) {
		if (s == null) {
			if (type.equals(boolean.class)) {
				return false;
			} else if (type.equals(String.class)) {
				return null;
			} else {
				return 0;
			}
		}
		if (type.equals(int.class)) {
			try {
				return Integer.parseInt(s.toString());
			} catch (Exception e) {
				Log.error("警告 类型转换错误", e);
				return 0;
			}
		} else if (type.equals(double.class)) {
			try {
				return Double.parseDouble(s.toString());
			} catch (Exception e) {
				Log.error("警告 类型转换错误", e);
				return 0.0D;
			}
		} else if (type.equals(float.class)) {
			try {
				return Float.parseFloat(s.toString());
			} catch (Exception e) {
				Log.error("警告 类型转换错误", e);
				return 0.0F;
			}
		} else if (type.equals(short.class)) {
			try {
				return Short.parseShort(s.toString());
			} catch (Exception e) {
				Log.error("警告 类型转换错误", e);
				return 0;
			}
		} else if (type.equals(long.class)) {
			try {
				return Long.parseLong(s.toString());
			} catch (Exception e) {
				Log.error("警告 类型转换错误", e);
				return 0L;
			}
		} else if (type.equals(boolean.class)) {
			try {
				return Boolean.parseBoolean(s.toString());
			} catch (Exception e) {
				Log.error("警告 类型转换错误", e);
				return false;
			}
		} else if (type.equals(byte.class)) {
			try {
				return Byte.parseByte(s.toString());
			} catch (Exception e) {
				Log.error("警告 类型转换错误", e);
				return 0;
			}
		} else if (type.equals(String.class)) {
			return s.equals("") ? null : s;
		} else {
			return s;
		}
	}

	public static String getClassListName(Class<?> c) {
		return getClassShortName(c) + "List";
	}

	public static String getClassShortName(Class<?> beanClass) {
		String[] classNameStrings = beanClass.getName().split("\\.");
		String name = classNameStrings[classNameStrings.length - 1];
		return getStringHeadDown(name);
	}

	/**
	 * 判断日期与时间是否在指定范围内
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return true 范围内， false 范围外
	 */
	public static boolean compareDateTime(String startTime, String endTime) {
		boolean start = false;
		boolean end = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		String[] startStr = startTime.split(" ");
		String[] endStr = endTime.split(" ");
		end = compareStringDate(startStr[0], endStr[0]);
		start = compareStringTime(startStr[1], endStr[1]);
		if (start && end) {
			return true;
		}
		return false;
	}

	public static boolean compareStringTime(String startTime, String endTime) {
		boolean start = false;
		boolean end = false;
		Calendar calendar = Calendar.getInstance();
		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nowMinute = calendar.get(Calendar.MINUTE);

		if ((startTime != null && !startTime.equals(""))) {

			String[] times = startTime.split(":");
			int hour = Integer.parseInt(times[0]);
			int minute = Integer.parseInt(times[1]);

			if ((nowHour == hour && nowMinute >= minute) || (nowHour > hour)) {
				start = true;
			} else {
				start = false;
			}
		} else {
			start = true;
		}
		if (endTime != null && !endTime.equals("")) {
			String[] times = endTime.split(":");
			int hour = Integer.parseInt(times[0]);
			int minute = Integer.parseInt(times[1]);
			if ((nowHour == hour && nowMinute < minute) || (nowHour < hour)) {
				end = true;
			} else {
				end = false;
			}
		} else {
			end = true;
		}

		if (start && end)
			return true;
		else
			return false;
	}

	/**
	 * 判断现在时间是否在指定时间范围
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static boolean compareStringDate(String startDate, String endDate) {
		boolean start = false;
		boolean end = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		String nowDateString = SDF.format(calendar.getTime());

		long nowDate = Long.parseLong(nowDateString.replaceAll("-", ""));

		if ((startDate != null && !startDate.equals(""))) {
			long date = Long.parseLong(startDate.replaceAll("-", ""));
			if (nowDate >= date) {
				start = true;
			} else {
				start = false;
			}
		} else {
			start = true;
		}

		if ((endDate != null && !endDate.equals(""))) {
			long date = Long.parseLong(endDate.replaceAll("-", ""));
			if (nowDate <= date) {
				end = true;
			} else {
				end = false;
			}
		} else {
			end = true;
		}

		if (start && end) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是连续的一天
	 * 
	 * @param oldDate
	 * @param newDate
	 * @return 1:连续的;0:当天;其他:不连续;**返回的是相隔天数
	 */
	public static int isNextDay(Date oldDate, Date newDate) {
		try {
			if (oldDate == null)
				return 1;

			String oldDateString = SDF.format(oldDate);
			// oldDateString = oldDateString.replaceAll("-", "");

			String newDateString = SDF.format(newDate);
			// newDateString = newDateString.replaceAll("-", "");

			Date tempOldDate = SDF.parse(oldDateString);

			Date tempNewDate = SDF.parse(newDateString);

			double prieod = (long) ((tempNewDate.getTime() - tempOldDate
					.getTime()) / (1000 * 60 * 60));

			// long oldDateLong = Long.parseLong(oldDateString);
			//
			// long newDateLong = Long.parseLong(newDateString);
			int days = (int) prieod / 24;

			return days;
			// if (prieod == 24) {
			// return 1;
			// } else if (prieod == 0) {
			// return 0;
			// } else {
			// return 2;
			// }
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 2;
	}

	/**
	 * 是否在规定的时间内
	 * 
	 * @param oldDate
	 *            开始时间
	 * @param period
	 *            天数-小时-分钟
	 * @return
	 * @throws ParseException
	 */
	public static boolean checkPeriod(String oldDateString, String period) {
		if (period == null || period.equals(""))
			return true;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date oldDate = simpleDateFormat.parse(oldDateString);
			String[] periods = period.split("-");
			int days = Integer.parseInt(periods[0]);
			int hours = Integer.parseInt(periods[1]);
			int minuts = Integer.parseInt(periods[2]);
			long periodTime = days * 24 * 60 + hours * 60 + minuts;
			long hours1 = (new Date().getTime() - oldDate.getTime())
					/ (60 * 1000);
			if (hours1 < periodTime)
				return true;

		} catch (ParseException e) {
		}

		return false;
	}

	public static void main(String[] args) {
		// String startTime = "12:00";
		// String endTime = "13:33";
		// boolean flag = ConstantUtil.compareStringTime(startTime, endTime);
		// System.err.println(flag);
		boolean flag = ConstantUtil.compareStringDate("2013-02-06",
				"2013-02-06");
		System.err.println(flag);
	}

	public static int isNextOrSameWeek(int oldyear, int oldWeek) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);

		if (year != oldyear) {
			return 2;
		}
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		if (oldWeek == week) {
			return 0;
		}
		if (week - oldWeek == 1) {
			return 1;
		}
		return 2;
	}

	public static int getYear() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

	public static int getWeek() {
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		return week;
	}

	public static int getWeekday() {
		Calendar calendar = Calendar.getInstance();
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		return weekday;
	}

	public static int getHour() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	public static int getMinute() {
		Calendar calendar = Calendar.getInstance();
		int minute = calendar.get(Calendar.MINUTE);
		return minute;
	}

	public static int getSecond() {
		Calendar calendar = Calendar.getInstance();
		int second = calendar.get(Calendar.SECOND);
		return second;
	}

	public static String getFormatDate(Date date, String format) {
		String formatDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		formatDate = simpleDateFormat.format(date);
		return formatDate;
	}

	public static Date getFormatDate(String dateString, String format) {
		Date formatDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		try {
			formatDate = simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatDate;
	}

	/**
	 * 更新目标
	 * 
	 * @param num
	 */
	public static String updateNum(String[] num) {
		String numString = "";
		for (int i = 0; i < num.length; i++) {
			if (i != num.length - 1) {
				numString += num[i] + ";";
			} else {
				numString += num[i];
			}
		}
		return numString;
	}

	/**
	 * 更新目标
	 * 
	 * @param num
	 */
	public static String updateNum(String[] num, String split) {
		String numString = "";
		for (int i = 0; i < num.length; i++) {
			if (i != num.length - 1) {
				numString += num[i] + split;
			} else {
				numString += num[i];
			}
		}
		return numString;
	}

	/**
	 * 是否包含字符串
	 * 
	 * @param target
	 *            以分号隔开的字符串
	 * @param string
	 *            要找的字符串
	 * @return
	 */
	public static boolean containString(String target, String string) {
		if (target == null || target.equals("")) {
			return false;
		}
		String[] targets = target.split(";");
		for (int i = 0; i < targets.length; i++) {
			if (targets[i].equals(string)) {
				return true;
			}
		}
		return false;
	}

	// /**
	// * 世界频道格式
	// */
	// public String getWorldFormat(String message) {
	// String format = "<rtf><text>" + message + "</text><icons/></rtf>";
	// return format;
	// }

	/**
	 * 玩家公告角色名格式
	 */
	public static String getNameFormat(String roleName) {
		String format = "<font face=\"微软雅黑\" size=\"16\" color=\"#2ebfef\"><b>"
				+ roleName + "</b></font>";
		return format;
	}

	/**
	 * 任务或物品名称
	 */
	public static String getThingNameFormat(String thingName) {
		String format = "<font face=\"微软雅黑\" size=\"16\" color=\"#fee50e\"><b>"
				+ thingName + "</b></font>";
		return format;
	}

	/**
	 * 玩家公告消息格式
	 */
	public static String getMessegeFormat(String messege) {
		String format = "<font face=\"微软雅黑\" size=\"16\" color=\"#98e871\"><b>"
				+ messege + "</b></font>";
		return format;
	}

	/**
	 * 系统公告消息格式
	 */
	public static String getSystemMessegeFormat(String messege) {
		String format = "<font face=\"宋体\" size=\"16\" color=\"#ff582b\"><b>"
				+ messege + "</b></font>";
		return format;
	}

	/**
	 * 计算某日期 多少天后的日期
	 * 
	 * @param datestr
	 *            日期
	 * @param day
	 *            相对天数，为正数表示之后，为负数表示之前
	 * @return 指定日期字符串n天之前或者之后的日期
	 */
	public static Date getBeforeAfterDate(Date datestr, int day) {
		SDF.setLenient(false);
		Calendar cal = new GregorianCalendar();
		cal.setTime(datestr);

		int Year = cal.get(Calendar.YEAR);
		int Month = cal.get(Calendar.MONTH);
		int Day = cal.get(Calendar.DAY_OF_MONTH);

		int NewDay = Day + day;

		cal.set(Calendar.YEAR, Year);
		cal.set(Calendar.MONTH, Month);
		cal.set(Calendar.DAY_OF_MONTH, NewDay);

		return new Date(cal.getTimeInMillis());
	}

	public static Random getRandom() {
		return random;
	}

}

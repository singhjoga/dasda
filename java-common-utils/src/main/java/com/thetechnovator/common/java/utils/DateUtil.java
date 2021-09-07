package com.thetechnovator.common.java.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	public static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);
	public static final String GERMAN_DATE_TIME = "dd.MM.yyyy HH:mm:ss";
	public static final String JSON_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String GERMAN_DATE = "dd.MM.yyyy";
	public static final String JSON_DATE = "yyyy-MM-dd";
	public static final SimpleDateFormat germanDateTimeFormat = new SimpleDateFormat(GERMAN_DATE_TIME);
	public static final SimpleDateFormat germanDateTimeFormatUtc = new SimpleDateFormat(GERMAN_DATE_TIME);
	public static final SimpleDateFormat jsonDateTimeFormat = new SimpleDateFormat(JSON_DATE_TIME);
	public static final SimpleDateFormat germanDateFormat = new SimpleDateFormat(GERMAN_DATE);
	public static final SimpleDateFormat jsonDateFormat = new SimpleDateFormat(JSON_DATE);

	/**
	 * Returns date string in German date time format i.e.dd.MM.yyyy HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		return germanDateTimeFormat.format(new Date());
	}

	/**
	 * Returns date string in German date format i.e.dd.MM.yyyy
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return germanDateFormat.format(new Date());
	}

	public static Date toDate(String germanDateString) {
		try {
			return germanDateFormat.parse(germanDateString);
		} catch (ParseException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}
	public static Date toDateFromGermanFormat(String germanDateString) {
		try {
			return germanDateFormat.parse(germanDateString);
		} catch (ParseException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}
	public static Date toDateFromJsonFormat(String jsonDateString) {
		try {
			return jsonDateFormat.parse(jsonDateString);
		} catch (ParseException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}
	public static Date toDateTime(String germanDateTimeString) {
		try {
			return germanDateTimeFormat.parse(germanDateTimeString);
		} catch (ParseException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}
	public static Date toDateTimeFromGermanFormat(String germanDateTimeString) {
		try {
			return germanDateTimeFormat.parse(germanDateTimeString);
		} catch (ParseException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}
	public static Date toDateTimeFromJsonFormat(String jsonDateTimeString) {
		try {
			return jsonDateTimeFormat.parse(jsonDateTimeString);
		} catch (ParseException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}
	public static boolean isDateTimeFromGermanFormat(String germanDateTimeString) {
		return toDateTimeFromGermanFormat(germanDateTimeString) != null;
	}
	public static boolean isDateTimeFromJsonFormat(String jsonDateTimeString) {
		return toDateTimeFromJsonFormat(jsonDateTimeString) != null;
	}
	public static boolean isDateFromGermanFormat(String germanDateString) {
		return toDateFromGermanFormat(germanDateString) != null;
	}
	public static boolean isDateFromJsonFormat(String jsonDateString) {
		return toDateFromJsonFormat(jsonDateString) != null;
	}
	public static String toDateString(Date date) {
		if (date == null)
			return null;
		return germanDateFormat.format(date);
	}

	public static String toDateTimeString(Date date) {
		if (date == null)
			return null;
		return germanDateTimeFormat.format(date);
	}

	public static Date toStartOfDay(Date date) {
		if (date == null)
			return null;
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}
	public static Date minDate() {

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}
	public static Date maxDate() {

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 9999);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}
	
	public static Date toStartOfDayUTC(Date date) {
		if (date == null)
			return null;
		germanDateTimeFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//cal.setTimeZone(TimeZone.getTimeZone("UTC"));

		return cal.getTime();
	}

	public static Date toEndOfDay(Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));

		return cal.getTime();
	}

	public static String msToNamedTimeInterval(long milliseconds) {
		long millis = milliseconds % 1000;
		long second = (milliseconds / 1000) % 60;
		long minute = (milliseconds / (1000 * 60)) % 60;
		long hour = (milliseconds / (1000 * 60 * 60)) % 24;	
		StringBuilder sb = new StringBuilder();
		if (hour > 0) {
			sb.append(hour).append(hour==1?" hour":" hours");
		}
		if (minute > 0) {
			sb.append(sb.length()==0?"":" ").append(minute).append(minute==1?" minute":" minutes");
		}
		if (second > 0) {
			sb.append(sb.length()==0?"":" ").append(second).append(second==1?" second":" seconds");
		}
		if (millis > 0) {
			sb.append(sb.length()==0?"":" ").append(millis).append(millis==1?" millisecond":" milliseconds");
		}
		
		return sb.toString();
	}
	
	public static LocalTime parseLocalTime(String time, DateTimeFormatter dateFormatter) {
		if (StringUtils.isEmpty(time)) {
			return null;
		}
		try {
			LocalTime result = LocalTime.parse(time, dateFormatter);
			return result;
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	public static LocalDate parseLocalDate(String date, DateTimeFormatter dateFormatter) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		try {
			LocalDate result = LocalDate.parse(date, dateFormatter);
			return result;
		} catch (DateTimeParseException e) {
			return null;
		}
	}
}

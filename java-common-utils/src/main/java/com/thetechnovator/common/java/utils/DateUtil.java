package com.thetechnovator.common.java.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	public static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);
	public static final String GERMAN_DATE_TIME = "dd.MM.yyyy HH:mm:ss";
	public static final String GERMAN_DATE = "dd.MM.yyyy";
	private static final SimpleDateFormat germanDateTimeFormat = new SimpleDateFormat(GERMAN_DATE_TIME);
	private static final SimpleDateFormat germanDateFormat = new SimpleDateFormat(GERMAN_DATE);

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

	public static Date toDateTime(String germanDateTimeString) {
		try {
			return germanDateTimeFormat.parse(germanDateTimeString);
		} catch (ParseException e) {
			LOG.warn(e.getMessage());
			return null;
		}
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
}

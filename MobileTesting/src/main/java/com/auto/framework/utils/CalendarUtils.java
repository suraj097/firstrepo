package com.auto.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * CalendarUtils contains utility methods to be used for operations related to date and time
 * 
 * @author naini.ghai
 */
public class CalendarUtils
{
	private static final String		MM_DD_YYYY				= "MM/dd/yyyy";
	public static final String		TIME_FORMAT				= "yyyy-MM-dd 'at' HH:mm:ss z";
	public static final String		TIME_FORMAT_REPORT_NAME	= "yyyy-MM-dd_HH_mm_ss";
	private long					startStamp;
	private static SimpleDateFormat	ft						= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private CalendarUtils()
	{

	}

	/**
	 * Returns string representation of a date
	 * @param date Date
	 * @param dateFormatString Date Pattern which you want to use for formatting the date
	 * @return String
	 * 
	 *         For more information regarding patterns , please refer
	 *         http://download.oracle.com/javase/1.5.0/docs/api/java/text/SimpleDateFormat.html
	 */
	public static String formatDate(Date date, String dateFormatString)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		return dateFormat.format(date);
	}

	/**
	 * Create Date object for specific DateFormatString
	 * @param dateStr - String from date object will be generated
	 * @param dateFormatString - The format of string
	 * @return - return Date Object from <code>dateStr</dateStr>
	 * @throws ParseException
	 */
	public static Date getDateFromFormat(String dateStr, String dateFormatString) throws ParseException
	{
		DateFormat df = new SimpleDateFormat(dateFormatString);
		return df.parse(dateStr);

	}

	/**
	 * Get current time in {@link CalendarUtils.#TIME_FORMAT} format
	 * @return
	 */
	public static String getCurrentTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
		return dateFormat.format(new Date());
	}

	/**
	 * Get current time in specified format.
	 * @param dateFormatString
	 * @return
	 */
	public static String getCurrentTime(String dateFormatString)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		return dateFormat.format(new Date());
	}

	/**
	 * Date Arithmetic function. Adds the specified (signed) amount of time to the given time field, based on the
	 * calendar's rules. For example, to subtract 5 days to a date of the calendar, you can achieve it by calling:
	 * <p/>
	 * add(new Date() ,Calendar.DATE, -5)
	 * <p/>
	 * To add 5 days to date, you can achieve it by calling: add(new Date() ,Calendar.DATE, 5) <br/>
	 * For details on values for <code>field</code>, refer {@link Calendar}
	 * 
	 * @param date Date
	 * @param field Field to which the addition and subtraction
	 * @param value Value which is to be added or subtracted
	 * @return Date
	 */
	public static Date add(Date date, int field, int value)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, value);
		return cal.getTime();
	}

	/**
	 * Date Arithmetic function. Adds the specified (signed) amount of year to the given time field, based on the
	 * calendar's rules. For example, to subtract 5 years to a date of the calendar, you can achieve it by calling:
	 * <p/>
	 * addYear(new Date() , -5)
	 * <p/>
	 * To add 5 years to date, you can achieve it by calling: addYear(new Date() ,5) <br/>
	 * 
	 * @param date Date
	 * @param value Value which is to be added or subtracted
	 * @return Date
	 */
	public static Date addYear(Date date, int value)
	{
		return add(date, Calendar.YEAR, value);

	}

	/**
	 * Date Arithmetic function. Adds the specified (signed) amount of months to the given time field, based on the
	 * calendar's rules. For example, to subtract 5 months to a date of the calendar, you can achieve it by
	 * calling:
	 * <p/>
	 * addMonth(new Date() , -5)
	 * <p/>
	 * To add 5 months to date, you can achieve it by calling: addMonth(new Date() ,5) <br/>
	 * 
	 * @param date Date
	 * @param value Value which is to be added or subtracted
	 * @return Date
	 */
	public static Date addMonth(Date date, int value)
	{
		return add(date, Calendar.MONTH, value);
	}

	/**
	 * Date Arithmetic function. Adds the specified (signed) amount of days to the given time field, based on the
	 * calendar's rules. For example, to subtract 5 days to a date of the calendar, you can achieve it by calling:
	 * <p/>
	 * addDay(new Date() , -5)
	 * <p/>
	 * To add 5 days to date, you can achieve it by calling: addDay(new Date() ,5) <br/>
	 * 
	 * @param date Date
	 * @param value Value which is to be added or subtracted
	 * @return Date
	 */
	public static Date addDay(Date date, int value)
	{
		return add(date, Calendar.DATE, value);
	}

	/**
	 * Date Arithmetic function. Adds the specified (signed) amount of hours to the given time field, based on the
	 * calendar's rules. For example, to subtract 5 hours to a date of the calendar, you can achieve it by calling:
	 * <p/>
	 * addHour(new Date() , -5)
	 * <p/>
	 * To add 5 hours to date, you can achieve it by calling: addHour(new Date() ,5) <br/>
	 * 
	 * @param date Date
	 * @param value Value which is to be added or subtracted
	 * @return Date
	 */
	public static Date addHour(Date date, int value)
	{
		return add(date, Calendar.HOUR_OF_DAY, value);
	}

	/**
	 * Date Arithmetic function. Adds the specified (signed) amount of minutes to the given time field, based on
	 * the calendar's rules. For example, to subtract 5 minutes to a date of the calendar, you can achieve it by
	 * calling:
	 * <p/>
	 * addHour(new Date() , -5)
	 * <p/>
	 * To add 5 minutes to date, you can achieve it by calling: addMinutes(new Date() ,5) <br/>
	 * 
	 * @param date Date
	 * @param value Value which is to be added or subtracted
	 * @return Date
	 */
	public static Date addMinutes(Date date, int value)
	{
		return add(date, Calendar.MINUTE, value);
	}

	/**
	 * Date Arithmetic function. Adds the specified (signed) amount of seconds to the given time field, based on
	 * the calendar's rules. For example, to subtract 5 seconds to a date of the calendar, you can achieve it by
	 * calling:
	 * <p/>
	 * addSeconds(new Date() , -5)
	 * <p/>
	 * To add 5 seconds to date, you can achieve it by calling: addSeconds(new Date() ,5) <br/>
	 * 
	 * @param date Date
	 * @param value Value which is to be added or subtracted
	 * @return Date
	 */
	public static Date addSeconds(Date date, int value)
	{
		return add(date, Calendar.SECOND, value);
	}

	/**
	 * Returns the day of the month represented by current date. The value returned is between 1 and 31
	 * representing the day of the month that contains or begins with the instant in time represented by the
	 * current, as interpreted in the local time zone.
	 * @param date Date
	 * @return the day of the month represented by current date
	 */
	public static int getDayOfMonth()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns the day of the week represented by current date. The returned value (0 = Sunday, 1 = Monday, 2 =
	 * Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday) represents the day of the week that contains
	 * or begins with the instant in time represented by current Date object, as interpreted in the local time
	 * zone.
	 * @param date Date
	 * @return the day of the week represented by current date
	 */
	public static int getDayOfWeek()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns the hour represented by current date. The returned value is a number (0 through 23) representing the
	 * hour within the day that contains or begins with the instant in time represented by current, as interpreted
	 * in the local time zone.
	 * @return the hour represented by current date
	 */
	public static int getHourOfDay()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Returns the number of minutes past the hour represented by current date, as interpreted in the local time
	 * zone. The value returned is between 0 and 59.
	 * @return the number of minutes past the hour represented by current date
	 */
	public static int getMinutes()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * 
	 * Returns the number of seconds past the minute represented by current date. The value returned is between 0
	 * and 61. The values 60 and 61 can only occur on those Java Virtual Machines that take leap seconds into
	 * account.
	 * @return the number of seconds past the minute represented by current date.
	 */
	public static int getSeconds()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.SECOND);
	}

	/**
	 * Returns month of the specified date
	 * 
	 * @param date Date
	 * @return the numeric value of the month for the specified date January = 1, February = 2 and so on.
	 */
	public static int getMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * Returns year of the specified date
	 * 
	 * @param date Date
	 * @return the numeric value of the year for the specified date.
	 */
	public static int getYear(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * Returns the day of the month of the specified date. The value returned is between 1 and 31 representing the
	 * day of the month that contains or begins with the instant in time represented by the current, as interpreted
	 * in the local time zone.
	 * 
	 * @param date Date
	 * @return the day of the month represented by specified date
	 */
	public static int getDay(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * This method will return the time duration between two dates in milliseconds, This method will take
	 * difference between only in: <code>Calendar.DAY_OF_MONTH</code>, <code>Calendar.HOUR_OF_DAY</code>,
	 * <code>Calendar.MINUTE</code>, <code>Calendar.SECOND</code>
	 * 
	 * @param startDate - Start date
	 * @param endDate - End Dates
	 * @return - time duration in millis between passed two dates
	 */
	public static long timeBetweenInMillSecs(Date startDate, Date endDate)
	{
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		endCal.add(Calendar.DAY_OF_MONTH, -startCal.get(Calendar.DAY_OF_MONTH));
		endCal.add(Calendar.HOUR_OF_DAY, -startCal.get(Calendar.HOUR_OF_DAY));
		endCal.add(Calendar.MINUTE, -startCal.get(Calendar.MINUTE));
		endCal.add(Calendar.SECOND, -startCal.get(Calendar.SECOND));

		return endCal.getTimeInMillis();
	}

	/**
	 * This method will return the time duration between two dates in specific format
	 * @param startDate - Start date
	 * @param endDate - End Dates
	 * @return - time duration in String between passed two dates
	 */
	public static String timeBetweenInMillSecs(Date startDate, Date endDate, String dateFormatString)
	{
		long diff = timeBetweenInMillSecs(startDate, endDate);
		return formatDate(new Date(diff), dateFormatString);
	}

	/**
	 * This method will return date in specific format.For example oldFormat can be "yyyy-MM-dd hh:mm:ss.SSS" and
	 * new Format can be "MM/dd/yyyy"
	 * @param date - date in String
	 * @param oldFormat - oldFormat in String
	 * @param newFormat - oldFormat in String
	 * @return - date String in new Format
	 */

	public static String getDateInNewFormat(String date, String oldFormat, String newFormat) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
		java.util.Date d1 = sdf.parse(date);
		sdf.applyPattern(newFormat);
		return sdf.format(d1);
	}

	public static String enddate()
	{
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, 1);
		Date date1 = now.getTime();
		SimpleDateFormat sd2 = new SimpleDateFormat(MM_DD_YYYY);
		return sd2.format(date1);
	}

	public static String bufferdate()
	{
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, 2);
		Date date1 = now.getTime();
		SimpleDateFormat sd2 = new SimpleDateFormat(MM_DD_YYYY);
		return sd2.format(date1);
	}

	public static String startdate()
	{
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -1);
		Date date1 = now.getTime();
		SimpleDateFormat sd2 = new SimpleDateFormat(MM_DD_YYYY);
		return sd2.format(date1);
	}

	public static String getTimeStamp()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		Date curDate = new Date();
		return sdf.format(curDate);
	}

	public void start()
	{
		startStamp = getTimeStampInMilliSecs();
	}

	public static long getTimeStampInMilliSecs()
	{
		return new Date().getTime();
	}

	public boolean expired(int seconds)
	{
		int difference = (int) ((getTimeStampInMilliSecs() - startStamp) / 1000);
		return difference > seconds;
	}

	public static String getCurrentDateTime()
	{
		Date dNow = new Date();
		return ft.format(dNow);
	}
}

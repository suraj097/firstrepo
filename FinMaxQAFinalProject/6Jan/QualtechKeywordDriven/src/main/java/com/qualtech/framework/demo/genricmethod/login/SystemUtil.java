package com.qualtech.framework.demo.genricmethod.login;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This Utility consists of the generic methods provided by JAVA.
 * @author gaurav.garg
 *
 */
public class SystemUtil {


	private SystemUtil(){

	}
	private static SystemUtil sysUtil;
	
	public static SystemUtil getObject(){

		if(sysUtil==null){
			sysUtil=new SystemUtil();
		}
		return sysUtil;
	}

	/**
	 * This method is use to get the Current Date 
	 * @param dateFormatPattern Need to provide the date format in which we want to retreive the data.
	 * @return It will return Today's date as String in M/d/yyyy format.
	 */
	public  String getCurrentDate(String dateFormatPattern){
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat formatter = new SimpleDateFormat(dateFormatPattern);
		return formatter.format(calendar.getTime());

	}

	/**
	 * This method is use to get the current time.
	 * @param dateFormatPattern Need to provide the date format in which we want to retreive the data.
	 * @return it will return the today's current time with date.
	 */
	public  String getTimeStamp(String dateFormatPattern){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormatPattern);
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * This method is use to get the date after/before the Today's date.
	 * @param laterDays It will have the integer value, if we want date later from today then provide positive value and if we want date prior to Today's date then provide negative value.
	 * @param dateFormatPattern Need to provide the date format in which we want to retreive the data.
	 * @return Return the date in M/d/yyyy format.
	 */
	public  String getLaterDateFromToday(int laterDays, String dateFormatPattern){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar.getTime());
		calendar.add(Calendar.DAY_OF_YEAR, laterDays);
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormatPattern);
		return formatter.format(calendar.getTime());
	}
}

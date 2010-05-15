package cn.edu.sjtu.ltlab.jcrawler.util;

import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * The utilities of Date operation related
 * @author Wenson
 *
 */

public class DateUtils {
	
	/**
	 * Time pattern of ISO-8601. e.g. 2009-12-09T18:38:21Z
	 * TODO: It can not work with SimpleDateFormat
	 */
	public static final String TIME_PATTERN_ISO8601 = "yyyy-MM-ddTmm:ss:Z";
	
	/**
	 * Time pattern of Sina. e.g. 2009年12月09日11:55
	 */
	public static final String TIME_PATTERN_SINA = "yyyy年MM月dd日HH:mm";
	
	
	/**
	 * Get a calendar from a time string.
	 * @param time
	 * 			The time string with the specific format.
	 * @param format
	 * 			The format the time string follows.
	 * @return The Calendar instance with the specified time.
	 */
	public static Calendar getCalendar(final String time, 
			final String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		
		Date d = null;
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		c.setTime(d);
		return c;
	}
	
	/**
	 * Get string of a calendar instance with specified pattern.
	 * @param cal
	 * 			The calendar instance which needs to format.
	 * @param pattern
	 * 			The specified date format.
	 * @return The time string of the specified pattern.
	 */
	public static String getString(final Calendar cal, final String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(cal.getTime());
	}
}

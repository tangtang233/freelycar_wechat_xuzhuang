package com.geariot.platform.freelycar_wechat.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author mxy940127
 *
 */
public class DateHandler {
	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Calendar setTimeToBeginningOfMonth(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public static Calendar setTimeToEndOfMonth(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar;
	}

	public static Calendar setTimeToBeginningOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public static Calendar setTimeToEndofDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar;
	}
	
	public static Calendar addValidMonth(Calendar calendar , int validMonth){
		calendar.add(Calendar.MONTH, validMonth);
		return calendar;
	}
	
	public static Calendar addValidYear(Calendar calendar, int validYear){
		calendar.add(Calendar.YEAR, validYear);
		return calendar;
	}
	
	public static boolean insuranceCheck(Calendar now, Calendar checkTime){
		if(now.before(checkTime)){
			now.add(Calendar.MONTH, 1);
			return now.after(checkTime);
		}
		return false;
	}
	
	public static int annualCheck(Calendar now, Calendar checkTime){
		//如果现在时间在年检日期之前
		if(now.before(checkTime)){
			now.add(Calendar.MONTH, 1);
				//如果现在时间加一月大于年检日期,返回1;
				if(now.after(checkTime)){
					return 1;
				}
				return 0;
		}
		//则 现在时间大于 年检日期
		else{
			checkTime.add(Calendar.YEAR, 2);
				//如果现在时间在两年后年检日期之前
				if(now.before(checkTime)){
					now.add(Calendar.MONTH, 1);
					if(now.after(checkTime)){
						return 2;
					}
					return 0;
				}
				else{
					checkTime.add(Calendar.YEAR, 2);
					//如果现在时间在四年后年检日期之前
					if(now.before(checkTime)){
						now.add(Calendar.MONTH, 1);
						if(now.after(checkTime)){
							return 4;
						}
						return 0;
					}
					else{
						checkTime.add(Calendar.YEAR, 2);
						//如果现在时间在6年后年检日期之前
						if(now.before(checkTime)){
							now.add(Calendar.MONTH, 1);
							if(now.after(checkTime)){
								return 6;
							}
							return 0;
						}
						return 0;
					}
				}
		}
	}
}

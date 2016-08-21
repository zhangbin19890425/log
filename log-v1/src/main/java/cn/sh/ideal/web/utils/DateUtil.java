package cn.sh.ideal.web.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	private static Calendar calendar = Calendar.getInstance();  
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 
     * 获取某年第一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getCurrYearFirst(int year){  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        Date currYearFirst = calendar.getTime();  
        return currYearFirst;  
    }  
    
    /**
     * 获取某年的第一秒的字符串
     * @param year
     * @return
     */
    public static String getYearFirstSecong(String year){
    	return year+"-01-01 00:00:00";
    }
    
    /**
     * 获取某年的最后一秒的字符串
     * @param year
     * @return
     */
    public static String getYearLastSecong(String year){
//    	return year+"-12-31 23:59:59";
    	return String.valueOf(Integer.valueOf(year)+1)+"-01-01 00:00:00";
    }
    
    /**
     * 获取某月的第一秒的字符串
     * @param year
     * @return
     */
    public static String getMonthFirstSecong(String year,String month){
    	return year+"-"+month+"-01 00:00:00";
    }
    
    /**
     * 获取某月的最后一秒的字符串
     * @param year
     * @return
     */
    @SuppressWarnings("deprecation")
	public static String getMonthLastSecong(String year,String month){
    	int day = new Date(Integer.valueOf(year),Integer.valueOf(month),0).getDate();
    	return getNextSecondTime(year+"-"+month+"-"+day+" 23:59:59");
    }
    
    /**
     * 获取某天的第一秒的字符串
     * @param year
     * @return
     */
    public static String getDayFirstSecong(String year,String month,String day){
    	return year+"-"+month+"-"+day+" 00:00:00";
    }
    
    /**
     * 获取某天的最后一秒的字符串
     * @param year
     * @return
     */
    public static String getDayLastSecong(String year,String month,String day){
//    	return year+"-"+month+"-"+day+" 23:59:59";
    	long time = 0;
    	try {
			time = sdf.parse(year+"-"+month+"-"+day+" 23:59:59").getTime()+1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return sdf.format(new Date(time));
    }
    
    /**
     * 获取某小时的第一秒的字符串
     * @param year
     * @return
     */
    public static String getHourFirstSecong(String year,String month,String day,String hour){
    	return year+"-"+month+"-"+day+" "+hour+":00:00";
    }
    
    /**
     * 获取某小时的最后一秒的字符串
     * @param year
     * @return
     */
    public static String getHourLastSecong(String year,String month,String day,String hour){
    	return getNextSecondTime(year+"-"+month+"-"+day+" "+hour+":59:59");
    }
    
    /**
     * 获取某分钟的最后一秒的字符串
     * @param year
     * @return
     */
    public static String getMinuteLastSecong(String year,String month,String day,String hour,String minute){
    	return getNextSecondTime(year+"-"+month+"-"+day+" "+hour+":"+minute+":59");
    }

    /**
     * 获取某分钟的第一秒的字符串
     * @param year
     * @return
     */
    public static String getMinuteFirstSecong(String year,String month,String day,String hour,String minute){
    	return year+"-"+month+"-"+day+" "+hour+":"+minute+":00";
    }

      
    /** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getCurrYearLast(int year){  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return currYearLast;  
    }  
    
	public static String millToDateString(String mill){
		Date date = new Date(Long.valueOf(mill));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date); 
	}
	
	//获取下一秒的时间
	public static String getNextSecondTime(String originTime){
		long time = 0;
    	try {
			time = sdf.parse(originTime).getTime()+1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return sdf.format(new Date(time));
	}
	
	/**
	 * 获取某个月有多少天
	 * @param month
	 * @return
	 */
	public static int getOneMonthDaysNum(int month,int year){
		Calendar time=Calendar.getInstance(); 
		time.clear(); 
		time.set(Calendar.YEAR,year); 
		//year年
		time.set(Calendar.MONTH,month-1);
		//Calendar对象默认一月为0,month月 
		return time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
	}
	
	
    public static void main(String[] args) {
//    	String[]a = [1467676800000, 1467763200000, 1467849600000, 1467936000000, 1468022400000, 1468108800000, 1468195200000, 1468281600000]
	System.out.println(sdf.format(new Date(1467849600000l)));
    }
}

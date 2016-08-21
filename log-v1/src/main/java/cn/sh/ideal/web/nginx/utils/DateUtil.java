package cn.sh.ideal.web.nginx.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String millToDateString(String mill){
		Date date = new Date(Long.valueOf(mill));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date); 
	}
	
}

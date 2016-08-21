package cn.sh.ideal.web.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import cn.sh.ideal.elasticsearch.constant.aggregation.EsAggregationParams;

public class InteralUtil {
	private static String secondPattern = "mm:ss";
	
	private static String minutePattern = "HH:mm";
	
	private static String hourPattern = "dd:HH";
	
	private static String dayPattern = "yyyy-MM-dd";
	
	private static String monthPattern = "yyyy-MM";
	
	private static String yearPattern = "yyyy";
	
	
	//获取适合的时间维度
	public static String getInteral(String fromDate, String toDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long from = 0;
		long to = 0;
		try {
			from = sdf.parse(fromDate).getTime();
			to = sdf.parse(toDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = to - from;
		//时间在一分钟内，按照秒来聚合
		if (time <= 60*1000) {
			return EsAggregationParams.date_histogram.second;
		}
		//时间在一分钟到一小时内，按分钟聚合
		else if (time > (60*1000) && time <= (1000*60*60)) {
			return EsAggregationParams.date_histogram.minute;
		}
		//时间在一小时到一天，按小时聚合
		else if (time > (1000*60*60) && time <= 86400000l) {
			return EsAggregationParams.date_histogram.hour;
		}
		//时间在一天到一个月，按天聚合
		else if (time > 86400000l && time <= 2678400000l) {
			return EsAggregationParams.date_histogram.day;
		}
		//时间在一个月到一年，按月聚合
		else if (time > 2678400000l && time <= 32140800000l) {
			return EsAggregationParams.date_histogram.month;
		}
		//时间大于一年，按年聚合
		else {
			return EsAggregationParams.date_histogram.year;
		}
	}
 
	//对于没有数据而缺失的时间段重新填充
	public static String getInteralDateString(String date, String interal) throws ParseException {
		String result = "";
		switch (interal) {     
		case EsAggregationParams.date_histogram.second:
				result = new SimpleDateFormat(secondPattern).format(Long.valueOf(date));
			break;
		case EsAggregationParams.date_histogram.minute:
			result = new SimpleDateFormat(minutePattern).format(Long.valueOf(date));
			break;
		case EsAggregationParams.date_histogram.hour:
			result = new SimpleDateFormat(hourPattern).format(Long.valueOf(date));
			break;
		case EsAggregationParams.date_histogram.day:
			result = new SimpleDateFormat(dayPattern).format(Long.valueOf(date));
//			String middleDay = String.valueOf(Integer.valueOf(result.split("-")[2]));
//			result = result.substring(0,result.length()-2)+middleDay;
			break;
		case EsAggregationParams.date_histogram.month:
			result = new SimpleDateFormat(monthPattern).format(Long.valueOf(date));
			break;
		case EsAggregationParams.date_histogram.year:
			result = new SimpleDateFormat(yearPattern).format(Long.valueOf(date));
			break;
		default:
			break;
		}
		return result;
	}
	
	//时间map补全
	public static Map<String,String> interalMap(Map<String,String> map,String interal,String fromDate,String toDate){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long start,to;
		try {
			start = simpleDateFormat.parse(fromDate).getTime();
			to = simpleDateFormat.parse(toDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}     
		switch (interal) {     
		case EsAggregationParams.date_histogram.second:
			for(long i=start;i<to;i+=1000){
				String time = new SimpleDateFormat(secondPattern).format(i);
				if(map.get(time)==null){
					map.put(time, "0");
				}
			}
			break;
		case EsAggregationParams.date_histogram.minute:
			for(long i=start;i<to;i+=60000){
				String time = new SimpleDateFormat(minutePattern).format(i);
				if(map.get(time)==null){
					map.put(time, "0");
				}
			}
			break;
		case EsAggregationParams.date_histogram.hour:
			for(long i=start;i<to;i+=3600000){
				String time = new SimpleDateFormat(hourPattern).format(i);
				if(map.get(time)==null){
					map.put(time, "0");
				}     
			}
			break;
		case EsAggregationParams.date_histogram.day:
			for(long i=start;i<to;i+=86400000){
				String time = new SimpleDateFormat(dayPattern).format(i);
				if(map.get(time)==null){
					map.put(time, "0");
				}
			}
			break;
		case EsAggregationParams.date_histogram.month:
			int fromYear = Integer.valueOf(fromDate.substring(0, 4));
			int fromMonth = Integer.valueOf(fromDate.substring(5, 7));
			int toYear = Integer.valueOf(toDate.substring(0, 4));
			int toMonth = Integer.valueOf(toDate.substring(5, 7));
			
			while(fromYear!=toYear && toMonth!=toYear){
				String fromMonthString = null;
				if(fromMonth>9){
					fromMonthString = String.valueOf(fromMonth);
				}else{
					fromMonthString = "0"+ String.valueOf(fromMonth);
				}
				String time = String.valueOf(fromYear)+"-"+fromMonthString;
				if(map.get(time)==null){
					map.put(time, "0");
				}
				if(fromMonth==12){
					fromYear++;
					fromMonth=1;
				}else{
					fromMonth++;
				}
				
			}
			break;
		case EsAggregationParams.date_histogram.year:
			for(long i=start;i<to;i+=31104000000l){
				String time = new SimpleDateFormat(yearPattern).format(i);
				if(map.get(time)==null){
					map.put(time, "0");
				}
			}
			break;
		default:
			break;
		}
		return transferTimeKey(map,interal);
	}
	
	//给时间完善格式
	public static Map<String,String> transferTimeKey(Map<String,String> map,String interal){
		Set<Entry<String, String>> entrySet = map.entrySet();
		Map<String,String> resultMap = new TreeMap<String, String>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
					String[] split1 = o1.split("[\u4e00-\u9fa5]");
					String[] split2 = o2.split("[\u4e00-\u9fa5]");
					for(int i=0;i<split1.length;i++){
						if(!split1[i].equals(split2[i])){
							return Integer.valueOf(split1[i])>Integer.valueOf(split2[i])?1:-1;
						}
					}
				return 0;
			}
		});
		switch (interal) {     
		case EsAggregationParams.date_histogram.second:
			for (Entry<String, String> entry : entrySet) {
				String finalkey = entry.getKey().replaceAll(":", "分")+"秒";
				resultMap.put(finalkey, entry.getValue());
			}
			break;
		case EsAggregationParams.date_histogram.minute:
			for (Entry<String, String> entry : entrySet) {
				String finalkey = entry.getKey().replaceAll(":", "时")+"分";
				resultMap.put(finalkey, entry.getValue());
			}
			break;
		case EsAggregationParams.date_histogram.hour:
			for (Entry<String, String> entry : entrySet) {
				String finalkey = entry.getKey().replaceAll(":", "日")+"时";
				resultMap.put(finalkey, entry.getValue());
			}
			break;
		case EsAggregationParams.date_histogram.day:
			for (Entry<String, String> entry : entrySet) {
				String[] split = entry.getKey().split("-");
				String finalkey = split[0]+"年"+split[1]+"月"+split[2]+"日";
				resultMap.put(finalkey, entry.getValue());
			}
			break;
		case EsAggregationParams.date_histogram.month:
			for (Entry<String, String> entry : entrySet) {
				String[] split = entry.getKey().split("-");
				String finalkey = split[0]+"年"+split[1]+"月";
				resultMap.put(finalkey, entry.getValue());
			}
			break;
		case EsAggregationParams.date_histogram.year:
			for (Entry<String, String> entry : entrySet) {
				String finalkey = entry.getKey()+"年";
				resultMap.put(finalkey, entry.getValue());
			}
			break;
		default:
			break;
		}
		return resultMap;
	}
	
	/**
	 * 同比分析时间段处理
	 * @param breakthrough
	 * @param time
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException 
	 */
	public static List<Map<String,String>> comparedAnylysisTime(String breakthrough,String time,String start,String end) throws ParseException{
		String startTime = null,endTime = null;
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		switch (breakthrough) {
		case "month":
			for(int year=Integer.valueOf(start);year<=Integer.valueOf(end);year++){
				Map<String,String> resultMap = new HashMap<String, String>();
				startTime = year + "-" + time + "-01 00:00:00";
				endTime = year + "-" + time + "-" + DateUtil.getOneMonthDaysNum(Integer.valueOf(time), year)+" 23:59:59";
				resultMap.put("startTime", startTime);  
				resultMap.put("endTime", endTime);
				resultList.add(resultMap);
			}
			break;
        case "day":
        	int startYear = Integer.valueOf(start.split("-")[0]);
        	int startMonth = Integer.valueOf(start.split("-")[1]);
        	int toYear = Integer.valueOf(end.split("-")[0]);
        	int toMonth = Integer.valueOf(end.split("-")[1]);
        	while(true){
        		if(startYear>toYear || (startYear==toYear&&startMonth>toMonth)){
        			break;
        		}
				Map<String,String> resultMap = new HashMap<String, String>();
				startTime = startYear + "-" + (startMonth>9?startMonth:("0"+startMonth)) + "-" + time + " 00:00:00";
				endTime = startYear + "-" + (startMonth>9?startMonth:("0"+startMonth)) + "-" + time + " 23:59:59";
				resultMap.put("startTime", startTime);
				resultMap.put("endTime", endTime);
				resultList.add(resultMap);
				if(startMonth!=12){
					startMonth++;
				}
				else{
					startYear++;
					startMonth = 1;
				}
        	}
			
			break;
        case "hour":
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	startTime = start + " " + time + ":00:00";
        	endTime = start + " " + time + ":59:59";
        	Map<String,String> resultMap = new HashMap<String,String>();
        	resultMap.put("startTime", startTime);
        	resultMap.put("endTime", endTime);
        	resultList.add(resultMap);
        	long startMill = sdf.parse(startTime).getTime();
        	long endMill = sdf.parse(endTime).getTime();
        	long finishTime = sdf.parse(end+" 00:00:00").getTime();
        	long oneDay = 24*60*60*1000l;
        	while(true){
        		resultMap = new HashMap<String,String>();
        		startMill+=oneDay;
        		endMill+=oneDay;
        		resultMap.put("startTime", sdf.format(startMill));
            	resultMap.put("endTime", sdf.format(endMill));
            	resultList.add(resultMap);
            	if(startMill>finishTime){
            		break;
            	}
        	}
	break;
		default:
			break;
		}
		return resultList;
		
	}
	
	//获取同比的时间
	public static String getComparedTimeString(String breakthrough,String time){
		switch (breakthrough) {
		case "month":
			return time.substring(0,7);
        case "day":
        	//2016-09
        	return time.substring(0,10);
        case "hour":
        	return time.substring(0,13) + "点";
		default:
			break;
		}
		return time;
		
	}
	
	
	public static void main(String[] args) throws ParseException {
		comparedAnylysisTime("hour", "03", "2015-08-04", "2015-08-09");
		
		String date = "1467864000000";
		
		String format = new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(date));
		
		System.out.println(format);
	}
}

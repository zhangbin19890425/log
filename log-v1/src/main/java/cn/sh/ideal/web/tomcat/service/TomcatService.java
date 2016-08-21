package cn.sh.ideal.web.tomcat.service;

import java.util.List;
import java.util.Map;

import cn.sh.ideal.web.tomcat.entity.TomcatAccessLogEntity;

public interface TomcatService {
	//错误页面统计
	Map<String, String> getWrongPageCount(String fromDate,String toDate,String ip);
    //访问频次统计
	Map<String, String> getVisitFrequently(String fromDate,String toDate,String ip);
	//按照时段聚合数据
	Map<String, String> getDateHisgram(String fromDate,String toDate,String ip,String drillingTime,String lastDrillingTime,String analysisType);
	//页面处理时间排名
	Map<String, String> getDealTime(String fromDate,String toDate,String ip);
    //服务器分布图
	Map<String, String> getGeoIpAggregation(String fromDate, String toDate,String analysisType);
	//明细信息钻取
	List<TomcatAccessLogEntity> detailInfo(String fromDate, String toDate,String detailType,String searchWord,String ip);
    //状态码统计
	Map<String, String> statusAnalysis(String fromDate, String toDate,String ip,String url);
 
	//同比分析
	Map<String,String> compared(String ip,String breakthrough,String time,String start,String end, String analysisType);
}
package cn.sh.ideal.web.tomcat.service.impl;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;

import cn.sh.ideal.elasticsearch.constant.aggregation.EsAggregationParams;
import cn.sh.ideal.elasticsearch.constant.aggregation.EsAggregationType;
import cn.sh.ideal.elasticsearch.handler.ElasticSearchHandler;
import cn.sh.ideal.web.tomcat.constants.TomcatAccessLogFields;
import cn.sh.ideal.web.tomcat.constants.TomcatIndexNames;
import cn.sh.ideal.web.tomcat.constants.TomcatTypes;
import cn.sh.ideal.web.tomcat.entity.TomcatAccessLogEntity;
import cn.sh.ideal.web.tomcat.service.TomcatService;
import cn.sh.ideal.web.tomcat.utils.ResultMapUtil;
import cn.sh.ideal.web.tomcat.utils.StringUtil;
import cn.sh.ideal.web.utils.DateUtil;
import cn.sh.ideal.web.utils.InteralUtil;

public class TomcatServiceImpl implements TomcatService {

	@Resource(name = "es")
	private ElasticSearchHandler es;

	// geoIp分布图
	@Override
	public Map<String, String> getGeoIpAggregation(String fromDate, String toDate, String analysisType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, TomcatAccessLogFields.localhost_ip);
		// 按localhost_ip进行聚合
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(EsAggregationType.term, params);

		// 统计维度聚合条件
		AggregationBuilder analysisAggregationBuilder = null;

		Map<String, String> analysisParams = new HashMap<String, String>();

		RangeQueryBuilder dealDateQueryBuilder = es.dealDate(fromDate, toDate, TomcatAccessLogFields.visit_time);

		// 查询builder
		QueryBuilder finalQueryBuilder = null;

		// 聚合builder
		AggregationBuilder finalAggregationBuilder = null;
		switch (analysisType) {
		// 错误页面
		case "errorPage":
			QueryBuilder filter = QueryBuilders.rangeQuery(TomcatAccessLogFields.http_status).from("201");
			finalQueryBuilder = QueryBuilders.boolQuery().must(dealDateQueryBuilder).must(filter);
			finalAggregationBuilder = aggregationBuilder;
			break;
		// 响应时间
		case "responseTime":
			analysisParams.put(EsAggregationParams.term, TomcatAccessLogFields.visit_contined_time_mill);
			MetricsAggregationBuilder metricsAggregationBuilder = es.getMetricsAggregationBuilder(EsAggregationType.avg,
					analysisParams);
			finalQueryBuilder = dealDateQueryBuilder;
			finalAggregationBuilder = aggregationBuilder.subAggregation(metricsAggregationBuilder);
			break;
		// 访问次数
		case "accessTimes":
			finalQueryBuilder = dealDateQueryBuilder;
			finalAggregationBuilder = aggregationBuilder;
			break;
		default:
			break;
		}

		Map<String, String> distribution = new HashMap<String, String>();

		SearchResponse response = es.getClient().prepareSearch(TomcatIndexNames.tomcat_access_log)
				.setTypes(TomcatTypes.tomcat_access_log).setQuery(finalQueryBuilder).addAggregation(finalAggregationBuilder)
				.execute().actionGet();

		if (analysisType.equals("responseTime")) {
			Aggregations aggs = response.getAggregations();
			Terms terms = aggs.get("result");
			List<Bucket> bucket = terms.getBuckets();
			for (Bucket bu : bucket) {
				Avg max = bu.getAggregations().get("avg");
				double value = max.getValue();
				distribution.put(bu.getKeyAsString(), String.valueOf(value));
			}
		} else {
			Aggregations aggs = response.getAggregations();
			Terms terms = aggs.get("result");
			List<Bucket> bucket = terms.getBuckets();
			for (Bucket bu : bucket) {
				distribution.put(bu.getKeyAsString(), String.valueOf(bu.getDocCount()));
			}
		}
		return distribution;
	}

	// 错误页面统计
	@Override
	public Map<String, String> getWrongPageCount(String fromDate, String toDate, String ip) {
		QueryBuilder queryBuilder = QueryBuilders.rangeQuery(TomcatAccessLogFields.http_status).from("201");
		// es.getQueryBuilder("404", TomcatAccessLogFields.http_status);
		TermQueryBuilder termQuery = QueryBuilders.termQuery(TomcatAccessLogFields.localhost_ip, ip);
		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationType.term, TomcatAccessLogFields.visit_url);
		// params.put(EsAggregationParams.size, "8");
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(EsAggregationType.term, params);
		Map<String, String> distribution = new HashMap<String, String>();
		SearchResponse response = es
				.getClient()
				.prepareSearch(TomcatIndexNames.tomcat_access_log)
				.setTypes(TomcatTypes.tomcat_access_log)
				.setQuery(
						QueryBuilders.boolQuery().must(queryBuilder).must(termQuery)
								.must(es.dealDate(fromDate, toDate, TomcatAccessLogFields.visit_time)))
				.addAggregation(aggregationBuilder).execute().actionGet();
		Aggregations aggs = response.getAggregations();
		Terms terms = aggs.get("result");
		List<Bucket> bucket = terms.getBuckets();
		for (Bucket bu : bucket) {
			distribution.put(bu.getKeyAsString(), String.valueOf(bu.getDocCount()));
		}

		return distribution;
	}

	// 页面访问频次统计
	@Override
	public Map<String, String> getVisitFrequently(String fromDate, String toDate, String ip) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationType.term, TomcatAccessLogFields.visit_url);
		// params.put(EsAggregationParams.size, "8");
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(EsAggregationType.term, params);

		Map<String, String> distribution = new HashMap<String, String>();
		SearchResponse response = es
				.getClient()
				.prepareSearch(TomcatIndexNames.tomcat_access_log)
				.setTypes(TomcatTypes.tomcat_access_log)
				.setQuery(
						QueryBuilders.boolQuery().must(QueryBuilders.termQuery(TomcatAccessLogFields.localhost_ip, ip))
								.must(es.dealDate(fromDate, toDate, TomcatAccessLogFields.visit_time)))
				.addAggregation(aggregationBuilder).execute().actionGet();
		Aggregations aggs = response.getAggregations();
		Terms terms = aggs.get("result");
		List<Bucket> bucket = terms.getBuckets();
		for (Bucket bu : bucket) {
			distribution.put(bu.getKeyAsString(), String.valueOf(bu.getDocCount()));
		}

		return distribution;
	}

	// 按时段聚合数据
	@Override
	public Map<String, String> getDateHisgram(String fromDate, String toDate, String ip, String drillingTime,
			String lastDrillingTime, String analysisType) {
		// 判断是否是钻取动作，如是，则改变开始结束时间为钻取时间
		if (drillingTime != null) {
			if (drillingTime.endsWith("年")) {
				String year = drillingTime.replaceAll("年", "");
				fromDate = DateUtil.getYearFirstSecong(year);
				toDate = DateUtil.getYearLastSecong(year);
			} else if (drillingTime.endsWith("月")) {
				String year = drillingTime.split("[\u4e00-\u9fa5]")[0];
				String month = drillingTime.split("[\u4e00-\u9fa5]")[1];
				fromDate = DateUtil.getMonthFirstSecong(year, month);
				toDate = DateUtil.getMonthLastSecong(year, month);
			} else if (drillingTime.endsWith("日")) {
				String year = drillingTime.split("[\u4e00-\u9fa5]")[0];
				String month = drillingTime.split("[\u4e00-\u9fa5]")[1];
				String day = drillingTime.split("[\u4e00-\u9fa5]")[2];
				fromDate = DateUtil.getDayFirstSecong(year, month, day);
				toDate = DateUtil.getDayLastSecong(year, month, day);
			} else if (drillingTime.endsWith("时")) {
				String year, month;
				if (!"".equals(lastDrillingTime) && lastDrillingTime != null) {
					year = lastDrillingTime.split("[\u4e00-\u9fa5]")[0];
					month = lastDrillingTime.split("[\u4e00-\u9fa5]")[1];
				} else {
					year = toDate.substring(0, 4);
					month = toDate.substring(5, 7);
				}
				String hour = drillingTime.split("[\u4e00-\u9fa5]")[1];
				String day = drillingTime.split("[\u4e00-\u9fa5]")[0];
				fromDate = DateUtil.getHourFirstSecong(year, month, day, hour);
				toDate = DateUtil.getHourLastSecong(year, month, day, hour);
			} else if (drillingTime.endsWith("分")) {
				String year, month, day;
				if (!"".equals(lastDrillingTime) && lastDrillingTime != null) {
					year = lastDrillingTime.split("[\u4e00-\u9fa5]")[0];
					month = lastDrillingTime.split("[\u4e00-\u9fa5]")[1];
					day = lastDrillingTime.split("[\u4e00-\u9fa5]")[2];
				} else {
					year = toDate.substring(0, 4);
					month = toDate.substring(5, 7);
					day = toDate.substring(8, 10);
				}
				String hour = drillingTime.split("[\u4e00-\u9fa5]")[0];
				String minute = drillingTime.split("[\u4e00-\u9fa5]")[1];
				fromDate = DateUtil.getMinuteFirstSecong(year, month, day, hour, minute);
				toDate = DateUtil.getMinuteLastSecong(year, month, day, hour, minute);
			}
		}
		Map<String, String> middleMap = new HashMap<String, String>();

		// 时间聚合维度
		String interalParam = null;
		interalParam = InteralUtil.getInteral(fromDate, toDate);

		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, TomcatAccessLogFields.visit_time);
		params.put(EsAggregationParams.date_histogram.inteval, interalParam);

		Map<String, String> avgParams = new HashMap<String, String>();
		avgParams.put(EsAggregationParams.term, TomcatAccessLogFields.visit_contined_time_mill);

		// 查询builder
		QueryBuilder finalQueryBuilder = null;

		// 聚合builder
		AggregationBuilder finalAggregationBuilder = null;

		switch (analysisType) {
		// 错误页面
		case "errorPage":
			finalQueryBuilder = QueryBuilders.boolQuery().must(es.dealDate(fromDate, toDate, TomcatAccessLogFields.visit_time))
					.must(QueryBuilders.termQuery(TomcatAccessLogFields.localhost_ip, ip))
					.must(QueryBuilders.rangeQuery(TomcatAccessLogFields.http_status).from("201"));
			finalAggregationBuilder = es.getAggregationBuilder(EsAggregationType.date_histogram, params);
			break;
		// 响应时间
		case "responseTime":
			finalQueryBuilder = QueryBuilders.boolQuery().must(es.dealDate(fromDate, toDate, TomcatAccessLogFields.visit_time))
					.must(QueryBuilders.termQuery(TomcatAccessLogFields.localhost_ip, ip));
			finalAggregationBuilder = es.getAggregationBuilder(EsAggregationType.date_histogram, params).subAggregation(
					es.getMetricsAggregationBuilder(EsAggregationType.avg, avgParams));
			break;
		// 访问次数
		case "accessTimes":
			finalQueryBuilder = QueryBuilders.boolQuery().must(es.dealDate(fromDate, toDate, TomcatAccessLogFields.visit_time))
					.must(QueryBuilders.termQuery(TomcatAccessLogFields.localhost_ip, ip));
			finalAggregationBuilder = es.getAggregationBuilder(EsAggregationType.date_histogram, params);
			break;
		default:
			break;
		}

		SearchResponse response = es.getClient().prepareSearch(TomcatIndexNames.tomcat_access_log)
				.setTypes(TomcatTypes.tomcat_access_log).setQuery(finalQueryBuilder).addAggregation(finalAggregationBuilder)
				.execute().actionGet();
		Map<String, String> resultMap = new HashMap<String, String>();
		if (analysisType.equals("responseTime")) {
			Histogram histogram = response.getAggregations().get("result");

			for (Histogram.Bucket entry : histogram.getBuckets()) {
				String keyAsString = "";
				try {
					keyAsString = InteralUtil.getInteralDateString(entry.getKeyAsString(), interalParam);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Avg avg = entry.getAggregations().get("avg");
				String value = String.valueOf(avg.getValue());
				if ("NaN".equals(value)) {
					value = "0";
				}
				middleMap.put(keyAsString, value);
			}
			resultMap = InteralUtil.interalMap(middleMap, interalParam, fromDate, toDate);
		} else {
			Histogram histogram = response.getAggregations().get("result");

			for (Histogram.Bucket entry : histogram.getBuckets()) {
				String keyAsString = "";
				try {
					keyAsString = InteralUtil.getInteralDateString(entry.getKeyAsString(), interalParam);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String value = String.valueOf(entry.getDocCount());
				if ("NaN".equals(value)) {
					value = "0";
				}
				middleMap.put(keyAsString, value);
			}
			resultMap = InteralUtil.interalMap(middleMap, interalParam, fromDate, toDate);
		}
		return resultMap;
	}

	// 页面处理时间排名
	@Override
	public Map<String, String> getDealTime(String fromDate, String toDate, String ip) {
		Map<String, String> resultMap = new HashMap<String, String>();

		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, TomcatAccessLogFields.visit_url);
		params.put(EsAggregationParams.size, "10");

		Map<String, String> avgParams = new HashMap<String, String>();
		avgParams.put(EsAggregationParams.term, TomcatAccessLogFields.visit_contined_time_mill);

		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(TomcatAccessLogFields.localhost_ip, ip);

		SearchResponse response = es
				.getClient()
				.prepareSearch(TomcatIndexNames.tomcat_access_log)
				.setTypes(TomcatTypes.tomcat_access_log)
				.setQuery(
						QueryBuilders.boolQuery().must(termQueryBuilder)
								.must(es.dealDate(fromDate, toDate, TomcatAccessLogFields.visit_time)))
				.addAggregation(
						es.getAggregationBuilder(EsAggregationType.term, params).subAggregation(
								es.getMetricsAggregationBuilder(EsAggregationType.avg, avgParams))).execute().actionGet();
		Terms terms = response.getAggregations().get("result");

		for (Terms.Bucket entry : terms.getBuckets()) {
			String key = (String) entry.getKey(); // Term
			Avg avg = entry.getAggregations().get("avg");
			String value = String.valueOf(avg.getValue());
			resultMap.put(key, value);
		}
		return resultMap;
	}

	@Override
	public List<TomcatAccessLogEntity> detailInfo(String fromDate, String toDate, String detailType, String searchWord,
			String ip) {
		QueryBuilder queryBuilder = null;

		switch (detailType) {
		// 错误页面
		case "errorPage":
			if (StringUtil.isEmpty(searchWord)) {
				queryBuilder = QueryBuilders.rangeQuery(TomcatAccessLogFields.http_status).from("201");
			} else {
				String url = searchWord.split("\\|")[1];
				String errorCode = searchWord.split("\\|")[0];
				queryBuilder = QueryBuilders.boolQuery()
						.must(QueryBuilders.termQuery(TomcatAccessLogFields.http_status, errorCode))
						.must(QueryBuilders.termQuery(TomcatAccessLogFields.visit_url, url));
			}
			break;
		// 访问频次
		case "Frequently":
			queryBuilder = QueryBuilders.termQuery(TomcatAccessLogFields.visit_url, searchWord);
			break;
		// 响应时间
		case "VisitTime":
			queryBuilder = null;
			break;
		// 处理时间
		case "DealTime":
			queryBuilder = QueryBuilders.termQuery(TomcatAccessLogFields.visit_url, searchWord);
			break;
		default:
			break;
		}
		List<Map<String, Object>> detailInfo = es.detailInfo(TomcatIndexNames.tomcat_access_log, TomcatTypes.tomcat_access_log,
				TomcatAccessLogFields.visit_time, fromDate, toDate, queryBuilder, ip);

		List<TomcatAccessLogEntity> resultList = new ArrayList<TomcatAccessLogEntity>(200);

		for (Map<String, Object> map : detailInfo) {
			TomcatAccessLogEntity entity = (TomcatAccessLogEntity) ResultMapUtil.mapToObject(map, TomcatAccessLogEntity.class);
			resultList.add(entity);
		}
		return resultList;
	}

	// 各错误码统计分析
	@Override
	public Map<String, String> statusAnalysis(String fromDate, String toDate, String ip, String url) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> aggregationParams = new HashMap<String, String>();
		aggregationParams.put(EsAggregationParams.term, TomcatAccessLogFields.http_status);
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(EsAggregationParams.term, aggregationParams);
		BoolQueryBuilder must = QueryBuilders.boolQuery().must(es.getQueryBuilder(url, TomcatAccessLogFields.visit_url))
				.must(es.getQueryBuilder(ip, TomcatAccessLogFields.localhost_ip))
				.must(QueryBuilders.rangeQuery(TomcatAccessLogFields.http_status).from("201"));
		SearchResponse response = es.getClient().prepareSearch(TomcatIndexNames.tomcat_access_log)
				.setTypes(TomcatTypes.tomcat_access_log).setQuery(must).addAggregation(aggregationBuilder).execute()
				.actionGet();

		Aggregations aggs = response.getAggregations();
		Terms terms = aggs.get("result");
		List<Bucket> bucket = terms.getBuckets();
		for (Bucket bu : bucket) {
			long docCount = bu.getDocCount();
			resultMap.put(bu.getKeyAsString(), String.valueOf(docCount));
		}
		return resultMap;
	}

	public static void main(String[] args) throws UnknownHostException {
		ElasticSearchHandler es = new ElasticSearchHandler("elasticsearch", "192.168.147.132", 9300);
		String analysisType = "accessTimes";
		String breakthrough = "hour";
		Map<String,String> resultMap = new HashMap<String, String>();
		List<Map<String, String>> comparedAnylysisTime = new ArrayList<Map<String, String>>();
		try {
			comparedAnylysisTime = InteralUtil.comparedAnylysisTime("hour", "07", "2016-07-24", "2016-07-26");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateRangeBuilder rangeBuilder = AggregationBuilders.dateRange("result").field(TomcatAccessLogFields.visit_time)
				.format("yyyy-MM-dd HH:mm:ss");
		for (Map<String, String> map : comparedAnylysisTime) {
			String startTime = map.get("startTime");
			String endTime = map.get("endTime");
			rangeBuilder.addRange(startTime, endTime);
		}
		// 查询builder
				QueryBuilder finalQueryBuilder = null;

				// 聚合builder
				AggregationBuilder finalAggregationBuilder = null;
				QueryBuilder queryBuilder = es.getQueryBuilder("10.4.231.62", TomcatAccessLogFields.localhost_ip);
		switch (analysisType) {
		// 错误页面
				case "errorPage":
					QueryBuilder queryBuilder2 = QueryBuilders.rangeQuery(TomcatAccessLogFields.http_status).from("201");
					finalQueryBuilder = QueryBuilders.boolQuery().must(queryBuilder2).must(queryBuilder);
					finalAggregationBuilder = rangeBuilder;
					break;
				// 响应时间
				case "responseTime": 
					Map<String, String> analysisParams = new HashMap<String, String>();
					analysisParams.put(EsAggregationParams.term, TomcatAccessLogFields.visit_contined_time_mill);
					MetricsAggregationBuilder metricsAggregationBuilder = es.getMetricsAggregationBuilder(EsAggregationType.avg,
							analysisParams);
					finalQueryBuilder = queryBuilder;
					finalAggregationBuilder = rangeBuilder.subAggregation(metricsAggregationBuilder);
					break;
				// 访问次数
				case "accessTimes":
					finalQueryBuilder = queryBuilder;
					finalAggregationBuilder = rangeBuilder;
					break;
				default:
					break;
				}
		
		SearchResponse response = es.getClient().prepareSearch(TomcatIndexNames.tomcat_access_log)
				.setTypes(TomcatTypes.tomcat_access_log)
				.setQuery(finalQueryBuilder).addAggregation(finalAggregationBuilder)
				.execute().actionGet();

		if (analysisType.equals("responseTime")) {
			Range aggs = (Range) response.getAggregations().get("result");
			//2015-01
			for (Range.Bucket entry : aggs.getBuckets()) {     
			    String key = InteralUtil.getComparedTimeString(breakthrough, entry.getKeyAsString());
			    Avg avg = entry.getAggregations().get("avg");
			    String avgValue = String.valueOf(avg.getValue());
			    if("NaN".equals(avgValue)){
			    	avgValue = "0";
			    }
			    resultMap.put(key, avgValue);
			}
		}else{
			Range aggs = (Range) response.getAggregations().get("result");
			for (Range.Bucket entry : aggs.getBuckets()) {     
			    String key = InteralUtil.getComparedTimeString(breakthrough, entry.getKeyAsString());
			    String docCount = String.valueOf(entry.getDocCount());
			    resultMap.put(key, docCount);
			}
		}
		} 

	// 同比分析
	@Override
	public Map<String, String> compared(String ip, String breakthrough, String time, String start, String end,String analysisType) {		
		Map<String,String> resultMap = new TreeMap<String, String>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if(o1.equals(o2)){
					return 0;
				}
				int year1 = Integer.valueOf(o1.split("-")[0]);
				int year2 = Integer.valueOf(o2.split("-")[0]);
				if(year1!=year2){
					return year1>year2?1:-1;
				}
				int month1 = Integer.valueOf(o1.split("-")[1]);
				int month2 = Integer.valueOf(o2.split("-")[1]);
				if(month1!=month2){
					return month1>month2?1:-1;
				} 
				int day1 = Integer.valueOf(o1.split("-")[2].substring(0,2));
				int day2 = Integer.valueOf(o2.split("-")[2].substring(0,2));
				if(day1!=day2){
					return day1>day2?1:-1;
				} 
				return 0;
			}
			
		});
	List<Map<String, String>> comparedAnylysisTime = new ArrayList<Map<String, String>>();
	try {
		comparedAnylysisTime = InteralUtil.comparedAnylysisTime(breakthrough, time, start, end);
	} catch (ParseException e) {
		e.printStackTrace();
	}
	DateRangeBuilder rangeBuilder = AggregationBuilders.dateRange("result").field(TomcatAccessLogFields.visit_time)
			.format("yyyy-MM-dd HH:mm:ss");
	for (Map<String, String> map : comparedAnylysisTime) {
		String startTime = map.get("startTime");
		String endTime = map.get("endTime");
		rangeBuilder.addRange(startTime, endTime);
	}
	// 查询builder
			QueryBuilder finalQueryBuilder = null;

			// 聚合builder
			AggregationBuilder finalAggregationBuilder = null;
			QueryBuilder queryBuilder = es.getQueryBuilder(ip, TomcatAccessLogFields.localhost_ip);
	switch (analysisType) {
	// 错误页面
			case "errorPage":
				QueryBuilder queryBuilder2 = QueryBuilders.rangeQuery(TomcatAccessLogFields.http_status).from("201");
				finalQueryBuilder = QueryBuilders.boolQuery().must(queryBuilder2).must(queryBuilder);
				finalAggregationBuilder = rangeBuilder;
				break;
			// 响应时间
			case "responseTime": 
				Map<String, String> analysisParams = new HashMap<String, String>();
				analysisParams.put(EsAggregationParams.term, TomcatAccessLogFields.visit_contined_time_mill);
				MetricsAggregationBuilder metricsAggregationBuilder = es.getMetricsAggregationBuilder(EsAggregationType.avg,
						analysisParams);
				finalQueryBuilder = queryBuilder;
				finalAggregationBuilder = rangeBuilder.subAggregation(metricsAggregationBuilder);
				break;
			// 访问次数
			case "accessTimes":
				finalQueryBuilder = queryBuilder;
				finalAggregationBuilder = rangeBuilder;
				break;
			default:
				break;
			}
	
	SearchResponse response = es.getClient().prepareSearch(TomcatIndexNames.tomcat_access_log)
			.setTypes(TomcatTypes.tomcat_access_log)
			.setQuery(finalQueryBuilder).addAggregation(finalAggregationBuilder)
			.execute().actionGet();

	if (analysisType.equals("responseTime")) {
		Range aggs = (Range) response.getAggregations().get("result");
		//2015-01
		for (Range.Bucket entry : aggs.getBuckets()) {     
		    String key = InteralUtil.getComparedTimeString(breakthrough, entry.getKeyAsString());
		    Avg avg = entry.getAggregations().get("avg");
		    String avgValue = String.valueOf(avg.getValue());
		    if("NaN".equals(avgValue)){
		    	avgValue = "0";
		    }
		    resultMap.put(key, avgValue);
		}
	}else{
		Range aggs = (Range) response.getAggregations().get("result");
		for (Range.Bucket entry : aggs.getBuckets()) {     
		    String key = InteralUtil.getComparedTimeString(breakthrough, entry.getKeyAsString());
		    String docCount = String.valueOf(entry.getDocCount());
		    resultMap.put(key, docCount);
		}
	}
	return resultMap;
	}

}

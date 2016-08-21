package cn.sh.ideal.web.nginx.service.impl;

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
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;

import cn.sh.ideal.elasticsearch.constant.aggregation.EsAggregationParams;
import cn.sh.ideal.elasticsearch.constant.aggregation.EsAggregationType;
import cn.sh.ideal.elasticsearch.handler.ElasticSearchHandler;
import cn.sh.ideal.web.nginx.constants.NginxAccessLogFields;
import cn.sh.ideal.web.nginx.constants.NginxIndexNames;
import cn.sh.ideal.web.nginx.constants.NginxTypes;
import cn.sh.ideal.web.nginx.entity.NginxAccessLogEntity;
import cn.sh.ideal.web.nginx.service.NginxService;
import cn.sh.ideal.web.tomcat.constants.TomcatAccessLogFields;
import cn.sh.ideal.web.tomcat.constants.TomcatIndexNames;
import cn.sh.ideal.web.tomcat.constants.TomcatTypes;
import cn.sh.ideal.web.tomcat.utils.ResultMapUtil;
import cn.sh.ideal.web.utils.DateUtil;
import cn.sh.ideal.web.utils.InteralUtil;

public class NginxServiceImpl implements NginxService {
	@Resource(name = "es")
	private ElasticSearchHandler es;

	// geoIp分布图
	@Override
	public Map<String, String> getGeoIpAggregation(String fromDate,
			String toDate, String analysisType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, NginxAccessLogFields.upstream_addr);
		// 按host进行聚合
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(
				EsAggregationType.term, params);

		// 统计维度聚合条件
		AggregationBuilder analysisAggregationBuilder = null;

		Map<String, String> analysisParams = new HashMap<String, String>();

		RangeQueryBuilder dealDateQueryBuilder = es.dealDate(fromDate, toDate,
				NginxAccessLogFields.timestamp);

		// 查询builder
		QueryBuilder finalQueryBuilder = null;

		// 聚合builder
		AggregationBuilder finalAggregationBuilder = null;
		switch (analysisType) {
		// 错误页面
		case "errorPage":
			QueryBuilder filter = QueryBuilders.rangeQuery(
					NginxAccessLogFields.status).from("201");
			finalQueryBuilder = QueryBuilders.boolQuery()
					.must(dealDateQueryBuilder).must(filter);
			finalAggregationBuilder = aggregationBuilder;
			break;
		// 响应时间
		case "responseTime":
			analysisParams.put(EsAggregationParams.term,
					NginxAccessLogFields.upstreap_response_time);
			MetricsAggregationBuilder metricsAggregationBuilder = es
					.getMetricsAggregationBuilder(EsAggregationType.avg,
							analysisParams);
			finalQueryBuilder = dealDateQueryBuilder;
			// finalAggregationBuilder =
			// aggregationBuilder.subAggregation(metricsAggregationBuilder);
			finalAggregationBuilder = aggregationBuilder
					.subAggregation(AggregationBuilders.avg("avg").script(
							new Script("nginxResponseTime", ScriptType.FILE,
									null, null)));
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

		SearchResponse response = es.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log)
				.setQuery(finalQueryBuilder)
				.addAggregation(finalAggregationBuilder).execute().actionGet();

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
				distribution.put(bu.getKeyAsString(),
						String.valueOf(bu.getDocCount()));
			}
		}
		return distribution;
	}

	// 每秒请求数
	@Override
	public Map<String, String> getResNumSecondAggregation(String fromDate,
			String toDate, String ip) {
		Map<String, String> resultMap = new TreeMap<String, String>(
				new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						int minute1 = Integer.valueOf(o1.split(":")[1]);
						int minute2 = Integer.valueOf(o2.split(":")[1]);
						if (minute1 != minute2) {
							return minute1 > minute2 ? 1 : -1;
						} else {
							int sec1 = Integer.valueOf(o1.split(":")[2]);
							int sec2 = Integer.valueOf(o2.split(":")[2]);
							return sec1 > sec2 ? 1 : -1;
						}
					}
				});

		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, NginxAccessLogFields.timestamp);
		params.put(EsAggregationParams.date_histogram.inteval,
				EsAggregationParams.date_histogram.second);

		RangeQueryBuilder rangeQueryBuilder = es.dealDate(fromDate, toDate,
				NginxAccessLogFields.timestamp);
		SearchResponse response = es
				.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log)
				.setQuery(rangeQueryBuilder)
				.addAggregation(
						es.getAggregationBuilder(
								EsAggregationType.date_histogram, params))
				.execute().actionGet();
		Histogram histogram = response.getAggregations().get("result");
		for (Histogram.Bucket entry : histogram.getBuckets()) {
			String keyAsString = entry.getKeyAsString();
			keyAsString = DateUtil.millToDateString(keyAsString).substring(11,
					19);
			String value = String.valueOf(entry.getDocCount());
			resultMap.put(keyAsString, value);
		}
		return resultMap;
	}

	// 统计Nginx响应状态码分布图
	@Override
	public Map<String, String> getStatusStatistics(String ip) {
		Map<String, String> resultMap = new HashMap<String, String>();

		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, NginxAccessLogFields.status);
		params.put(EsAggregationParams.size, "10");

		SearchResponse response = es
				.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log)
				.setQuery(
						es.getQueryBuilder(ip,
								NginxAccessLogFields.upstream_addr))
				.addAggregation(
						es.getAggregationBuilder(EsAggregationType.term, params))
				.execute().actionGet();

		Aggregations aggs = response.getAggregations();
		Terms terms = aggs.get("result");
		List<Bucket> bucket = terms.getBuckets();
		for (Bucket bu : bucket) {
			resultMap
					.put(bu.getKeyAsString(), String.valueOf(bu.getDocCount()));
		}
		return resultMap;
	}

	// 错误页面统计
	@Override
	public Map<String, String> getWrongPageCount(String fromDate,
			String toDate, String ip) {
		QueryBuilder queryBuilder = QueryBuilders.rangeQuery(
				NginxAccessLogFields.status).from("201");
		TermQueryBuilder termQuery = QueryBuilders.termQuery(
				NginxAccessLogFields.upstream_addr, ip);
		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationType.term, NginxAccessLogFields.request);
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(
				EsAggregationType.term, params);
		Map<String, String> distribution = new HashMap<String, String>();
		SearchResponse response = es
				.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log)
				.setPostFilter(
						es.dealDate(fromDate, toDate,
								NginxAccessLogFields.timestamp))
				.setQuery(
						QueryBuilders.boolQuery().must(queryBuilder)
								.must(termQuery))
				.addAggregation(aggregationBuilder).execute().actionGet();
		Aggregations aggs = response.getAggregations();
		Terms terms = aggs.get("result");
		List<Bucket> bucket = terms.getBuckets();
		for (Bucket bu : bucket) {
			distribution.put(bu.getKeyAsString(),
					String.valueOf(bu.getDocCount()));
		}

		return distribution;

	}

	@Override
	public Map<String, String> getVisitFrequently(String fromDate,
			String toDate, String ip) {

		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationType.term, NginxAccessLogFields.request);
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(
				EsAggregationType.term, params);

		Map<String, String> distribution = new HashMap<String, String>();
		SearchResponse response = es
				.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log)
				.setQuery(
						QueryBuilders
								.boolQuery()
								.must(QueryBuilders.termQuery(
										NginxAccessLogFields.upstream_addr, ip))
								.must(es.dealDate(fromDate, toDate,
										NginxAccessLogFields.timestamp)))
				.addAggregation(aggregationBuilder).execute().actionGet();
		Aggregations aggs = response.getAggregations();
		Terms terms = aggs.get("result");
		List<Bucket> bucket = terms.getBuckets();
		for (Bucket bu : bucket) {
			distribution.put(bu.getKeyAsString(),
					String.valueOf(bu.getDocCount()));
		}

		return distribution;

	}

	// 按时段聚合数据
	@Override
	public Map<String, String> getDateHisgram(String fromDate, String toDate,
			String ip, String drillingTime, String lastDrillingTime,
			String analysisType) {

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
				fromDate = DateUtil.getMinuteFirstSecong(year, month, day,
						hour, minute);
				toDate = DateUtil.getMinuteLastSecong(year, month, day, hour,
						minute);
			}
		}
		Map<String, String> middleMap = new HashMap<String, String>();

		// 时间聚合维度
		String interalParam = null;
		interalParam = InteralUtil.getInteral(fromDate, toDate);

		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, NginxAccessLogFields.timestamp);
		params.put(EsAggregationParams.date_histogram.inteval, interalParam);

		Map<String, String> avgParams = new HashMap<String, String>();
		avgParams.put(EsAggregationParams.term,
				NginxAccessLogFields.upstreap_response_time);

		// 查询builder
		QueryBuilder finalQueryBuilder = null;

		// 聚合builder
		AggregationBuilder finalAggregationBuilder = null;

		switch (analysisType) {
		// 错误页面
		case "errorPage":
			finalQueryBuilder = QueryBuilders
					.boolQuery()
					.must(es.dealDate(fromDate, toDate,
							NginxAccessLogFields.timestamp))
					.must(QueryBuilders.termQuery(
							NginxAccessLogFields.upstream_addr, ip))
					.must(QueryBuilders.rangeQuery(NginxAccessLogFields.status)
							.from("201"));
			finalAggregationBuilder = es.getAggregationBuilder(
					EsAggregationType.date_histogram, params);
			break;
		// 响应时间
		case "responseTime":
			finalQueryBuilder = QueryBuilders
					.boolQuery()
					.must(es.dealDate(fromDate, toDate,
							NginxAccessLogFields.timestamp))
					.must(QueryBuilders.termQuery(
							NginxAccessLogFields.upstream_addr, ip));
			finalAggregationBuilder = es.getAggregationBuilder(
					EsAggregationType.date_histogram, params).subAggregation(
					AggregationBuilders.avg("avg").script(
							new Script("nginxResponseTime", ScriptType.FILE,
									null, null)));

			break;
		// 访问次数
		case "accessTimes":
			finalQueryBuilder = QueryBuilders
					.boolQuery()
					.must(es.dealDate(fromDate, toDate,
							NginxAccessLogFields.timestamp))
					.must(QueryBuilders.termQuery(
							NginxAccessLogFields.upstream_addr, ip));
			finalAggregationBuilder = es.getAggregationBuilder(
					EsAggregationType.date_histogram, params);
			break;
		default:
			break;
		}

		SearchResponse response = es.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log)
				.setQuery(finalQueryBuilder)
				.addAggregation(finalAggregationBuilder).execute().actionGet();
		Map<String, String> resultMap = new HashMap<String, String>();
		if (analysisType.equals("responseTime")) {
			Histogram histogram = response.getAggregations().get("result");

			for (Histogram.Bucket entry : histogram.getBuckets()) {
				String keyAsString = "";
				try {
					keyAsString = InteralUtil.getInteralDateString(
							entry.getKeyAsString(), interalParam);
					// 12时查询会查询出其他时段的数据，暂时过滤掉
					if (interalParam
							.equals(EsAggregationParams.date_histogram.minute)) {
						String hour = drillingTime.split("[\u4e00-\u9fa5]")[1];
						if (!keyAsString.startsWith(hour)) {
							continue;
						}
					}
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
			resultMap = InteralUtil.interalMap(middleMap, interalParam,
					fromDate, toDate);
		} else {
			Histogram histogram = response.getAggregations().get("result");

			for (Histogram.Bucket entry : histogram.getBuckets()) {
				String keyAsString = "";
				try {
					keyAsString = InteralUtil.getInteralDateString(
							entry.getKeyAsString(), interalParam);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String value = String.valueOf(entry.getDocCount());
				if ("NaN".equals(value)) {
					value = "0";
				}
				middleMap.put(keyAsString, value);
			}
			resultMap = InteralUtil.interalMap(middleMap, interalParam,
					fromDate, toDate);
		}
		return resultMap;
	}

	@Override
	public Map<String, String> getDealTime(String fromDate, String toDate,
			String ip) {
		Map<String, String> resultMap = new HashMap<String, String>();

		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, NginxAccessLogFields.request);
		params.put(EsAggregationParams.size, "10");

		Map<String, String> avgParams = new HashMap<String, String>();
		avgParams.put(EsAggregationParams.term,
				NginxAccessLogFields.upstreap_response_time);

		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(
				NginxAccessLogFields.upstream_addr, ip);

		SearchResponse response = es
				.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log)
				.setQuery(
						QueryBuilders
								.boolQuery()
								.must(termQueryBuilder)
								.must(es.dealDate(fromDate, toDate,
										NginxAccessLogFields.timestamp)))
				.addAggregation(
						es.getAggregationBuilder(EsAggregationType.term, params)
								.subAggregation(AggregationBuilders.avg("avg").script(
										new Script("nginxResponseTime", ScriptType.FILE,
												null, null)))).execute()
				.actionGet();
		Terms terms = response.getAggregations().get("result");

		for (Terms.Bucket entry : terms.getBuckets()) {
			String key = (String) entry.getKey(); // Term
			Avg avg = entry.getAggregations().get("avg");
			String value = String.valueOf(avg.getValue());
			resultMap.put(key, value);
		}

		return resultMap;
	}

	// 详细信息钻取
	@Override
	public List<NginxAccessLogEntity> detailInfo(String fromDate,
			String toDate, String detailType, String searchWord, String ip) {
		QueryBuilder queryBuilder = null;

		switch (detailType) {
		// 404页面
		case "error":
			queryBuilder = QueryBuilders
					.rangeQuery(NginxAccessLogFields.status).from("201");
			break;
		// 访问频次
		case "accessTimes":
			queryBuilder = null;
			break;
		// 响应时间
		case "VisitTime":
			queryBuilder = null;
			break;
		// 处理时间
		case "DealTime":
			queryBuilder = QueryBuilders.termQuery(
					NginxAccessLogFields.request, searchWord);
			break;
		default:
			break;
		}
		List<Map<String, Object>> detailInfo = es.detailInfo(
				NginxIndexNames.nginx_access_log, NginxTypes.nginx_access_log,
				NginxAccessLogFields.timestamp, fromDate, toDate, queryBuilder,
				ip);

		List<NginxAccessLogEntity> resultList = new ArrayList<NginxAccessLogEntity>(
				200);

		for (Map<String, Object> map : detailInfo) {
			NginxAccessLogEntity entity = (NginxAccessLogEntity) ResultMapUtil
					.mapToObject(map, NginxAccessLogEntity.class);
			resultList.add(entity);
		}
		return resultList;
	}

	// 各错误码统计分析
	@Override
	public Map<String, String> statusAnalysis(String fromDate, String toDate,
			String ip, String url) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> aggregationParams = new HashMap<String, String>();
		aggregationParams.put(EsAggregationParams.term,
				NginxAccessLogFields.status);
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(
				EsAggregationParams.term, aggregationParams);
		BoolQueryBuilder must = QueryBuilders
				.boolQuery()
				.must(es.getQueryBuilder(url, NginxAccessLogFields.request))
				.must(es.getQueryBuilder(ip, NginxAccessLogFields.upstream_addr))
				.must(QueryBuilders.rangeQuery(NginxAccessLogFields.status)
						.from("201"));
		SearchResponse response = es.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log).setQuery(must)
				.addAggregation(aggregationBuilder).execute().actionGet();

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
		ElasticSearchHandler es = new ElasticSearchHandler("elasticsearch",
				"10.4.247.251", 9300);

		String fromDate = "2016-05-07 00:00:00";
		String toDate = "2016-07-17 23:59:00";
		String analysisType = "AccessTimes";

		Map<String, String> params = new HashMap<String, String>();
		params.put(EsAggregationParams.term, NginxAccessLogFields.host);
		// 按host进行聚合
		AggregationBuilder aggregationBuilder = es.getAggregationBuilder(
				EsAggregationType.term, params);

		// 统计维度聚合条件
		AggregationBuilder analysisAggregationBuilder = null;

		Map<String, String> analysisParams = new HashMap<String, String>();

		RangeQueryBuilder dealDateQueryBuilder = es.dealDate(fromDate, toDate,
				NginxAccessLogFields.timestamp);

		// 查询builder
		QueryBuilder finalQueryBuilder = null;

		// 聚合builder
		AggregationBuilder finalAggregationBuilder = null;
		switch (analysisType) {
		// 错误页面
		case "errorPage":
			QueryBuilder filter = QueryBuilders.rangeQuery(
					NginxAccessLogFields.status).from("201");
			finalQueryBuilder = QueryBuilders.boolQuery()
					.must(dealDateQueryBuilder).must(filter);
			finalAggregationBuilder = aggregationBuilder;
			break;
		// 响应时间
		case "responseTime":
			analysisParams.put(EsAggregationParams.term,
					NginxAccessLogFields.upstreap_response_time);
			MetricsAggregationBuilder metricsAggregationBuilder = es
					.getMetricsAggregationBuilder(EsAggregationType.avg,
							analysisParams);
			finalQueryBuilder = dealDateQueryBuilder;
			finalAggregationBuilder = aggregationBuilder
					.subAggregation(AggregationBuilders.avg("avg").script(
							new Script("nginxResponseTime", ScriptType.FILE,
									null, null)));
			break;
		// 访问次数
		case "AccessTimes":
			finalQueryBuilder = dealDateQueryBuilder;
			finalAggregationBuilder = aggregationBuilder;
			break;
		default:
			break;
		}

		Map<String, String> distribution = new HashMap<String, String>();

		SearchResponse response = es.getClient()
				.prepareSearch(NginxIndexNames.nginx_access_log)
				.setTypes(NginxTypes.nginx_access_log)
				.setQuery(finalQueryBuilder)
				.addAggregation(finalAggregationBuilder).execute().actionGet();

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
				distribution.put(bu.getKeyAsString(),
						String.valueOf(bu.getDocCount()));
			}
		}
		System.out.println(distribution);
	}

}

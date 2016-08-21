package cn.sh.ideal.elasticsearch.handler;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.Term;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgBuilder;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sh.ideal.elasticsearch.constant.aggregation.EsAggregationParams;
import cn.sh.ideal.elasticsearch.constant.aggregation.EsAggregationType;
import cn.sh.ideal.elasticsearch.entity.ResultEntity;
import cn.sh.ideal.web.nginx.constants.NginxAccessLogFields;
import cn.sh.ideal.web.tomcat.constants.TomcatAccessLogFields;
import cn.sh.ideal.web.tomcat.constants.TomcatIndexNames;
import cn.sh.ideal.web.tomcat.constants.TomcatTypes;
import cn.sh.ideal.web.tomcat.entity.TomcatAccessLogEntity;
import cn.sh.ideal.web.tomcat.utils.ResultMapUtil;

import com.alibaba.fastjson.JSONObject;

public class ElasticSearchHandler {
	private Logger logger = LoggerFactory.getLogger(ElasticSearchHandler.class);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private SimpleDateFormat simpleDateFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

	private Client client;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ElasticSearchHandler(String clusterName, String serverIp, int serverport) throws UnknownHostException {
		// 装载配置文件
		Builder builder = Settings.settingsBuilder();
		builder.put("cluster.name", clusterName);
		builder.put("client.transport.ignore_cluster_name", false);
		builder.put("client.transport.ping_timeout", "5s");
		builder.put("client.transport.nodes_sampler_interval", "5s");
		builder.put("client.transport.sniff", true);

		Settings settings = builder.build();

		client = TransportClient.builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIp), serverport));
	}

	/**
	 * 将map封装为一个json
	 * @param dataMap
	 * @return
	 */
	public String getJson(HashMap<String, String> dataMap) {
		JSONObject node = new JSONObject();

		Set<String> keySet = dataMap.keySet();

		for (String string : keySet) {
			node.put(string, dataMap.get(string));
		}

		return node.toJSONString();

	}

	/**
	 * 批量导入
	 * @param indexName
	 * @param type
	 * @param dataList
	 * @return
	 */
	public void bulkInsert(String indexName, String type, List<HashMap<String, String>> dataList) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		for (HashMap<String, String> hashMap : dataList) {
			bulkRequest.add(client.prepareIndex(indexName, type).setSource(getJson(hashMap)));
		}

		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {

		}
	}

	/**
	 * 根据开始结束时间获取查询条件对象
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public RangeQueryBuilder dealDate(String fromDate, String toDate, String sortFieldName) {
		long start = 0l;

		long end = 0l;

		if(!fromDate.endsWith(".000")){
		try {
			start = sdf.parse(fromDate).getTime();
			end = sdf.parse(toDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
		else{
			try {
				start = simpleDateFormate.parse(fromDate).getTime();
				end = simpleDateFormate.parse(toDate).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(sortFieldName).from(start).to(end)
				.format("epoch_millis");

		return rangeQueryBuilder;

	}
	
	public static void main(String[] args) throws UnknownHostException {
		ElasticSearchHandler es = new ElasticSearchHandler("elasticsearch", "10.4.247.251", 9300);
//		QueryBuilder qq = QueryBuilders.termQuery(TomcatAccessLogFields.http_status, "404");
////		List<Map<String, Object>> detailInfo = es.detailInfo(TomcatIndexNames.tomcat_access_log, TomcatTypes.tomcat_access_log, TomcatAccessLogFields.visit_time, "2016-07-07 00:00:00", "2016-07-17 23:59:00", null);
//	    for (Map<String, Object> map : detailInfo) {
//			TomcatAccessLogEntity mapToObject = (TomcatAccessLogEntity) ResultMapUtil.mapToObject(map, TomcatAccessLogEntity.class);
//		    System.out.println(mapToObject);
//	    }
	    
	}

	public List<Map<String,Object>> detailInfo(String indexName, String type, String sortFieldName,
			String fromDate, String toDate, QueryBuilder queryBuilder,String ip) {
		TermQueryBuilder termQuery = null;
		if(TomcatIndexNames.tomcat_access_log.equals(indexName)){
			termQuery = QueryBuilders.termQuery(TomcatAccessLogFields.localhost_ip, ip);
		}else{
			termQuery = QueryBuilders.termQuery(NginxAccessLogFields.upstream_addr, ip);
		}
		
		RangeQueryBuilder rangeQueryBuilder = dealDate(fromDate, toDate, sortFieldName);
		
		QueryBuilder finalQueryBuilder = null;
		
		if(queryBuilder!=null){
			finalQueryBuilder = QueryBuilders.boolQuery().must(rangeQueryBuilder).must(queryBuilder).must(termQuery);
		}else{
			finalQueryBuilder = QueryBuilders.boolQuery().must(rangeQueryBuilder).must(termQuery);
		}
		SearchResponse response = client.prepareSearch(indexName).setTypes(type).addSort(sortFieldName, SortOrder.DESC)
				.setScroll(new TimeValue(33)).setQuery(finalQueryBuilder).setSize(2000)
				.execute().actionGet();
		
		List resultList = new ArrayList<Map>();

		SearchHit[] hits = response.getHits().hits();

		for (SearchHit searchHit : hits) {
			Map<String, Object> source = searchHit.getSource();

			resultList.add(source);
		}
		return resultList;
	}

	/**
	 * es查询api
	 * @param indexName
	 * @param type
	 * @param searchWord 检索词
	 * @param fieldNames 检索字段
	 * @param sortFieldName 排序字段
	 * @param fromDate 起始时间
	 * @param toDate 结束时间
	 * @return
	 */
	public ResultEntity scroll(String indexName, String type, String searchWord, String[] fieldNames, String sortFieldName,
			String fromDate, String toDate, int size) {
		RangeQueryBuilder rangeQueryBuilder = dealDate(fromDate, toDate, sortFieldName);

		SearchResponse response = client.prepareSearch(indexName).addSort(sortFieldName, SortOrder.DESC)
				.setScroll(new TimeValue(90000)).setPostFilter(rangeQueryBuilder)
				.setQuery(QueryBuilders.multiMatchQuery(searchWord, fieldNames)).setSize(100).execute().actionGet();

		return getResultFromSearchResponse(response);
	}

	public ResultEntity getResultFromSearchResponse(SearchResponse response) {
		List resultList = new ArrayList<Map>();

		String scrollId = response.getScrollId();

		SearchHit[] hits = response.getHits().hits();

		for (SearchHit searchHit : hits) {
			Map<String, Object> source = searchHit.getSource();

			resultList.add(source);
		}

		return new ResultEntity(scrollId, resultList, response.getHits().getTotalHits());
	}

	public ResultEntity scrollSecond(String scrollId) {
		SearchResponse response = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(90000)).execute().actionGet();
		return getResultFromSearchResponse(response);
	}

	/**
	 * 深分页检索
	 * @param indexName
	 * @param type
	 * @param searchWord
	 * @param fieldNames
	 * @param from
	 * @param size
	 */
	public String search(String indexName, String type, String searchWord, String[] fieldNames, int from, int size) {
		SearchResponse response = client.prepareSearch(indexName).setTypes(type).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		//				.setQuery(QueryBuilders.multiMatchQuery(searchWord, fieldNames))
				.setExplain(true).setFrom(from).setSize(size).execute().actionGet();

		SearchHits hits = response.getHits();
		System.out.println("num is = " + hits.getTotalHits());

		for (SearchHit searchHit : hits) {

		}

		return response.getHits().toString();
	}

	/**
	 * 查询某个索引下数据数量
	 * @param indexName
	 * @return
	 */
	public long getCount(String indexName) {
		CountResponse response = client.prepareCount(indexName).setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();

		return response.getCount();
	}

	/**
	 * 某个term查询满足条件的数据的数据量
	 * @param indexName
	 * @param fieldName
	 * @param term
	 * @return
	 */
	public long getQueryCount(String indexName, String fieldName, String term) {
		CountResponse response = client.prepareCount(indexName).setQuery(QueryBuilders.termQuery(fieldName, term)).execute()
				.actionGet();
		return response.getCount();
	}

	//	public Script getScript(){
	////		return new Script(script, type, lang, params);
	//		
	//	}

	public MetricsAggregationBuilder getMetricsAggregationBuilder(String type, Map<String, String> params) {
		MetricsAggregationBuilder builder = null;
		switch (type) {
		case EsAggregationType.avg:
			String avgTerm = params.get(EsAggregationParams.term);
			MetricsAggregationBuilder metricsAggregationBuilder = AggregationBuilders.avg("avg").field(avgTerm);
			builder = metricsAggregationBuilder;
			break;
		default:
			break;
		}
		return builder;

	}

	public AggregationBuilder getAggregationBuilder(String type, Map<String, String> params) {
		AggregationBuilder build = null;
		String rangeResult = "result";
		String order = params.get(EsAggregationParams.order);
		switch (type) {
		case "term":
			//聚合字段
			String term = params.get(EsAggregationParams.term);
			//聚合展示前多少名
//			Integer size = Integer.valueOf(params.get(EsAggregationParams.size));
			build = AggregationBuilders.terms(rangeResult).field(term).size(15).order(Terms.Order.count(false));
			break;
		case "dateRange":
			build = AggregationBuilders.dateRange(rangeResult).field(params.get(EsAggregationParams.term)).format(params.get("format"))
					.addUnboundedTo("1950") // from -infinity to 1950 (excluded)
					.addRange("1950", "1960") // from 1950 to 1960 (excluded)
					.addUnboundedFrom("1960"); // from 1960 to +infinity
			break;
		case "date_histogram":
			//聚合字段
			String dataTerm = params.get("term");
			//时间间隔
			String inteval = params.get("inteval");

			DateHistogramInterval dateInteval = null;
			if ("second".equals(inteval)) {
				dateInteval = DateHistogramInterval.SECOND;
			} else if ("minute".equals(inteval)) {
				dateInteval = DateHistogramInterval.MINUTE;
			} else if ("hour".equals(inteval)) {
				dateInteval = DateHistogramInterval.HOUR;
			} else if ("day".equals(inteval)) {
				dateInteval = DateHistogramInterval.DAY;
			} else if ("month".equals(inteval)) {
				dateInteval = DateHistogramInterval.MONTH;
			} else if ("sq".equals(inteval)) {
				dateInteval = DateHistogramInterval.QUARTER;
			} else if ("year".equals(inteval)) {
				dateInteval = DateHistogramInterval.YEAR;
			}
			DateHistogramBuilder dateHistogramBuilder = AggregationBuilders.dateHistogram(rangeResult).field(dataTerm)
					.interval(dateInteval).format("epoch_millis").offset("-8h");

			build = dateHistogramBuilder;
			break;

		default:
			break;
		}
		return build;
	}

	/**
	 * 饼图分析--一层维度
	 * @param term 需要分析的字段名称
	 * @param indexs
	 * @param types
	 * @param aggregationSize分析前多少名
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAggregation(String indexs, String types, String fromDate, String toDate, String dateField,
			QueryBuilder queryBuilder, AggregationBuilder aggregationBuilder) {
		Map<String, String> distribution = new HashMap<String, String>();
		SearchResponse response = client.prepareSearch(indexs).setTypes(types)
				.setPostFilter(dealDate(fromDate, toDate, dateField)).setQuery(queryBuilder).addAggregation(aggregationBuilder)
				.execute().actionGet();
		Aggregations aggs = response.getAggregations();
		Terms terms = aggs.get("result");
		List<Bucket> bucket = terms.getBuckets();
		for (Bucket bu : bucket) {
			distribution.put(bu.getKeyAsString(), String.valueOf(bu.getDocCount()));
		}

		return distribution;
	}

	/**
	 * 求最大值
	 * @param term
	 * @param client
	 * @param index
	 * @param type
	 * @return
	 */
	public double getMaxValue(String term, Client client, String index, String type) {
		double value = 0;
		@SuppressWarnings("rawtypes")
		MetricsAggregationBuilder aggregation = AggregationBuilders.max("max").field(term);

		SearchResponse response = client.prepareSearch(index).addAggregation(aggregation).execute().actionGet();
		Max agg = response.getAggregations().get("max");
		value = agg.getValue();
		return value;
	}

	/**
	 * 求最小值
	 * @param term
	 * @param client
	 * @param index
	 * @param type
	 * @return
	 */
	public double getMinValue(String term, Client client, String index, String type) {
		double value = 0;
		@SuppressWarnings("rawtypes")
		MetricsAggregationBuilder aggregation = AggregationBuilders.min("max").field(term);

		SearchResponse response = client.prepareSearch(index).addAggregation(aggregation).execute().actionGet();
		Min agg = response.getAggregations().get("max");
		value = agg.getValue();
		return value;
	}

	/**
	 * 先根据一字段聚合然后根据script计算平均值
	 * @param indexName
	 * @param type
	 * @return
	 */
	public double getAvgByScript(String indexName, String type, String scriptFileName, String fromDate, String toDate,
			String term, String startTime, String endTime) {
		Map<String, String> paramsMap = new HashMap<String, String>();

		paramsMap.put("startTime", startTime);

		paramsMap.put("endTime", endTime);

		SearchResponse response = client.prepareSearch(indexName)
				.setTypes(type)
				//				.setPostFilter(dealDate(fromDate, toDate,"Createtime_first"))
				.addAggregation(
						AggregationBuilders
								.terms("result")
								.field(term)
								.size(15)
								.subAggregation(
										AggregationBuilders.avg("avg").script(
												new Script(scriptFileName, ScriptType.FILE, null, paramsMap))))

				.execute().actionGet();

		Aggregations aggs = response.getAggregations();
		Terms terms = aggs.get("result");
		List<Bucket> bucket = terms.getBuckets();
		for (Bucket bu : bucket) {
			Aggregations aggregations = bu.getAggregations();
			Avg max = aggregations.get("avg");
			double value = max.getValue();
		}

		//		@SuppressWarnings("rawtypes")
		//		MetricsAggregationBuilder aggregation =
		//	        	AggregationBuilders
		//	                	.avg("agg").script(new Script(scriptFileName, ScriptType.FILE, null, null));
		return 0;

	}

	/**
	 * 求平均值
	 * @param term
	 * @param client
	 * @param index
	 * @param type
	 * @param valueDot
	 * @return
	 */
	public double getAvgValue(String term, String index, String type, int valueDot) {
		double value = 0;
		@SuppressWarnings("rawtypes")
		MetricsAggregationBuilder aggregation = AggregationBuilders.avg("max").field(term);

		SearchResponse response = client.prepareSearch(index).addAggregation(aggregation).execute().actionGet();
		Avg agg = response.getAggregations().get("max");
		value = agg.getValue();
		BigDecimal b = new BigDecimal(value);
		value = b.setScale(valueDot, BigDecimal.ROUND_HALF_UP).doubleValue();
		return value;
	}

	public QueryBuilder getQueryBuilder(String searchWord, String fieldName) {
		TermQueryBuilder termQuery = QueryBuilders.termQuery(fieldName, searchWord);
		return termQuery;
	}

}

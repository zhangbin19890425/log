package cn.sh.ideal.elasticsearch.constant.aggregation;

public interface EsAggregationParams {
	//聚合字段
	String term = "term";
	//聚合前多少名
	String size = "size";
	//聚合排序字段
	String order = "order";

	//term聚合
	interface Term {

	}

	interface date_histogram {
		//时间间隔
		String inteval = "inteval";

		String second = "second";
		String minute = "minute";
		String hour = "hour";
		String day = "day";
		String month = "month";
		String quarter = "sq";
		String year = "year";

	}
}

package cn.sh.ideal.elasticsearch.entity;

import java.util.List;

public class ResultEntity {
	private String scrollId;
	
	private List resultList;
	
	private long total;
	
	public ResultEntity(String scrollId,List resultList,long total){
		this.scrollId = scrollId;
		this.resultList = resultList;
		this.total = total;
	}

	public String getScrollId() {
		return scrollId;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public void setScrollId(String scrollId) {
		this.scrollId = scrollId;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
	
	
}

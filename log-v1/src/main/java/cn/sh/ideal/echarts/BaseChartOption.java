package cn.sh.ideal.echarts;

import java.util.List;
import java.util.Map;

public abstract class BaseChartOption {
	protected String title;
	protected String legend;
	protected Map<String, String> mapResult;
	protected String chartColor;
	protected List<Object> chartColors;
	protected String backgroundColor;
	protected String xAxisName = "";
	protected String yAxisName = "";
	protected Boolean hasRotate = true;
	protected String dataType = "Long";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public Map<String, String> getMapResult() {
		return mapResult;
	}

	public void setMapResult(Map<String, String> mapResult) {
		this.mapResult = mapResult;
	}

	public String getChartColor() {
		return chartColor;
	}

	public void setChartColor(String chartColor) {
		this.chartColor = chartColor;
	}

	public List<Object> getChartColors() {
		return chartColors;
	}

	public void setChartColors(List<Object> chartColors) {
		this.chartColors = chartColors;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getxAxisName() {
		return xAxisName;
	}

	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getyAxisName() {
		return yAxisName;
	}

	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	public Boolean getHasRotate() {
		return hasRotate;
	}

	public void setHasRotate(Boolean hasRotate) {
		this.hasRotate = hasRotate;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}

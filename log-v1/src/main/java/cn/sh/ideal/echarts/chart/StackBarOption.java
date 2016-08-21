package cn.sh.ideal.echarts.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.MarkType;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.PointData;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.Series;

import cn.sh.ideal.echarts.BaseChartOption;
import cn.sh.ideal.echarts.OptionGenerator;

public class StackBarOption extends BaseChartOption implements OptionGenerator {
	private Boolean yAxisBar = false;
	private List<String> legendList;
	private List<Object> axisLabelList = new ArrayList<Object>();
	private HashMap<Integer, List<Object>> bardatas = new HashMap<Integer, List<Object>>();
	private String stack;

	public Boolean getyAxisBar() {
		return yAxisBar;
	}

	public void setyAxisBar(Boolean yAxisBar) {
		this.yAxisBar = yAxisBar;
	}

	public List<String> getLegendList() {
		return legendList;
	}

	public void setLegendList(List<String> legendList) {
		this.legendList = legendList;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	@Override
	public Option getOption() throws Exception {
		Option option = new Option();
		for (Entry<String, String> entry : this.getMapResult().entrySet()) {
			axisLabelList.add(entry.getKey());
			String[] vals = entry.getValue().split("\\|");
			for (int i = 0; i < vals.length; i++) {
				List<Object> datalist = bardatas.get(i);
				if (datalist == null) {
					datalist = new ArrayList<Object>();
					if ("Double".equals("Double")) {
						datalist.add(Double.parseDouble(vals[i]));
					} else {
						datalist.add(Long.parseLong(vals[i]));
					}
					bardatas.put(i, datalist);
				} else {
					if ("Double".equals("Double")) {
						datalist.add(Double.parseDouble(vals[i]));
					} else {
						datalist.add(Long.parseLong(vals[i]));
					}
				}
			}
		}

		// x坐标、类型、格式
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(axisLabelList);
		if (this.getHasRotate()) {
			categoryAxis.axisLabel().interval(0).rotate(45).margin(2);
		}

		// y坐标、类型、格式
		ValueAxis valueAxis = new ValueAxis();

		option.title().text(this.getTitle()).x(X.center).textStyle().fontSize(12);
		option.legend().orient(Orient.vertical).left(X.right).padding(0, 10).setData(this.getLegendList());
		option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);

		if (this.getyAxisBar()) {
			valueAxis.name(this.getxAxisName());
			option.xAxis(valueAxis);
			categoryAxis.name(this.getyAxisName());
			option.yAxis(categoryAxis);
			option.grid().left(3).bottom(3).containLabel(true);
		} else {
			valueAxis.name(this.getyAxisName());
			categoryAxis.name(this.getxAxisName());
			option.xAxis(categoryAxis);
			option.yAxis(valueAxis);
		}

		option.setColor(this.getChartColors());// 柱状图颜色
		option.backgroundColor(this.getBackgroundColor()); // 背景色

		@SuppressWarnings("rawtypes")
		List<Series> seriesLists = new ArrayList<Series>();
		for (int i = 0; i < bardatas.size(); i++) {
			Bar bar = new Bar(this.getLegendList().get(i));
			if (!this.getyAxisBar()) {
				bar.markLine(new MarkLine().data(new PointData().type(MarkType.average).name("平均值")));
			}
			bar.setData(bardatas.get(i));
			bar.setStack(this.getStack());
			seriesLists.add(bar);
		}
		option.series(seriesLists);

		return option;
	}

}

package cn.sh.ideal.echarts.chart;

import java.util.ArrayList;
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

import cn.sh.ideal.echarts.BaseChartOption;
import cn.sh.ideal.echarts.OptionGenerator;

public class BarOption extends BaseChartOption implements OptionGenerator {
	private Boolean yAxisBar = false;

	private List<Object> axisLabelList = new ArrayList<Object>();
	private List<Object> bardatas = new ArrayList<Object>();
	private List<String> legendList = new ArrayList<String>();

	public Boolean getyAxisBar() {
		return yAxisBar;
	}

	public void setyAxisBar(Boolean yAxisBar) {
		this.yAxisBar = yAxisBar;
	}

	@Override
	public Option getOption() throws Exception {
		Option option = new Option();

		for (Entry<String, String> entry : this.getMapResult().entrySet()) {
			axisLabelList.add(entry.getKey());
			if ("Double".equals(this.getDataType())) {
				bardatas.add(Double.parseDouble(entry.getValue()));
			} else {
				bardatas.add(Long.parseLong(entry.getValue()));
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
		legendList.add(this.getLegend());
		option.legend().orient(Orient.vertical).left(X.right).padding(0, 10).setData(legendList);
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

		option.color(this.getChartColor()); // 柱状图颜色
		option.backgroundColor(this.getBackgroundColor()); // 背景色

		Bar bar = new Bar(this.getLegend());
		if (!this.getyAxisBar()) {
			bar.markLine(new MarkLine().data(new PointData().type(MarkType.average).name("平均值")));
		}
		bar.setData(bardatas);
		option.series(bar);

		return option;
	}

}

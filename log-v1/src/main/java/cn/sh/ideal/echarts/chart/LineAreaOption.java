package cn.sh.ideal.echarts.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.series.Line;

import cn.sh.ideal.echarts.BaseChartOption;
import cn.sh.ideal.echarts.OptionGenerator;

public class LineAreaOption extends BaseChartOption implements OptionGenerator {
	private List<Object> axisLabelList = new ArrayList<Object>();
	private List<Object> linedatas = new ArrayList<Object>();
	private List<String> legendList = new ArrayList<String>();

	@Override
	public Option getOption() throws Exception {
		Option option = new Option();

		for (Entry<String, String> entry : this.getMapResult().entrySet()) {
			axisLabelList.add(entry.getKey());
			if ("Double".equals(this.getDataType())) {
				linedatas.add(Double.parseDouble(entry.getValue()));
			} else {
				linedatas.add(Long.parseLong(entry.getValue()));
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
		valueAxis.name(this.getyAxisName());

		option.title().text(this.getTitle()).x(X.center).textStyle().fontSize(12);
		legendList.add(this.getLegend());
		option.legend().orient(Orient.vertical).left(X.right).padding(0, 10).setData(legendList);
		option.tooltip().trigger(Trigger.axis);

		option.color(this.getChartColor()); // chart颜色
		option.backgroundColor(this.getBackgroundColor()); // 背景色

		categoryAxis.name(this.getxAxisName());
		option.xAxis(categoryAxis); // 设置x轴的值和类型
		option.yAxis(valueAxis); // 设置y轴的类型

		Line line = new Line(this.getLegend());
		line.symbol("emptyCircle");
		line.itemStyle().normal().areaStyle().type("default");
		line.symbolSize(8);
		line.setData(linedatas);
		option.series(line);

		return option;

	}

}

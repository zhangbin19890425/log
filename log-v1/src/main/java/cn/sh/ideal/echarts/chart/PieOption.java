package cn.sh.ideal.echarts.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Emphasis;

import cn.sh.ideal.echarts.BaseChartOption;
import cn.sh.ideal.echarts.OptionGenerator;

public class PieOption extends BaseChartOption implements OptionGenerator {
	private List<Object> legendList = new ArrayList<Object>();
	private String pieName = "";
	private Object radius;
	private Object width;
	private Object height;

	public String getPieName() {
		return pieName;
	}

	public void setPieName(String pieName) {
		this.pieName = pieName;
	}

	public Object getWidth() {
		return width;
	}

	public void setWidth(Object width) {
		this.width = width;
	}

	public Object getHeight() {
		return height;
	}

	public void setHeight(Object height) {
		this.height = height;
	}

	public Object getRadius() {
		return radius;
	}

	public void setRadius(Object radius) {
		this.radius = radius;
	}

	@Override
	public Option getOption() throws Exception {
		Option option = new Option();

		for (String str : this.getMapResult().keySet()) {
			legendList.add(str);
		}
		option.title().text(this.getTitle()).x(X.center).top(10).textStyle().fontSize(12);
		option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
		option.legend().orient(Orient.vertical).left(X.left).padding(10, 0, 0, 10).setData(legendList);

		option.series(getPie(this.getMapResult()).center(this.getWidth(), this.getHeight()).radius(this.getRadius())
				.itemStyle(new ItemStyle().emphasis(new Emphasis().shadowBlur(10).shadowOffsetX(0).shadowColor("rgba(0, 0, 0, 0.5)"))));

		option.backgroundColor(this.getBackgroundColor());
		return option;
	}

	private Pie getPie(Map<String, String> map) {
		Pie pie = new Pie();
		List<PieData> list = new ArrayList<PieData>();
		for (Entry<String, String> entry : map.entrySet()) {
			PieData data = new PieData(entry.getKey(), entry.getValue());
			list.add(data);
		}

		pie.setName(this.getPieName());
		pie.setData(list);
		return pie;
	}

}

package cn.sh.ideal.echarts.series;

import com.github.abel533.echarts.series.MarkLine;

/**
 * 
 * @author wk
 * 
 *         MarkLineX extends MarkLine
 * 
 *         适配echarts-3.2.2中MarkLine的silent属性
 */
public class MarkLineX extends MarkLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = -568347655488626739L;

	private Boolean silent;

	public Boolean getSilent() {
		return silent;
	}

	public void setSilent(Boolean silent) {
		this.silent = silent;
	}

	public MarkLine silent(Boolean silent) {
		this.silent = silent;
		return this;
	}

}

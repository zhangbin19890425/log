package cn.sh.ideal.web.tomcat.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.github.abel533.echarts.Option;

import cn.sh.ideal.echarts.chart.BarOption;
import cn.sh.ideal.echarts.chart.LineOption;
import cn.sh.ideal.echarts.chart.PieOption;
import cn.sh.ideal.servlet.mvc.BaseController;
import cn.sh.ideal.web.tomcat.entity.TomcatAccessLogEntity;
import cn.sh.ideal.web.tomcat.service.TomcatService;

@Controller
@RequestMapping("tomcat")
public class TomcatController extends BaseController {
	Logger logger = Logger.getLogger(TomcatController.class);
	
	@Autowired
	private TomcatService tomcatService;

	@RequestMapping("tomcatTpl")
	public String getTomcatTpl(HttpServletRequest request) {
		return "tomcat/tomcatTpl";
	}

	@RequestMapping("test")
	public @ResponseBody String test() {
		// return tomcatService.getVisitTime("2016-07-04 00:00:01",
		// "2016-08-01 10:00:01");
		return "hello";
	}

	@RequestMapping("geoIpStatistics")
	public @ResponseBody String getgeoIpStatistics(@RequestParam(value = "analysisType") String analysisType, @RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate, HttpServletRequest request) throws Exception {
		Map<String, String> mapResult = tomcatService.getGeoIpAggregation(fromDate, toDate, analysisType);

		BarOption barOption = new BarOption();
		if ("errorPage".equals(analysisType)) {
			barOption.setTitle("Tomcat错误页面统计");
			barOption.setLegend("错误页面数");
			barOption.setyAxisName("错误页面数");
		} else if ("responseTime".equals(analysisType)) {
			barOption.setTitle("Tomcat页面响应时间统计");
			barOption.setLegend("响应时间");
			barOption.setDataType("Double");
			barOption.setyAxisName("时间(ms)");
		} else if ("accessTimes".equals(analysisType)) {
			barOption.setTitle("Tomcat访问次数统计");
			barOption.setLegend("访问次数");
			barOption.setyAxisName("次数");
		}

		barOption.setMapResult(mapResult);
		barOption.setChartColor("#2ec7c9");
		barOption.setBackgroundColor("#ffffff");
		barOption.setHasRotate(true);
		barOption.setxAxisName("主机IP");

		Option option = barOption.getOption();

		return JSONObject.toJSONString(option);

	}

	@RequestMapping("dateHisgramStatistics")
	public @ResponseBody String getDateHisgramStatistics(HttpServletRequest request, @RequestParam(value = "analysisType") String analysisType,
			@RequestParam(value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate, @RequestParam(value = "ip") String ip,
			@RequestParam(value = "drillingTime") String drillingTime, @RequestParam(value = "ymdTime") String ymdTime) throws Exception {

		if (drillingTime == null || "".endsWith(drillingTime)) {
			drillingTime = null;
		}
		if (ymdTime == null || "".endsWith(ymdTime)) {
			ymdTime = null;
		}
		Map<String, String> mapResult = tomcatService.getDateHisgram(fromDate, toDate, ip, drillingTime, ymdTime, analysisType);

		BarOption barOption = new BarOption();
		if ("errorPage".equals(analysisType)) {
			barOption.setTitle("Tomcat分时段错误页面统计(" + ip + ")");
			barOption.setLegend("错误页面数");
			barOption.setyAxisName("错误页面数");
		} else if ("responseTime".equals(analysisType)) {
			barOption.setTitle("Tomcat分时段页面响应时间统计(" + ip + ")");
			barOption.setLegend("响应时间");
			barOption.setDataType("Double");
			barOption.setyAxisName("时间(ms)");
		} else if ("accessTimes".equals(analysisType)) {
			barOption.setTitle("Tomcat分时段访问次数统计(" + ip + ")");
			barOption.setLegend("访问次数");
			barOption.setyAxisName("次数");
		}

		barOption.setMapResult(mapResult);
		barOption.setChartColor("#2ec7c9");
		barOption.setBackgroundColor("#ffffff");
		barOption.setxAxisName("时间");
		barOption.setHasRotate(false);

		Option option = barOption.getOption();

		return JSONObject.toJSONString(option);

	}

	@RequestMapping("wrongPageStatistics")
	public @ResponseBody String getwrongPageStatistics(HttpServletRequest request) throws Exception {
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ip = request.getParameter("ip");
		Map<String, String> mapResult = tomcatService.getWrongPageCount(fromDate, toDate, ip);

		LineOption lineOption = new LineOption();
		lineOption.setTitle("页面错误次数统计(" + ip + ")");
		lineOption.setLegend("错误次数");
		lineOption.setMapResult(mapResult);
		lineOption.setyAxisName("次数");
		lineOption.setxAxisName("URL");
		lineOption.setChartColor("#2ec7c9");
		lineOption.setBackgroundColor("#ffffff");
		lineOption.setHasRotate(false);

		Option option = lineOption.getOption();

		return JSONObject.toJSONString(option);
	}

	@RequestMapping("pageProcessTimeRank")
	public @ResponseBody String getPageProcessTimeRank(HttpServletRequest request) throws Exception {
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ip = request.getParameter("ip");
		Map<String, String> mapResult = tomcatService.getDealTime(fromDate, toDate, ip);

		BarOption barOption = new BarOption();
		barOption.setTitle("处理页面时间排名");
		barOption.setLegend("处理时间");
		barOption.setyAxisName("页面URL");
		barOption.setMapResult(mapResult);
		barOption.setxAxisName("时间(ms)");
		barOption.setChartColor("#2ec7c9");
		barOption.setBackgroundColor("#ffffff");
		barOption.setDataType("Double");
		barOption.setHasRotate(false);
		barOption.setyAxisBar(true);

		Option option = barOption.getOption();

		return JSONObject.toJSONString(option);
	}

	@RequestMapping("pageAccessFrequentlyStatistics")
	public @ResponseBody String getPageAccessStatistics(HttpServletRequest request) throws Exception {
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ip = request.getParameter("ip");
		Map<String, String> mapResult = tomcatService.getVisitFrequently(fromDate, toDate, ip);

		BarOption barOption = new BarOption();
		barOption.setTitle("页面访问频次统计");
		barOption.setLegend("访问频次");
		barOption.setMapResult(mapResult);
		barOption.setyAxisName("频次");
		barOption.setxAxisName("页面URL");
		barOption.setChartColor("#2ec7c9");
		barOption.setBackgroundColor("#ffffff");

		Option option = barOption.getOption();

		return JSONObject.toJSONString(option);
	}

	/**
	 * 明细信息展现接口
	 * 
	 * @param searchWord
	 * @param fromDate
	 * @param toDate
	 * @param hs
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("errorStatusStatistics")
	public @ResponseBody String getErrorStatusStatistics(@RequestParam(value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate,
			HttpServletRequest request) throws Exception {
		String url = request.getParameter("url");
		String ip = request.getParameter("ip");
		Map<String, String> mapResult = tomcatService.statusAnalysis(fromDate, toDate, ip, url);

		PieOption pieOption = new PieOption();
		pieOption.setTitle("页面错误状态码统计(" + url + ")");
		pieOption.setPieName("状态码");
		pieOption.setWidth("50%");
		pieOption.setHeight("55%");
		pieOption.setRadius("60%");
		pieOption.setBackgroundColor("#ffffff");
		pieOption.setMapResult(mapResult);

		Option option = pieOption.getOption();

		return JSONObject.toJSONString(option);
	}

	/**
	 * 明细信息展现接口
	 * 
	 * @param searchWord
	 * @param fromDate
	 * @param toDate
	 * @param hs
	 * @return
	 */
	@RequestMapping("detailInfo")
	public ModelAndView detailInfo(@RequestParam(value = "detailType") String detailType, @RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate, HttpSession hs, HttpServletRequest request) {
		String searchWord = request.getParameter("searchWord");
		String ip = request.getParameter("ip");
		List<TomcatAccessLogEntity> detailInfo = tomcatService.detailInfo(fromDate, toDate, detailType, searchWord, ip);

		ModelAndView mav = new ModelAndView("tomcat/tomcatDatatables");
		mav.addObject("tdataList", detailInfo);

		return mav;
	}

	/**
	 * 查询该秒的detail info
	 * 
	 * @param detailType
	 * @param sTime
	 * @param hs
	 * @param request
	 * @return
	 */
	@RequestMapping("sDetailInfo")
	public ModelAndView sDetailInfo(@RequestParam(value = "detailType") String detailType, @RequestParam(value = "sTime") String sTime, HttpSession hs, HttpServletRequest request) {
		String searchWord = request.getParameter("searchWord");
		String ip = request.getParameter("ip");
		String start = sTime + ".000";
		String end = sTime + ".999";
		List<TomcatAccessLogEntity> detailInfo = tomcatService.detailInfo(start, end, detailType, searchWord, ip);

		ModelAndView mav = new ModelAndView("tomcat/tomcatDatatables");
		mav.addObject("tdataList", detailInfo);

		return mav;
	}

	@RequestMapping("pageCompareAnalysis")
	public @ResponseBody String getCompareAnalysis(@RequestParam(value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate,
			@RequestParam(value = "timeRange") String timeRange, @RequestParam(value = "analysisTime") String analysisTime,
			@RequestParam(value = "analysisType") String analysisType, @RequestParam(value = "ip") String ip, HttpServletRequest request) throws Exception {
		Map<String, String> mapResult = tomcatService.compared(ip, timeRange, analysisTime, fromDate, toDate,analysisType);
		BarOption barOption = new BarOption();
		if ("errorPage".equals(analysisType)) {
			barOption.setTitle("Tomcat环比错误页面统计(" + ip + ")");
			barOption.setLegend("错误页面数");
			barOption.setyAxisName("错误页面数");
		} else if ("responseTime".equals(analysisType)) {
			barOption.setTitle("Tomcat环比页面响应时间统计(" + ip + ")");
			barOption.setLegend("响应时间");
			barOption.setDataType("Double");
			barOption.setyAxisName("时间(ms)");
		} else if ("accessTimes".equals(analysisType)) {
			barOption.setTitle("Tomcat环比访问次数统计(" + ip + ")");
			barOption.setLegend("访问次数");
			barOption.setyAxisName("次数");
		}

		barOption.setMapResult(mapResult);
		barOption.setChartColor("#2ec7c9");
		barOption.setBackgroundColor("#ffffff");
		barOption.setxAxisName("时间");
		barOption.setHasRotate(false);

		Option option = barOption.getOption();

		return JSONObject.toJSONString(option);
	}

}

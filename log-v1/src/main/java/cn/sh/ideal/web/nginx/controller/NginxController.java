package cn.sh.ideal.web.nginx.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.github.abel533.echarts.Option;

import cn.sh.ideal.echarts.chart.BarOption;
import cn.sh.ideal.echarts.chart.LineAreaOption;
import cn.sh.ideal.echarts.chart.LineOption;
import cn.sh.ideal.echarts.chart.PieOption;
import cn.sh.ideal.echarts.chart.StackBarOption;
import cn.sh.ideal.servlet.mvc.BaseController;
import cn.sh.ideal.web.nginx.entity.NginxAccessLogEntity;
import cn.sh.ideal.web.nginx.service.NginxService;

@Controller
@RequestMapping("/nginx")
public class NginxController extends BaseController {
	@Autowired
	private NginxService nginxService;

	@RequestMapping("nginxTpl")
	public String getNginxTpl() {
		return "nginx/nginxTpl";
	}

	@RequestMapping("demo")
	public String getdemo() {
		return "demo/components";
	}

	@RequestMapping("allView")
	public String getAllView() {
		return "demo/allView";
	}

	@RequestMapping("test")
	public @ResponseBody String test() throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("0点", "43|57");
		map.put("1点", "43|57");
		map.put("2点", "33|67");
		map.put("3点", "72|28");
		map.put("4点", "44|56");
		map.put("5点", "54|46");
		map.put("6点", "64|36");
		map.put("7点", "64|36");
		map.put("8点", "54|46");
		map.put("9点", "44|56");
		map.put("10点", "14|86");
		map.put("11点", "66|34");
		map.put("12点", "84|16");
		map.put("13点", "33|67");
		map.put("14点", "33|67");
		map.put("15点", "43|57");
		map.put("16点", "38|62");
		map.put("17点", "58|42");
		map.put("18点", "44|56");
		map.put("19点", "64|36");
		map.put("20点", "67|33");
		map.put("21点", "45|55");
		map.put("22点", "14|86");
		map.put("23点", "10|90");
		ArrayList<String> lenged = new ArrayList<String>();
		lenged.add("已使用");
		lenged.add("空闲");
		ArrayList<Object> colors = new ArrayList<Object>();
		colors.add("#A0522D");
		colors.add("#66CDAA");
		StackBarOption barOption = new StackBarOption();
		barOption.setMapResult(map);
		barOption.setTitle("JVM使用情况");
		barOption.setLegendList(lenged);
		barOption.setyAxisName("JVM");

		barOption.setChartColors(colors);
		barOption.setBackgroundColor("#ffffff");
		barOption.setxAxisName("时间");
		barOption.setStack("JVM总量");

		Option option = barOption.getOption();

		return JSONObject.toJSONString(option);
	}

	/**
	 * nginx访问次数统计
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("geoIpStatistics")
	public @ResponseBody String getgeoIpStatistics(@RequestParam(value = "analysisType") String analysisType, @RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate, HttpServletRequest request) throws Exception {
		Map<String, String> mapResult = nginxService.getGeoIpAggregation(fromDate, toDate, analysisType);

		BarOption barOption = new BarOption();
		if ("errorPage".equals(analysisType)) {
			barOption.setTitle("Nginx错误页面统计");
			barOption.setLegend("错误页面数");
			barOption.setyAxisName("错误页面数");
		} else if ("responseTime".equals(analysisType)) {
			barOption.setTitle("Nginx页面响应时间统计");
			barOption.setLegend("响应时间");
			barOption.setDataType("Double");
			barOption.setyAxisName("时间(ms)");
		} else if ("accessTimes".equals(analysisType)) {
			barOption.setTitle("Nginx访问次数统计");
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
		Map<String, String> mapResult = nginxService.getDateHisgram(fromDate, toDate, ip, drillingTime, ymdTime, analysisType);

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

	/**
	 * nginx 状态码统计
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("nginxStatusStatistics")
	public @ResponseBody String getNginxStatusStatistics(HttpServletRequest request) throws Exception {
		String ip = request.getParameter("ip");
		Map<String, String> mapResult = nginxService.getStatusStatistics(ip);

		PieOption pieOption = new PieOption();
		pieOption.setTitle("状态码统计");
		pieOption.setPieName("状态码");
		pieOption.setWidth("50%");
		pieOption.setHeight("55%");
		pieOption.setRadius("60%");
		pieOption.setBackgroundColor("#ffffff");
		pieOption.setMapResult(mapResult);

		Option option = pieOption.getOption();

		return JSONObject.toJSONString(option);
	}

	@RequestMapping("wrongPageStatistics")
	public @ResponseBody String getwrongPageStatistics(HttpServletRequest request) throws Exception {
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ip = request.getParameter("ip");
		Map<String, String> mapResult = nginxService.getWrongPageCount(fromDate, toDate, ip);

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

	/**
	 * 页面处理时间排名
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("pageProcessTimeRank")
	public @ResponseBody String getPageProcessTimeRank(HttpServletRequest request) throws Exception {
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ip = request.getParameter("ip");
		Map<String, String> mapResult = nginxService.getDealTime(fromDate, toDate, ip);

		BarOption barOption = new BarOption();
		barOption.setTitle("处理页面时间排名");
		barOption.setLegend("处理时间");
		barOption.setMapResult(mapResult);
		barOption.setxAxisName("(ms)");
		barOption.setChartColor("#2ec7c9");
		barOption.setBackgroundColor("#ffffff");
		barOption.setDataType("Double");
		barOption.setHasRotate(false);
		barOption.setyAxisBar(true);

		Option option = barOption.getOption();

		return JSONObject.toJSONString(option);
	}

	/**
	 * 页面访问频次统计
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("pageAccessFrequentlyStatistics")
	public @ResponseBody String getPageAccessStatistics(HttpServletRequest request) throws Exception {
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ip = request.getParameter("ip");
		Map<String, String> mapResult = nginxService.getVisitFrequently(fromDate, toDate, ip);

		BarOption barOption = new BarOption();
		barOption.setTitle("页面访问频次统计");
		barOption.setLegend("访问频次");
		barOption.setMapResult(mapResult);
		barOption.setyAxisName("频次");
		barOption.setChartColor("#2ec7c9");
		barOption.setBackgroundColor("#ffffff");

		Option option = barOption.getOption();

		return JSONObject.toJSONString(option);
	}

	/**
	 * 每秒钟请求次数统计
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("nginxSecondAccessTimes")
	public @ResponseBody String getNginxSecondAccessTimes(HttpServletRequest request) throws Exception {
		Map<String, String> mapResult = nginxService.getResNumSecondAggregation("2016-07-04 00:00:01", "2016-07-05 10:00:01", "127.0.0.1");

		LineAreaOption lieAreaOption = new LineAreaOption();
		lieAreaOption.setTitle("每秒钟请求次数统计");
		lieAreaOption.setLegend("请求次数");
		lieAreaOption.setMapResult(mapResult);
		lieAreaOption.setyAxisName("次数");
		lieAreaOption.setChartColor("#2ec7c9");
		lieAreaOption.setBackgroundColor("#ffffff");
		lieAreaOption.setHasRotate(false);

		Option option = lieAreaOption.getOption();

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
		Map<String, String> mapResult = nginxService.statusAnalysis(fromDate, toDate, ip, url);

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
		List<NginxAccessLogEntity> detailInfo = nginxService.detailInfo(fromDate, toDate, detailType, searchWord, ip);

		ModelAndView mav = new ModelAndView("nginx/nginxDatatables");
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

		List<NginxAccessLogEntity> detailInfo = nginxService.detailInfo(start, end, detailType, searchWord, ip);
		ModelAndView mav = new ModelAndView("nginx/nginxDatatables");
		mav.addObject("tdataList", detailInfo);

		return mav;
	}

}

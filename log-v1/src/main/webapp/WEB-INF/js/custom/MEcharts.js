if (typeof MECHART !== 'object') {
	MECHART = {};
}

(function() {
	'use strict';
	/**
	 * 获取option
	 * 
	 * @param url
	 * @param param
	 * @returns
	 */
	function getOption(url, param) {
		var option;
		// ajax getting data...............
		// $.ajaxSettings.async = false; //同步获取数据
		$.ajax({
			url : url, // 请求的url地址
			dataType : "json", // 返回格式为json
			async : false, // 请求是否异步，默认为异步，这也是ajax重要特性
			data : param, // 参数值
			type : "POST", // 请求方式
			beforeSend : function() {
				// 请求前的处理
			},
			success : function(result) {
				if(typeof result === 'object'){
					option = result;
				}else if(typeof result === 'string'){
					option = JSON.parse(result);
				}else{
					throw new Error('[RESULT_ERROR] type error!');
				}
			},
			complete : function() {
				// 请求结束的处理
			},
			error : function() {
				alert("获取数据失败！");
			}
		});

		return option;
	}
	
	/**
	 * init chart
	 */
	MECHART.initChart = function initChart(){
		var domId;
		var len = arguments.length;
		if (len < 1) {
			throw new Error('[ARGUMENTS_MISS] domId is not exists!');
		}
		domId = arguments[0];
		return echarts.init(document.getElementById(domId));
	}

	/**
	 * display chart
	 * 
	 * @param url
	 * @param param
	 */
	MECHART.displayChart = function displayChart() {
		var _myChart, url, param;
		var len = arguments.length;
		if (len < 2) {
			throw new Error('[ARGUMENTS_MISS] chart or url is not exists!');
		} else if (len == 2) {
			_myChart = arguments[0];
			url = arguments[1];
			param = {};
		} else if (len == 3) {
			_myChart = arguments[0];
			url = arguments[1];
			param = arguments[2];
		}
		/*
		if(typeof echarts.getInstanceByDom(document.getElementById(domId))==='undefined'){
			_myChart  = echarts.init(document.getElementById(domId));
		}else{
			_myChart = echarts.getInstanceByDom(document.getElementById(domId));
		}
		 */
		// 过渡---------------------
		_myChart.showLoading({
			text : '正在努力的读取数据中...', // loading话术
		});
		var option = getOption(url, param);
		// ajax callback
		_myChart.hideLoading();

		// 图表清空-------------------
		_myChart.clear();
		_myChart.setOption(option);

		//return _myChart;
	}
}());

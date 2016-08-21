/**
 * Created by wk on 2016/7/2.
 */


line_liandong_i_option = {
    title: {
        text: 'tomcat时间统计',
        subtext: '纯属虚构',
        textStyle:{
            fontSize:12
        }
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    calculable: true,
    grid:{
        top:25,
      bottom:25
    },
    xAxis: [
        {
            type: 'category',
            data: ['1点', '2点', '3点', '4点', '5点', '6点', '7点', '8点', '9点', '10点', '11点', '12点',
                '13点', '14点', '15点', '16点', '17点', '18点', '19点', '20点', '21点', '22点', '23点', '24点'],
            axisLabel: {
                interval: 0,
                rotate: 45,
                margin: 2
            }
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: '响应时间',
            type: 'line',
            data: [0.6, 0.9, 0.4, 1.2, 0.7, 0.8, 0.1, 0.7, 0.5, 1.4, 0.9, 0.9,
                0.7, 1.0, 0.8, 1.3, 2.0, 2.1, 1.1, 1.4, 0.7, 0.6, 0.5, 0.5],
            
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        }
    ],
    color: [
        '#2ec7c9'
    ],
    backgroundColor: '#ffffff'
};

line_liandong_i_option_ii = {
	    title: {
	        text: 'CPU使用率',
	        subtext: '纯属虚构',
	        textStyle:{
	            fontSize:12
	        }
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    calculable: true,
	    grid:{
	        top:25,
	        bottom:25
	    },
	    xAxis: [
	        {
	            type: 'category',
	            data: ['1点', '2点', '3点', '4点', '5点', '6点', '7点', '8点', '9点', '10点', '11点', '12点',
	                '13点', '14点', '15点', '16点', '17点', '18点', '19点', '20点', '21点', '22点', '23点', '24点'],
	            axisLabel: {
	                interval: 0,
	                rotate: 45,
	                margin: 2
	            }
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name: 'cpu利用率',
	            type: 'line',
	            data: [15.0, 23.0, 11.0, 30.0, 18.0, 20.0, 3.0, 18.0, 13.0, 35.0, 23.0, 23.0,
	                18.0, 25.0, 21.0, 33.0, 49.0, 53.0, 28.0, 35.0, 18.0, 15.0, 13.0, 13.0],
	                
	            markLine: {
	                data: [
	                    {type: 'average', name: '平均值'}
	                ]
	            }
	        }
	    ],
	    color: [
	        '#2ec7c9'
	    ],
	    backgroundColor: '#ffffff'
	};

line_liandong_i_option_iii = {
	    title: {
	        text: 'Memory使用率',
	        subtext: '纯属虚构',
	        textStyle:{
	            fontSize:12
	        }
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    calculable: true,
	    grid:{
	        top:25,
	        bottom:25
	    },
	    xAxis: [
	        {
	            type: 'category',
	            data: ['1点', '2点', '3点', '4点', '5点', '6点', '7点', '8点', '9点', '10点', '11点', '12点',
	                '13点', '14点', '15点', '16点', '17点', '18点', '19点', '20点', '21点', '22点', '23点', '24点'],
	            axisLabel: {
	                interval: 0,
	                rotate: 45,
	                margin: 2
	            }
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name: 'Memory使用率',
	            type: 'line',
	            data: [31.0, 47.0, 20.0, 62.0, 35.0, 42.0, 10.0, 35.0, 25.0, 56.0, 45.0, 47.0,
	               35.0, 48.0, 39.0, 52.0, 67.0, 69.0, 52.0, 58.0, 35.0, 29.0, 24.0, 24.0],
                
	            markLine: {
	                data: [
	                    {type: 'average', name: '平均值'}
	                ]
	            }
	        }
	    ],
	    color: [
	        '#2ec7c9'
	    ],
	    backgroundColor: '#ffffff'
	};



tomcat_bar_i_option = {
    title: {
        text: 'Tomcat页面响应时间统计',
        subtext: '纯属虚构'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    legend: {
        data: ['响应时间']
    },
  
    calculable: true,
    xAxis: [
        {
            type: 'category',
            data: ['1点', '2点', '3点', '4点', '5点', '6点', '7点', '8点', '9点', '10点', '11点', '12点',
                '13点', '14点', '15点', '16点', '17点', '18点', '19点', '20点', '21点', '22点', '23点', '24点']
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: '响应时间',
            type: 'bar',
            data: [0.6, 0.9, 0.4, 1.2, 0.7, 0.8, 0.1, 0.7, 0.5, 1.4, 0.9, 0.9,
                0.7, 1.0, 0.8, 1.3, 2.0, 2.1, 1.1, 1.4, 0.7, 0.6, 0.5, 0.5],
            markPoint: {
                data: [
                    {name: '时间(s)', value: 0.6, xAxis: 0, yAxis: 0.6}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        }
    ],
    color: [
        '#2ec7c9'
    ],
    backgroundColor:'#f0f0f0'
};

tomcat_line_i_option = {
    title : {
        text: '404次数统计',
        subtext: '纯属虚构'
    },
    tooltip : {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    legend: {
        data:['404次数']
    },
    /*
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    */
    calculable : true,
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : ['页面1','页面2','页面3', '页面4','页面5','页面6', '页面7','页面8','页面9', '页面10'],
            axisLabel:{
                formatter:function(val){
                    return val.split("").join("\n");
                }
            }
        }

    ],
    yAxis : [
        {
            type : 'value',
            axisLabel : {
                formatter: '{value} (w)'
            }
        }
    ],
    series : [
        {
            name:'404次数',
            type:'line',
            data:[8.0, 7.5, 9.0, 7.7777, 6.5, 9.9999, 10.0, 4.5, 3.0, 2.0],
            markPoint : {
                data : [
                    {name : '', value : 8.0, xAxis: 0, yAxis: 8.0}
                ]
            },
            markLine : {
                data : [
                    {type : 'average', name : '平均值'}
                ]
            }
        }
    ],
    color: [
        '#2ec7c9'
    ],
    backgroundColor:'#f0f0f0'
};

tomcat_bar_ii_option = {
    title: {
        text: '处理页面时间排名',
        subtext: '虚构'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    legend: {
        data: ['处理时间']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'value',
        //boundaryGap: [0, 0.01],
        axisLabel : {
            formatter: '{value} (s)'
        }
    },
    yAxis: {
        type: 'category',
        data : ['页面1','页面2','页面3', '页面4','页面5','页面6', '页面7','页面8','页面9', '页面10'],
    },
    series: [
        {
            name: '处理时间',
            type: 'bar',
            data:[8.0, 7.5, 9.0, 7.7777, 6.5, 9.9999, 10.0, 4.5, 3.0, 2.0],
            // markPoint: {
            //     data: [
            //         {name: '', value: 8.0, xAxis: 8.0, yAxis: 0}
            //     ]
            // },
        }
    ],
    color: [
        '#2ec7c9'
    ],
    backgroundColor:'#f0f0f0'
};

tomcat_bar_iii_option = {
    title: {
        text: '页面访问频次统计',
        subtext: '纯属虚构'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['响应时间']
    },
    /*
     toolbox: {
     show: true,
     feature: {
     dataView: {show: true, readOnly: false},
     magicType: {show: true, type: ['line', 'bar']},
     restore: {show: true},
     saveAsImage: {show: true}
     }
     },*/
    calculable: true,
    xAxis: [
        {
            type: 'category',
            data : ['页面1','页面2','页面3', '页面4','页面5','页面6', '页面7','页面8','页面9', '页面10'],
            axisLabel:{
                formatter:function(val){
                    return val.split("").join("\n");
                }
            }
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: '响应时间',
            type: 'bar',
            data:[8.0, 7.5, 9.0, 7.7777, 6.5, 9.9999, 10.0, 4.5, 3.0, 2.0],
            markPoint: {
                data: [
                    {name: '时间(s)', value: 0.6, xAxis: 0, yAxis: 8}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        }
    ],
    color: [
        '#2ec7c9'
    ],
    backgroundColor:'#f0f0f0'
};

/**
 * Created by wk on 2016/7/2.
 */


allview_bar_i_option = {
		title: {
	        text: 'JVM使用情况', 
	        x:'center',
	        textStyle:{
	            fontSize:12
	        }
	    },
	    legend: {
	        orient: 'vertical',
	        left: 'right',
	        data: ['已用', '剩余', '总量']
	    },
		tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    	   
	    calculable : true,
	    xAxis: [
		        {
		            type: 'category',
		            data: ['1点', '2点', '3点', '4点', '5点', '6点', '7点', '8点', '9点', '10点', '11点', '12点',
		                '13点', '14点', '15点', '16点', '17点', '18点', '19点', '20点', '21点', '22点', '23点', '24点'],
		            axisLabel: {
		                interval: 0,
		                rotate: 45,
		                margin: 2
		            }
		        }
		    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	        {
			            name:'已用',
			            type:'bar',
			            data: [3200, 3100, 2600, 3500, 3800, 4100, 2500, 4600, 5200, 3500, 3900, 2900,
			 	                4700, 2900, 2800, 2300, 2700, 2800, 3100, 3400, 3700, 3600, 3500, 3500],
			 	               markLine: {
			 	                  data: [
			 	                      {type: 'average', name: '平均值'}
			 	                  ]
			 	              }
			 	               
			        },
			        {
			            name:'剩余',
			            type:'bar',
			            data: [4800, 4900, 5400, 4500, 4200, 3900, 5500, 3400, 2800, 4500, 4100, 5100,
			 	                3300, 5100, 5200, 5700, 5300, 5200, 4900, 5600, 4300, 4400, 4500, 4500],
			 	               markLine: {
			 	                  data: [
			 	                      {type: 'average', name: '平均值'}
			 	                  ]
			 	              }
			        },
			        {
			            name:'总量',
			            type:'bar',
			            data: [8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000,
			 	                8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000],
			        markLine: {
	 	                  data: [
	 	                      {type: 'average', name: '平均值'}
	 	                  ]
	 	              }
			 	               
			        }
	    ],
	    color: [
             '#2ec7c9','#7FFF00','#8B3A3A'
	    ],
	    backgroundColor: '#ffffff'
	};

allview_bar_ii_option = {
		title: {
	        text: '连接池使用情况',
	        x:'center',
	        textStyle:{
	            fontSize:12
	        }
	    },
	    legend: {
	        orient: 'vertical',
	        left: 'right',
	        data: ['已用', '剩余', '总量']
	    },
		tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    	   
	    calculable : true,
	    xAxis: [
		        {
		            type: 'category',
		            data: ['1点', '2点', '3点', '4点', '5点', '6点', '7点', '8点', '9点', '10点', '11点', '12点',
		                '13点', '14点', '15点', '16点', '17点', '18点', '19点', '20点', '21点', '22点', '23点', '24点'],
		            axisLabel: {
		                interval: 0,
		                rotate: 45,
		                margin: 2
		            }
		        }
		    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	        {
			            name:'已用',
			            type:'bar',
			            data: [60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115,
			 	                105, 65, 80, 85, 90, 100, 85, 95, 75, 70, 100, 85],
			 	               markLine: {
			 	                  data: [
			 	                      {type: 'average', name: '平均值'}
			 	                  ]
			 	              }
			 	               
			        },
			        {
			            name:'剩余',
			            type:'bar',
			            data: [140, 135, 130, 125, 120, 115, 110, 105, 100, 95, 90, 85,
			 	                95, 135, 120, 115, 110, 100, 115, 105, 125, 130, 100, 115],
			 	               markLine: {
			 	                  data: [
			 	                      {type: 'average', name: '平均值'}
			 	                  ]
			 	              }
			        },
			        {
			            name:'总量',
			            type:'bar',
			            data: [200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
			 	                200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200],
			        markLine: {
	 	                  data: [
	 	                      {type: 'average', name: '平均值'}
	 	                  ]
	 	              }
			 	               
			        }
	    ],
	    color: [
	        '#2ec7c9','#7FFF00','#8B3A3A'
	    ],
	    backgroundColor: '#ffffff'
	};




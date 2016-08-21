/**
 * Created by wk on 2016/7/2.
 */


allview_bar_i_option = {
		title: {
	        text: 'JVM使用情况',
	        subtext: '纯属虚构',
	        textStyle:{
	            fontSize:12
	        }
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
			            data: [2.4, 3.1, 2.6, 3.5, 3.8, 4.1, 2.5, 4.6, 5.2, 3.5, 3.9, 2.9,
			 	                4.7, 2.9, 2.8, 2.3, 2.7, 2.8, 3.1, 3.4, 3.7, 3.6, 3.5, 3.5],
			 	               markLine: {
			 	                  data: [
			 	                      {type: 'average', name: '平均值'}
			 	                  ]
			 	              }
			 	               
			        },
			        {
			            name:'剩余',
			            type:'bar',
			            data: [5.6, 4.9, 5.4, 4.5, 4.2, 3.9, 5.5, 3.4, 2.8, 4.5, 4.1, 5.1,
			 	                3.3, 5.1, 5.2, 5.7, 5.3, 5.2, 4.9, 5.6, 4.3, 4.4, 4.5, 4.5],
			 	               markLine: {
			 	                  data: [
			 	                      {type: 'average', name: '平均值'}
			 	                  ]
			 	              }
			        },
			        {
			            name:'总量',
			            type:'bar',
			            data: [8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
			 	                8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8]
			 	               
			        }
	    ],
	    color: [
	        '#2ec7c9'
	    ],
	    backgroundColor: '#ffffff'
	};

allview_bar_ii_option = {
		title: {
	        text: '连接池使用情况',
	        subtext: '纯属虚构',
	        textStyle:{
	            fontSize:12
	        }
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
			 	                200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200]
			 	               
			        }
	    ],
	    color: [
	        '#2ec7c9'
	    ],
	    backgroundColor: '#ffffff'
	};


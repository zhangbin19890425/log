/**
 * Created by wk on 2016/7/3.
 */
ngnix_bar_i_option = {
    title: {
        text: 'Ngnix节点访问次数',
        subtext: '纯属虚构'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['访问次数']
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
            data: ['192.168.1.100', '192.168.1.101', '192.168.1.103', '192.168.1.104',
                '192.168.1.105', '192.168.1.106', '192.168.1.107', '192.168.1.108',
                '192.168.1.109', '192.168.1.110', '192.168.1.111', '192.168.1.112',
                '192.168.1.113', '192.168.1.114', '192.168.1.115', '192.168.1.116',
                '192.168.1.117', '192.168.1.118', '192.168.1.119', '192.168.1.120',
                '192.168.1.121', '192.168.1.122', '192.168.1.123', '192.168.1.124'],

            axisLabel:{
                interval: 0,
                rotate:45,
                margin:2,
                textStyle:{
                    color:'#2ec7c9'
                },
            }
        }
    ],

    grid: { // 控制图的大小，调整下面这些值就可以，
       // x: 40,
        //x2: 100,
        //y2: 120,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
    },

    yAxis: [
        {
            axisLabel : {
                formatter: '{value} (w)'
            }
        }
    ],
    series: [
        {
            name: '访问次数',
            type: 'bar',
            data: [32.2223, 22.3122, 12.3322, 22.3321, 12.3222, 11.222, 9.021, 12.1111, 20.223, 18.342, 8.321, 9.222,
                18.234, 11.231, 12.22, 6.23, 5.89, 14.31, 11.0, 12.22, 8.34, 7.46, 9.75, 6.48],
            markPoint: {
                data: [
                    {name: '次数(w)', value: 32.2223, xAxis: 0, yAxis: 32.2223}
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

ngnix__line_i_option = {
    title : {
        text: '每秒钟请求次数计',
        subtext: '纯属虚构'
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['请求次数']
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
            data : ['13:38:01','13:38:02','13:38:03', '13:38:04','13:38:05','13:38:06', '13:38:07',
                '13:38:08','13:38:09', '13:38:10'],
            axisLabel:{
                interval: 0,
                rotate:45,
                margin:2,
                textStyle:{
                    color:'#2ec7c9'
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
            name:'请求次数',
            type:'line',
            data:[3.0, 1.5, 0.90, 0.73, 2.1, 1.2, 1.11, 0.43, 0.23, 1.12],
            markPoint : {
                data : [
                    {name : '', value : 3.0, xAxis: 0, yAxis: 3.0}
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

ngnix_bar_ii_option = {
    title: {
        text: '服务器错误率统计',
        subtext: '纯属虚构'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['错误率']
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
            data : ['13:38:01','13:38:02','13:38:03', '13:38:04','13:38:05','13:38:06', '13:38:07',
                '13:38:08','13:38:09', '13:38:10'],
            axisLabel:{
                interval: 0,
                rotate:45,
                margin:2,
                textStyle:{
                    color:'#2ec7c9'
                }
            }
        }
    ],
    yAxis: [
        {
            type: 'value',
            axisLabel : {
                formatter: '{value} (%)'
            }
        }
    ],
    series: [
        {
            name: '错误率',
            type: 'bar',
            data:[0.01, 0.003, 0.005, 0.012, 0.0003, 0.0028, 0.028, 0.0001, 0.007, 0.012],
            markPoint: {
                data: [
                    {name: '错误率', value: 0.01, xAxis: 0, yAxis: 0.01}
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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>Nginx</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5/css/bootstrap.min.css">
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-datetimepicker.min.css">
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/custom/nav-header.css">
	<style type="text/css">
       .modal{ overflow: auto !important;}
        .list-group a:hover{
            z-index: 2;
            color: #fff;
            background-color: #43CD80;
            border-color: #43CD80;
            cursor:pointer;
        }
        .col-md-1, .col-md-2, .col-md-3,
        .col-md-4, .col-md-5, .col-md-6,
        .col-md-7, .col-md-8, .col-md-9,
        .col-md-10, .col-md-11, .col-md-12 {
            padding-left: 5px;
            padding-right: 5px;
        }
        .panel{
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<!-- header -->
<jsp:include page="/common/nav-header.jsp"/>

<div class="container-fluid">
	<div class="row">
	<div class="col-md-2" style="padding-top: 10px;">
        <div class="list-group">
             <a href="${pageContext.request.contextPath}/tomcat/tomcatTpl" class="list-group-item">Tomcat分析</a>
             <a href="${pageContext.request.contextPath}/nginx/nginxTpl" class="list-group-item">Nginx分析</a>
             <a href="${pageContext.request.contextPath}/nginx/allView" class="list-group-item active">综合分析</a>
             <%-- <a href="${pageContext.request.contextPath}/nginx/demo" class="list-group-item">demo页</a> --%>
         </div>
    </div>
    <div class="col-md-10">
	<div class="col-md-12" style="margin: 10px 0px;">
        <form id="sform" action="" class="form-horizontal" role="form">
            <div class="col-md-5">
                <label class="col-md-4 control-label">开始时间：</label>
                <div class="col-md-8">
                    <input id="fromDate"  name="fromDate" class="form-control date" size="16" type="text" value="">
                </div>
            </div>

            <div class="col-md-5">
                <label class="control-label col-md-4">结束时间：</label>
                <div class="col-md-8">
                    <input id="toDate" name="toDate" class="form-control date" size="16" type="text" value="">
                </div>
            </div>
            <input type="text" id="ip" name="ip" class="hidden">
            <!--
            <button type="button" class="btn btn-primary">查询</button>
            -->
            <button type="button" class="btn btn-default" onclick="_search();">查询</button>

        </form>
    </div>

	<div class="col-md-12">
           <ul class="nav nav-tabs">
            <li role="presentation"><a href="${pageContext.request.contextPath}/nginx/allView">机器A</a></li>
            <li role="presentation"><a href="${pageContext.request.contextPath}/nginx/allView">机器B</a></li>
            <li role="presentation"><a href="${pageContext.request.contextPath}/nginx/allView">机器C</a></li>
            <li role="presentation"><a href="${pageContext.request.contextPath}/nginx/allView">机器D</a></li>
            <li role="presentation"><a href="${pageContext.request.contextPath}/nginx/allView">机器E</a></li>
            <li role="presentation"><a href="${pageContext.request.contextPath}/nginx/allView">机器F</a></li>
            </ul>
	</div>

      <div class="col-md-12">
           <ul class="nav nav-tabs">
             <li role="presentation"><a href="#line_lidong_i">tomcat统计时间</a></li>
             <li role="presentation"><a href="#line_lidong_ii">CPU使用率</a></li>
             <li role="presentation"><a href="#line_lidong_iii">Momney使用率</a></li>
             <li role="presentation"><a href="#allview_bar_i">JVM使用情况</a></li>
             <li role="presentation"><a href="#allview_bar_ii">连接池使用情况</a></li>
           </ul>
        </div>

        
	    <div class="col-md-12">  
	     	<div class="panel panel-default">
	             <div class="panel-body">
	                 <div id="line_lidong_i" style="height:  200px;"></div>
	                 <div id="line_lidong_ii" style="height: 200px;"></div>
	                 <div id="line_lidong_iii" style="height: 200px;"></div>
	             </div>
             </div>
   		</div>
     
	    <div id="section_2" class="col-md-12">
	         <div class="panel panel-default">
	             <div class="panel-body">
	                 <div id="allview_bar_i" style="height:  200px;"></div>
	                 <div id="allview_bar_ii" style="height: 200px;"></div> 
	             </div>
	         </div>
	    </div>
	    <div id="section_3" class="col-md-12">
	         <div class="panel panel-default">
	             <div class="panel-body">
	                 <div id="bar_ii" style="height: 350px;"></div> 
	             </div>
	         </div>
	    </div>
    </div>
</div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.serializeObject.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/MEcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/option_component.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/option_tomcat.js"></script>
<script type="text/javascript">
	$(function(){
	    //init datetimepicker
	    $('.date').datetimepicker({
	        language:  'zh-CN',
	        format:'yyyy-mm-dd hh:ii:ss',
	        weekStart: 1,
	        todayBtn:  1,
	        autoclose: 1,
	        todayHighlight: 1,
	        startView: 2,
	        forceParse: 0,
	        //showMeridian: 1,
	        minuteStep:5    //分钟步长
	    });
	    
	    var line_lidong_i = echarts.init(document.getElementById('line_lidong_i'));
	    line_lidong_i.setOption(line_liandong_i_option);
	    var line_lidong_ii = echarts.init(document.getElementById('line_lidong_ii'));
	    line_lidong_ii.setOption(line_liandong_i_option_ii);
	    var line_lidong_iii = echarts.init(document.getElementById('line_lidong_iii'));
	    line_lidong_iii.setOption(line_liandong_i_option_iii);
	    
	    echarts.connect([line_lidong_i, line_lidong_ii,line_lidong_iii]);
	    
	    var allview_bar_i = echarts.init(document.getElementById('allview_bar_i'));
	    allview_bar_i.setOption(allview_bar_i_option);
	    var allview_bar_ii = echarts.init(document.getElementById('allview_bar_ii'));
	    allview_bar_ii.setOption(allview_bar_ii_option);
	});
	
	function checksearchtime(){
		if($('#fromDate').val()==null ||$('#fromDate').val() == ""){
			return false;
		}
		if($('#toDate').val()==null ||$('#toDate').val() == ""){
			return false;
		}
		return true;
	}
	
	<!--nginx分布统计-->
	function _search(){
		/*if(!checksearchtime()){
			alert("请填写查询时间!!");
			return;
		}*/
		
		var bar_ii = MECHART.initChart("bar_ii");
        MECHART.displayChart(bar_ii,"${pageContext.request.contextPath}/nginx/test");
        bar_ii.on('click', function (params) {
           console.log(params);
           alert("name = "+params.name + "  seriesName = "+params.seriesName + "  value = "+params.value);
        });
	}
	
</script>
</body>
</html>
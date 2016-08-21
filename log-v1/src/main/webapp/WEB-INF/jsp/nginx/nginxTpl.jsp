<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>Nginx</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/ui-dialog.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5/css/bootstrap.min.css">
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-datetimepicker.min.css">
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/custom/nav-header.css">
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/assets/font-awesome/css/font-awesome.min.css">
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
             <a href="${pageContext.request.contextPath}/nginx/nginxTpl" class="list-group-item active">Nginx分析</a>
             <a href="${pageContext.request.contextPath}/nginx/allView" class="list-group-item">综合分析</a>
             <%-- <a href="${pageContext.request.contextPath}/nginx/demo" class="list-group-item">demo页</a> --%>
         </div>
    </div>
    <div class="col-md-10">
	<div class="col-md-12" style="margin: 10px 0px;">
        <form id="sform" class="form-horizontal" role="form">
              <div class="col-md-3">
                  <label for="type" class="col-md-4 control-label">分析类型:</label>
                  <div class="col-md-8">
                      <select class="form-control" id="analysisType" name="analysisType">
                          <option value="errorPage">错误页面</option>
                          <option value="responseTime">响应时间</option>
                          <option value="accessTimes">访问次数</option>
                      </select>
                  </div>
              </div>
              <div class="col-md-3">
                  <label for="fromDate" class="col-md-4 control-label">开始时间:</label>
                  <div class="col-md-8">
                      <input id="fromDate"  name="fromDate" class="form-control date" size="16" type="text" value="">
                  </div>
              </div>
              <div class="col-md-3">
                  <label for="toDate" class="col-md-4 control-label">结束时间：</label>
                  <div class="col-md-8">
                      <input id="toDate" name="toDate" class="form-control date" size="16" type="text" value="">
                  </div>
              </div>
              <div class="col-md-3">
                  <button type="button" class="btn btn-default" onclick="_search();">查询</button>
              </div>
              <input type="text" id="ip" name="ip" class="hidden">
             <input type="text" id="detailType" name="detailType" class="hidden">
             <input type="text" id="searchWord" name="searchWord" class="hidden">
             <input type="text" id="drillingTime" name="drillingTime" class="hidden">
             <input type="text" id="ymdTime" name="ymdTime" class="hidden">
             <input type="text" id="url" name="url" class="hidden">
             <input type="text" id="sTime" name="sTime" class="hidden">
        </form>
             <input type="text" id="mTime" class="hidden"><!-- 保存分钟时间 -->
    </div>
	 
    <div class="col-md-12">
          <div class="panel panel-default">
              <!--
              <div class="panel-heading">Panel heading without title</div>
              -->
              <div class="panel-body">
                  <div id="bar_parent" style="height: 350px;"></div>
              </div>
          </div>
   	</div>
   	<div id="ii_container" class="hidden">
       <div class="col-md-12">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="bar_i" style="height: 350px;"></div>
                </div>
            </div>
        </div>
        
<!--   环比start
       <div class="col-md-12">
              <div class="panel panel-default">
                  
                  <div class="panel-heading">Panel heading without title</div>
                 
                  <div style="height: 30px; padding: 5px;">
                      <form id="tform" class="form-horizontal" role="form">
                          <div class="col-md-2">
                              <label for="analysisTime" class="col-md-7 control-label">时间跨度:</label>
                              <div class="col-md-5">
                                  <select class="form-control input-sm" id="timeRange" name="timeRange" onchange="timeRangeChange(this.options[this.options.selectedIndex].value);">
                                      <option value="month">年</option>
                                      <option value="day">月</option>
                                      <option value="hour">日</option>
                                  </select>
                              </div>
                          </div>
                          <div class="col-md-3">
                              <label for="fromDate" class="col-md-4 control-label">开始时间:</label>
                              <div class="col-md-8">
                                  <input id="fromDate1" name="fromDate" class="form-control input-sm" size="16"
                                         type="text" value="">
                              </div>
                          </div>
                          <div class="col-md-3">
                              <label for="toDate" class="col-md-4 control-label">结束时间：</label>
                              <div class="col-md-8">
                                  <input id="toDate1" name="toDate" class="form-control input-sm" size="16"
                                         type="text" value="">
                              </div>
                          </div>
                          <div class="col-md-2">
                              <label for="analysisTime" class="col-md-5 control-label">时间点:</label>
                              <div class="col-md-6">
                                  <select class="form-control input-sm" id="analysisTime" name="analysisTime">
                                  </select>
                              </div>
                          </div>
                          <div class="col-md-2">
                              <button type="button" class="btn btn-default input-sm" onclick="_analysis();">同比分析
                              </button>
                          </div>
                          <input type="text" id="t_ip" name="ip" class="hidden">
                          <input type="text" id="t_analysisType" name="analysisType" class="hidden">
                      </form>
                  </div>
                  <div class="panel-body">
                      <div id="bar_ii" style="height: 350px;"></div>
                  </div>
              </div>
         </div>环比end  -->
   </div>
  <div id="iii_container" class="hidden">
        <div id="iii_container_i" class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div id="chart_common_i" style="height: 390px;"></div>
                </div>
            </div>
        </div>
         <div id="iii_container_ii" class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div id="chart_common_ii" style="height: 390px;"></div>
                </div>
            </div>
        </div>
        <!--  
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div id="bar_ii" style="height: 390px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div id="bar_iii" style="height: 390px;"></div>
                </div>
            </div>
        </div>-->
	</div>
    </div>
    </div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dialog-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.serializeObject.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/MEcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/MDialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/customer.js"></script>
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
	
	function _search(){
		if(!checksearchtime()){
			alert("请填写查询时间!!");
			return;
		}
		
		if( $('#ii_container').hasClass("show")&& $('#iii_container').hasClass("show")){
            $('#ii_container').removeClass("show");
            $('#ii_container').addClass("hidden");
            $('#iii_container').removeClass("show");
            $('#iii_container').addClass("hidden");
	     }
		
		if($('#analysisType').val() != 'errorPage'){
			if( $('#iii_container_i').hasClass("col-md-6")){
	            $('#iii_container_i').removeClass("col-md-6");
	            $('#iii_container_i').addClass("col-md-12");
	            
	            $('#iii_container_ii').removeClass("show");
	            $('#iii_container_ii').addClass("hidden");
		     }
		}else{
			if( $('#iii_container_i').hasClass("col-md-12")){
	            $('#iii_container_i').removeClass("col-md-12");
	            $('#iii_container_i').addClass("col-md-6");
	            
	            $('#iii_container_ii').removeClass("hidden");
	            $('#iii_container_ii').addClass("show");
		     }
		}
		
		var params = $('#sform').serializeObject();
		 <!--nginx访问次数统计-->
		var bar_parent_chart = MECHART.initChart("bar_parent");
		MECHART.displayChart(bar_parent_chart,"${pageContext.request.contextPath}/nginx/geoIpStatistics",params);
		bar_parent_chart.on('click', function(params){
			 if( $('#ii_container').hasClass("hidden")&& $('#iii_container').hasClass("hidden")){
		            $('#ii_container').removeClass("hidden");
		            $('#ii_container').addClass("show");
		            $('#iii_container').removeClass("hidden");
		            $('#iii_container').addClass("show");
		        }
			 if( $('#ii_container').hasClass("show")&& $('#iii_container').hasClass("show")){
				  if(params.name=="平均值"){
					 return;
				  }
				  $('#ip').val(params.name);
				  $('#drillingTime').val(""); //clear 很重要
				  $('#ymdTime').val("");  //清空
				  $('#mTime').val("");  //清空
				  $('#sTime').val("");  //清空
				  var params = $('#sform').serializeObject();
				  
				  
				  <!----------------------------分时段  start----------------------------->
				  var bar_i_chart = MECHART.initChart("bar_i");
				  MECHART.displayChart(bar_i_chart,"${pageContext.request.contextPath}/nginx/dateHisgramStatistics",params);
				  bar_i_chart.on('click', function(params){
					  if(params.name=="平均值"){
							 return;
					  }
					  if((params.name).endsWith("日")){
						  $('#ymdTime').val(params.name);
					  }
					  if((params.name).endsWith("分")){
						  $('#mTime').val(params.name);
					  }
					  if((params.name).endsWith("秒")){
						  if($('#analysisType').val() == 'errorPage'){
							  $('#detailType').val("errorPage");
						  }else if($('#analysisType').val() == 'responseTime'){
							  $('#detailType').val("responseTime");
						  }else if($('#analysisType').val() == 'accessTimes'){
							  $('#detailType').val("accessTimes");
						  }
						  $('#searchWord').val("");
						  var time = $('#ymdTime').val() + $('#mTime').val() + params.name.substring(params.name.indexOf('分')+1,params.name.length);
						  time = time.replace("年","-").replace("月","-").replace("日"," ").replace("时",":").replace("分",":").replace("秒","");
						  $('#sTime').val(time);
						  var paramStr = $('#sform').serialize();
						  MDialog.show('${pageContext.request.contextPath}/nginx/sDetailInfo?'+paramStr);
						  return;
					  }
					  $('#drillingTime').val(params.name);
					  var params = $('#sform').serializeObject();
					  MECHART.displayChart(bar_i_chart,"${pageContext.request.contextPath}/nginx/dateHisgramStatistics",params);
				  });
				  <!----------------------------分时段  end----------------------------->
				  
				  
				  var chart_common_i = MECHART.initChart("chart_common_i");
				  if($('#analysisType').val() == 'errorPage'){
				  		<!--错误统统计-->
				  		MECHART.displayChart(chart_common_i,"${pageContext.request.contextPath}/nginx/wrongPageStatistics",params);
				  		chart_common_i.on('click', function(params){
				  			if(params.name=="平均值"){
				  				 //console.log(params);
								 return;
							 }
				  			 $('#url').val(params.name);
							 var params = $('#sform').serializeObject();
							 var chart_common_ii = MECHART.initChart("chart_common_ii");
				  			 MECHART.displayChart(chart_common_ii,"${pageContext.request.contextPath}/nginx/errorStatusStatistics",params);
				  			 chart_common_ii.on('click', function(params){
					  			if(params.name=="平均值"){
									 return;
								 }
					  			$('#detailType').val("errorPage");
								$('#searchWord').val(params.name+"|"+ $('#url').val());
							    var paramStr = $('#sform').serialize();
							    MDialog.show('${pageContext.request.contextPath}/nginx/detailInfo?'+paramStr);
				  			});
				  		});  
				  }else if($('#analysisType').val() == 'responseTime'){
					    <!--页面处理时间排名-->
					    MECHART.displayChart(chart_common_i,"${pageContext.request.contextPath}/nginx/pageProcessTimeRank",params);
					    chart_common_i.on('click', function(params){
							  $('#detailType').val("DealTime");
							  $('#searchWord').val(params.name);
							  var paramStr = $('#sform').serialize();
							  MDialog.show('${pageContext.request.contextPath}/nginx/detailInfo?'+paramStr);
						  });
				  }else if($('#analysisType').val() == 'accessTimes'){
					   if(params.name=="平均值"){
							 return;
						  }
					    <!--页面访问频次统计-->
					    MECHART.displayChart(chart_common_i,"${pageContext.request.contextPath}/nginx/pageAccessFrequentlyStatistics",params);
					    chart_common_i.on('click', function(params){
							  $('#detailType').val("Frequently");
							  $('#searchWord').val(params.name);
							  var paramStr = $('#sform').serialize();
							  MDialog.show('${pageContext.request.contextPath}/nginx/detailInfo?'+paramStr);
					  }); 
				  }
			 }
		});
	 }
	
	function _analysis() {
		$("#t_ip").val($("#ip").val());
		$('#t_analysisType').val($('#analysisType').val());
        var params = $('#tform').serializeObject();
        var bar_ii_chart = MECHART.initChart("bar_ii");
		MECHART.displayChart(bar_ii_chart,"${pageContext.request.contextPath}/tomcat/dateHisgramStatistics",params);
    }
</script>
</body>
</html>
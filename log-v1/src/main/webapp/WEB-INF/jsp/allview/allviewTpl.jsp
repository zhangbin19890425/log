
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>全视图监控</title>
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
    </style>
</head>
<body>
<!-- header -->
<jsp:include page="/common/nav-header.jsp"/>

<div class="container-fluid">
	<div style="margin: 10px 0px;">
        <form id="sform" action="" class="form-horizontal" role="form">
            <div class="col-md-4">
                <label class="col-md-3 control-label">开始时间：</label>
                <div class="col-md-9">
                    <input id="fromDate"  name="fromDate" class="form-control date" size="16" type="text" value="">
                </div>
            </div>

            <div class="col-md-4">
                <label class="control-label col-md-3">结束时间：</label>
                <div class="col-md-9">
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

	 <div  id="i_container" class="row">
	  <div class="col-md-2">
           <div class="list-group">
                <a href="${pageContext.request.contextPath}/tomcat/tomcatTpl" class="list-group-item">Tomcat管理</a>
                <a href="${pageContext.request.contextPath}/nginx/nginxTpl" class="list-group-item">Nginx管理</a>
                <a href="${pageContext.request.contextPath}/allview/allviewTpl" class="list-group-item active">全视图监控</a>
                 <a href="${pageContext.request.contextPath}/nginx/demo" class="list-group-item">demo页</a>
            </div>
        </div>
        <%--- 
         <div class="col-md-10">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="bar_parent" style="height: 350px;"></div>
                </div>
            </div>
        </div>
        --%>
        <div class="col-md-10">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="allview_bar_i" style="height:  200px;"></div>
                    <div id="allview_bar_ii" style="height: 200px;"></div>
                </div>
            </div>
        </div>
      
    </div>
    <div id="ii_container" class="row hidden">
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
    </div>
    <div id="iii_container" class="row hidden">
        <div class="col-md-4">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="line_i" style="height: 390px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="bar_ii" style="height: 390px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="bar_iii" style="height: 390px;"></div>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/option_allview.js"></script>
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
		var params = $('#sform').serializeObject();
		 <!--tomcat访问次数统计-->
		var bar_parent = MECHART.displayChart("bar_parent","${pageContext.request.contextPath}/tomcat/geoIpStatistics",params);
		bar_parent.on('click', function(params){
			 if( $('#ii_container').hasClass("hidden")){
		            $('#ii_container').removeClass("hidden");
		            $('#ii_container').addClass("show");
		        }
			  if( $('#ii_container').hasClass("show")){
				  $('#ip').val(params.name);
				  var params = $('#sform').serializeObject();
				  <!--响应时间统计-->
				  var bar_i = MECHART.displayChart("bar_i","${pageContext.request.contextPath}/tomcat/pageResponseTimeStatistics",params);
				  bar_i.on('click', function(params){
					  if( $('#iii_container').hasClass("hidden")){
				            $('#iii_container').removeClass("hidden");
				            $('#iii_container').addClass("show");
				        }
					  if($('#iii_container').hasClass("show")){
						  var params = $('#sform').serializeObject();
						  <!--404统计-->
						  var line_i = MECHART.displayChart("line_i","${pageContext.request.contextPath}/tomcat/404TimesStatistics",params);
						  <!--页面处理时间排名-->
						  var bar_i = MECHART.displayChart("bar_ii","${pageContext.request.contextPath}/tomcat/pageProcessTimeRank",params);
						  <!--页面访问频次统计-->
						  var bar_iii = MECHART.displayChart("bar_iii","${pageContext.request.contextPath}/tomcat/pageAccessFrequentlyStatistics",params);
					  }
				  });
			  }
		});
	}
	
	function toTomcat(){
		
	}
	var line_lidong_i = echarts.init(document.getElementById('line_lidong_i'));
    line_lidong_i.setOption(line_liandong_i_option);
    var line_lidong_ii = echarts.init(document.getElementById('line_lidong_ii'));
    line_lidong_ii.setOption(line_liandong_i_option_ii);
    

    echarts.connect([line_lidong_i, line_lidong_ii]);
</script>
</body>
</html>



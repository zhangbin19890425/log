<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>Ngnix</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5/css/bootstrap.min.css">
  	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/custom/nav-header.css">
	<style type="text/css">
       .modal{ overflow: auto !important;}
    </style>
</head>
<body>
<!-- header -->
<jsp:include page="/common/nav-header.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="ngnix_bar_i" style="height: 350px;"></div>
                </div>
            </div>
        </div>
    </div>
    <div id="ngnix_child_i" class="row hidden">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="ngnix_line_i" style="height: 390px;"></div>
                </div>
            </div>
        </div>
       <!--   <div class="col-lg-6">
            <div class="panel panel-default">
                
                <div class="panel-heading">Panel heading without title</div>
               
                <div class="panel-body">
                    <div id="ngnix_bar_ii" style="height: 390px;"></div>
                </div>
            </div> 
        </div>  -->
        <div class="col-lg-12">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="ngnix_pie_i" style="height: 390px;width:1287px"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.serializeObject.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/MEcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/echarts/theme/macarons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/option_ngnix.js"></script>
<script type="text/javascript">
	<!--nginx节点访问次数-->
    var ngnix_bar_i = MECHART.displayChart("ngnix_bar_i","${pageContext.request.contextPath}/nginx/nginxAccessTimes");
    ngnix_bar_i.on('click', function (params) {
    	 // 控制台打印数据的名称
        //console.log(params.name);
        if( $('#ngnix_child_i').hasClass("hidden")){
            $('#ngnix_child_i').removeClass("hidden");
            $('#ngnix_child_i').addClass("show");
        }
        if( $('#ngnix_child_i').hasClass("show")){
        	<!--每秒钟请求次数-->
        	var ngnix_line_i = MECHART.displayChart("ngnix_line_i","${pageContext.request.contextPath}/nginx/nginxSecondAccessTimes");
            
        	
  /*           var ngnix_bar_ii = echarts.init(document.getElementById('ngnix_bar_ii'));
            ngnix_bar_ii.setOption(ngnix_bar_ii_option); */
            
            <!--状态码统计-->
            var ngnix_pie_i = MECHART.displayChart("ngnix_pie_i","${pageContext.request.contextPath}/nginx/nginxStatusStatistics");
        }

    });
</script>
</body>
</html>
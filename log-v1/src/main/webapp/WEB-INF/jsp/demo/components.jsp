<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>Components</title>
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

<!--page content start-->
<div class="container-fluid">
    <div class="row">
        <div class="col-md-4">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="pie_i" style="height: 350px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="pie_ii" style="height: 350px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="pie_iii" style="height: 350px;"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="bar_i" style="height: 350px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="bar_ii" style="height: 350px;"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-4">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="line_i" style="height: 350px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-8">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="line_ii" style="height: 350px;"></div>
                </div>
            </div>
        </div>

    </div>
    <div class="row">
        <div class="col-md-8">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="line_iii" style="height: 350px;"></div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="gauge" style="height: 350px;"></div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <!--
                <div class="panel-heading">Panel heading without title</div>
                -->
                <div class="panel-body">
                    <div id="line_lidong_i" style="height:  200px;"></div>
                    <div id="line_lidong_ii" style="height: 200px;"></div>
                    <div id="line_lidong_iii" style="height: 200px;"></div>
                </div>
            </div>
        </div>
    </div>

</div><!--page content end-->

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.serializeObject.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/MEcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/option_component.js"></script>
<script type="text/javascript">
    var pie_i = echarts.init(document.getElementById('pie_i'));
    pie_i.setOption(pie_i_option);

    var pie_ii = echarts.init(document.getElementById('pie_ii'));
    pie_ii.setOption(pie_ii_option);

    var pie_iii = echarts.init(document.getElementById('pie_iii'));
    pie_iii.setOption(pie_iii_option);

    var bar_i = echarts.init(document.getElementById('bar_i'));
    bar_i.setOption(bar_i_option);

    var bar_ii = echarts.init(document.getElementById('bar_ii'));
    bar_ii.setOption(bar_ii_option);

    var line_i = echarts.init(document.getElementById('line_i'));
    line_i.setOption(line_i_option);

    var line_ii = echarts.init(document.getElementById('line_ii'));
    line_ii.setOption(line_ii_option);

    var line_iii = echarts.init(document.getElementById('line_iii'));
    line_iii.setOption(line_iii_option);

    var gauge = echarts.init(document.getElementById('gauge'));
    gauge.setOption(gauge_option);

    var line_lidong_i = echarts.init(document.getElementById('line_lidong_i'));
    line_lidong_i.setOption(line_liandong_i_option);
    var line_lidong_ii = echarts.init(document.getElementById('line_lidong_ii'));
    line_lidong_ii.setOption(line_liandong_i_option_ii);
    var line_lidong_iii = echarts.init(document.getElementById('line_lidong_iii'));
    line_lidong_iii.setOption(line_liandong_i_option_iii);

    echarts.connect([line_lidong_i, line_lidong_ii,line_lidong_iii]);
</script>
</body>
</html>
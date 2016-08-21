<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>折线图</title>
	<link href="${pageContext.request.contextPath}/css/codemirror.css" rel="stylesheet">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/codemirror.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/javascript.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/json2.js"></script>
	<script type="text/javascript"src="${pageContext.request.contextPath}/js/echarts.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/MEcharts.js"></script>
</head>
<body>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	<div id="main" style="height: 400px;"></div>
	<!-- ECharts单文件引入 -->
	<script type="text/javascript">
		// 基于准备好的dom，初始化echarts图表
		var myChart = MECHART.displayChart("main","${pageContext.request.contextPath}/demo/linedata");
	</script>

</body>
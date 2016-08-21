<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
<link href="<%=path%>/favicon.ico" type="image/x-icon" rel="shortcut icon" />
<title>error</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="description" content="error page">
</head>
<body>
	<br>
	<br>服务器内部错误
	<br>${error}
</body>
</html>
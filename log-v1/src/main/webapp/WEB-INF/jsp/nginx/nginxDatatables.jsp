<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${pageContext.request.contextPath}/assets/datatables/css/dataTables.bootstrap.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        .table th, .table td {
            text-align: center;
        }
        .pagee{
            margin-top: 10px;
        }
    </style>
</head>
<body>
<table id="tb" class="table table-bordered table-striped table-hover" cellspacing="0" width="100%">
    <thead>
    <tr>
    <th>body_bytes_sent</th>
    <th>http_referer</th>
    <th>http_true_client_ip</th>
    <th>http_user_agent</th>
    <th>remote_user</th>
    <th>request</th>
    <th>source_ip</th>
    <th>status</th>
    <th>tags</th>
    <th>timestamp</th>
    <th>upstream_addr></th>
    <th>upstream_response_time</th>
    <th>upstreap_request_time</th>
    <th>x_forword</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="tdata" items="${tdataList}">
	     <tr>
	        <td>${tdata.body_bytes_sent}</td>
	        <td>${tdata.http_referer}</td>
	        <td>${tdata.http_true_client_ip}</td>
	        <td>${tdata.http_user_agent}</td>
	        <td>${tdata.remote_user}</td>
	        <td>${tdata.request}</td>
	        <td>${tdata.source_ip}</td>
	        <td>${tdata.status}</td>
	        <td>${tdata.tags}</td>
	        <td>${tdata.timestamp}</td>
	        <td>${tdata.upstream_addr}</td>
	        <td>${tdata.upstream_response_time}</td>
	        <td>${tdata.upstreap_request_time}</td>
	        <td>${tdata.x_forword}</td>
	    </tr>
    </c:forEach>
    </tbody>
</table>

<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/datatables/js/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/datatables/js/dataTables.bootstrap.js"></script>
<script>
    var oTable = $("#tb").DataTable({
        //processing: true,
        dom: 't<"row pagee"<"col-md-2" l><"col-md-3" i><"col-md-7" p>>',
        ordering: true,
        paging: true,
        scrollX: true,
        scrollY: "370px",
        //scrollCollapse: true,
        language: {
            sProcessing: "处理中...",
            sLengthMenu: "显示 _MENU_ 项结果",
            sZeroRecords: "没有匹配结果",
            sInfo: "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
            sInfoEmpty: "显示第 0 至 0 项结果，共 0 项",
            sInfoFiltered: "(由 _MAX_ 项结果过滤)",
            sInfoPostFix: "",
            sSearch: "搜索:",
            sUrl: "",
            sEmptyTable: "表中数据为空",
            sLoadingRecords: "载入中...",
            sInfoThousands: ",",
            oPaginate: {
                sFirst: "首页",
                sPrevious: "上页",
                sNext: "下页",
                sLast: "末页"
            },
            oAria: {
                sSortAscending: ": 以升序排列此列",
                sSortDescending: ": 以降序排列此列"
            }
        }
    });
</script>
</body>
</html>
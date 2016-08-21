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
    <th>bytes_send</th>
    <th>bytes_send_excluding_http_header</th>
    <th><span style="width:80px; display:-moz-inline-box;display:inline-block;">host</span></th>
    <th>http_status</th>
    <th>identd_user</th>
    <th>identd_username</th>
    <th>local_port</th>
    <th>localhost_ip</th>
    <th>localhost_name</th>
    <th>server_name</th>
    <th>session_id</th>
    <th>visit_aggrement</th>
    <th>visit_contined_time_mill</th>
    <th>visit_contined_time_second</th>
    <th>visit_ip</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="tdata" items="${tdataList}">
	     <tr>
	         <td>${tdata.bytes_send}</td>
	         <td>${tdata.bytes_send_excluding_http_header}</td>
	         <td>${tdata.host}</td>
	         <td>${tdata.http_status}</td>
	         <td>${tdata.identd_user}</td>
	         <td>${tdata.identd_username}</td>
	         <td>${tdata.local_port}</td>
	         <td>${tdata.localhost_ip}</td>
	         <td>${tdata.localhost_name}</td>
	         <td>${tdata.server_name}</td>
	         <td>${tdata.session_id}</td>
	         <td>${tdata.visit_aggrement}</td>
	         <td>${tdata.visit_contined_time_mill}</td>
	         <td>${tdata.visit_contined_time_second}</td>
	         <td>${tdata.visit_ip}</td>
	    </tr>
    </c:forEach>
    </tbody>
</table>

<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/datatables/js/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/datatables/js/dataTables.bootstrap.js"></script>
<script>
    var oTable = $("#tb").dataTable({
        //processing: true,
        dom: 't<"row pagee"<"col-md-3" l><"col-md-4" i><"col-md-5" p>>',
        ordering: true,
        paging: true,
        scrollX: true,
        scrollY: "370px",
        scrollCollapse: true,
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
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${pageContext.request.contextPath}/assets/datatables/css/dataTables.bootstrap.min.css" rel="stylesheet"/>
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
<% 
	List thdataList = new ArrayList();
	thdataList.add("Name");
	thdataList.add("Position");
	thdataList.add("Office");
	thdataList.add("Age");
	thdataList.add("Start date");
	thdataList.add("Salary");
	request.setAttribute("thdataList",thdataList);
 %>
<body>
<table id="example" class="table table-bordered table-striped table-hover" cellspacing="0" width="100%">
    <thead>
    <tr>
     <c:forEach var="thdata" items="${thdataList}">
     	<th><c:out value="${thdata}"/></th>
     </c:forEach>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="tdata" items="${tdataList}">
	     <tr>
	        <td>${tdata.name}</td>
	        <td>${tdata.position}</td>
	        <td>${tdata.office}</td>
	        <td>${tdata.age}</td>
	        <td>${tdata.date}</td>
	        <td>${tdata.salary}</td>
	    </tr>
    </c:forEach>
    <tr>
        <td>Tiger Nixon</td>
        <td>System Architect</td>
        <td>Edinburgh</td>
        <td>61</td>
        <td>2011/04/25</td>
        <td>$320,800</td>
    </tr>
    </tbody>
</table>

<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/datatables/js/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/datatables/js/dataTables.bootstrap.min.js"></script>
<script>
    var oTable = $("#example").DataTable({
        //processing: true,
        dom: 't<"row pagee"<"col-md-2" l><"col-md-3" i><"col-md-7" p>>',
        ordering: true,
        paging: true,
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
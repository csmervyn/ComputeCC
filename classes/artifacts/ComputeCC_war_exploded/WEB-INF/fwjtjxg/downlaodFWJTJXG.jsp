<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="org.github.mervyn.utils.Result" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>下载非问卷调节的修改后的表格数据</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body class="container">
<h3>下载非问卷调节的修改后的表格数据</h3>
	<c:if test="${not empty error}">
		<div class="panel panel-danger">
			<div class="panel-heading">
			    <h3 class="panel-title">错误</h3>
			 </div>
			  <div class="panel-body">
			    ${error}
			  </div>
		</div>
	</c:if>
	<br>
	<a href="<%=basePath%>FWJTJXG/goSetParameterPage.do">继续进行非问卷调节的修改</a>
	<br>
	<a href="<%=basePath%>FWJTJXG/download.do">下载问卷调节后的数据表格</a>
	<br>
	<%-- <a href="<%=basePath%>fWJXGXSXGXin/alterFWJXinTable.do">下载非问卷相关系数修改后的表格数据(新)</a>
	<br>
	
	<a href="<%=basePath%>fWJXGXSXGXin/goFWJXGXSXGXinPage.do">继续进行非问卷数据相关系修改(新)</a>
	<br>
	<a href="<%=basePath%>fWJXGXSXGXin/goDYHUYZPage.do">对非问卷数据进行多元回归验证</a> --%>
	<br>
	<a href="<%=basePath%>">返回主页</a>
	<br>
	<c:if test="${not empty finalStr}">
		<div class="panel panel-success">
		  <div class="panel-heading">
		  	<h3 class="panel-title">多元回归方程</h3>
		  </div>
		  <div class="panel-body">
		   ${finalStr}
		  </div>
		</div>
	</c:if>
	<br>
	<div class="panel panel-primary">
		 <div class="panel-heading">
		 <h3 class="panel-title"> 因变量组、自变量组、调节变量组、交互项B相关系数表</h3>
		
		 </div>
		  <div class="panel-body">
			<table class="table table-bordered" border="1" style="border-collapse:collapse;">
				<thead>
					<tr>
						<td class="text-center">----------</td>
						<td class="text-center">因变量组</td>
						<td class="text-center">自变量组</td>
						<td class="text-center">调节变量组</td>
						<td class="text-center">交互项B</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="text-center">因变量组</td>
						<td></td>
						<c:set value="${resultList[0].getCorrelationCoefficientList()}" var="correlationCoefficientList"/>  
						<c:forEach items="${correlationCoefficientList}" var="correlationCoefficient" varStatus="CCStatusColumn">
							<td class="text-center">${correlationCoefficient}</td>
		         		</c:forEach>
					</tr>
					<tr>
						<td class="text-center">自变量组</td>
						<td></td>
						<td></td>
						<c:set value="${resultList[1].getCorrelationCoefficientList()}" var="correlationCoefficientList1"/>  
						<c:forEach items="${correlationCoefficientList1}" var="correlationCoefficient" varStatus="CCStatusColumn">
							<td class="text-center">${correlationCoefficient}</td>
		         		</c:forEach>
					</tr>
					<tr>
						<td class="text-center">调节变量组</td>
						<td></td>
						<td></td>
						<td></td>
						<c:set value="${resultList[2].getCorrelationCoefficientList()}" var="correlationCoefficientList2"/>  
						<c:forEach items="${correlationCoefficientList2}" var="correlationCoefficient" varStatus="CCStatusColumn">
							<td class="text-center">${correlationCoefficient}</td>
		         		</c:forEach>
					</tr>
					<tr>
						<td class="text-center">交互项B</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</tbody>	
			</table>
		  </div>
	</div>
	
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="<%=basePath%>bootstrap-3.3.7-dist/js/bootstrap.min.js">
	</script>
</body>
</html>
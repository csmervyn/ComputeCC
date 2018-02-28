<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>生成数列</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body class="container">
	<h3>数列生成</h3>
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
	
	<form action="<%=basePath%>createShuLie/create.do" method="post">
		<table class="table table-bordered">
			<tr >
				<th class="text-center">设定均值</th>
				<th class="text-center">标准差</th>
				<th class="text-center">个数</th>
				<th class="text-center">小数位</th>
				<th class="text-center">最小值</th>
				<th class="text-center">最大值</th>
			</tr>
			<c:forEach var="i" begin="1" end="${shuLieNum+0}" step="1">   
				<tr>
				  <td>
				  	<input type="text" name="mean${i}" ></input>
				  </td>
				  
				  <td class="active">
				  	<input type="text" name="sd${i}" ></input>
				  </td>
				  
				  <td class="success">
				  	<input type="text" name="num${i}" ></input>
				  </td>
				  
				  <td class="warning">
				  	<input type="text" name="decimalPlace${i}" value="2"></input>
				  </td>
				  
				  <td class="danger">
				  	<input type="text" name="min${i}"></input>
				  </td>
				  
				  <td class="info">
				  	<input type="text" name="max${i}"></input>
				  </td>
				</tr>
			</c:forEach>
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td class="text-right">
				 <input type="submit" value="提交"  class="btn btn-primary"></input>
				</td>
			</tr>
		</table>
		<input type="hidden" class="form-control"  name="shuLieNum" value="${shuLieNum}">	
	</form>
	
	<br>
	<a href="<%=basePath%>">返回主页</a>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="<%=basePath%>bootstrap-3.3.7-dist/js/bootstrap.min.js">
	</script>
</body>
</html>
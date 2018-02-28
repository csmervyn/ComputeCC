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
	<form class="form-inline" action="<%=basePath%>createShuLie/getShuLieNum.do" method="post">
	  <div class="form-group">
	    <label for="shuLieNum">生成数列的个数:</label>
	    <input type="text" class="form-control" id="shuLieNum" name="shuLieNum">
	  </div>
	  
	  <button type="submit" class="btn btn-default">确定</button>
	</form>
	<a href="<%=basePath%>">返回主页</a>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="<%=basePath%>bootstrap-3.3.7-dist/js/bootstrap.min.js">
	</script>
</body>
</html>
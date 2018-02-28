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
<title>数据抽取-五点量表</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body class="container">
	<h3>数据抽取-五点量表</h3>
	<form action="<%=basePath%>DEC/fiveSetColumnNum.do" method="post" class="form-horizontal">
		<div class="form-group">
		    <label for="groupNum" class="col-sm-2 control-label">组别个数：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="groupNum" name="groupNum">
		    </div>
		</div>
		<div class="form-group">
		    <label for="sampleSize" class="col-sm-2 control-label">样本量：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="sampleSize" name="sampleSize">
		    </div>
		</div>
		<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
	 			<button type="submit" class="btn btn-primary">提交</button>
		    </div>
		  </div>
	</form>
	<br>
	<a href="<%=basePath%>">返回主页</a>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="<%=basePath%>bootstrap-3.3.7-dist/js/bootstrap.min.js">
	</script>
</body>
</html>
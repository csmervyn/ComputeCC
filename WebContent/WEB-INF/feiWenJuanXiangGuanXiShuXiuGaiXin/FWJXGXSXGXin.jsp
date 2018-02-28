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
<title>非问卷数据相关系数修改-设定参数</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

</head>
<body class="container">
<h3>非问卷数据相关系数修改-设定参数</h3>
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
	<form action="<%=basePath%>fWJXGXSXGXin/compute.do" method="post" class="form-horizontal">	
		<div class="form-group">
		    <label for="former" class="col-sm-2 control-label">设定标准数组：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="former" name="former">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="latter" class="col-sm-2 control-label">修改数组：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="latter" name="latter">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="parameter" class="col-sm-2 control-label">相关系数值：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="parameter" name="parameter">
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
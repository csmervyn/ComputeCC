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
<title>非问卷调节修改-设定参数</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

</head>
<body class="container">
<h3>非问卷调节修改-设定参数</h3>
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
	<form action="<%=basePath%>FWJTJXG/compute.do" method="post" class="form-horizontal">
		  <div class="form-group">
		    <label for="dependentVar" class="col-sm-2 control-label">因变量组：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="dependentVar" name="dependentVar">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="independentVar" class="col-sm-2 control-label">自变量组：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="independentVar" name="independentVar">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="adjustVar" class="col-sm-2 control-label">调节变量组：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="adjustVar" name="adjustVar">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="correlationCoefficient" class="col-sm-2 control-label">交互项相关系数：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="correlationCoefficient" name="correlationCoefficient">
		    </div>
		  </div>
		 <div class="form-group">
		    <label for="percent" class="col-sm-2 control-label">每次修改量：</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="percent" name="percent" value="0.01">
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
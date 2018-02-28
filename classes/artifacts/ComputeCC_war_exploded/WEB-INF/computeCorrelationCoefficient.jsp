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
<title>计算相关系数</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body class="container">
	<h3>信效度分析的参数设置</h3>
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
	<form action="<%=basePath%>CCCompute/compute.do" method="post" class="form-horizontal">
		
		<%-- 修改百分比：<input type="text" name="PERCENT" value="${compute.getPERCENT()}"></input>
		<br> --%>
		<div class="form-group">
		    <label for="PERCENT" class="col-sm-2 control-label">修改百分比:</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="PERCENT"  name="PERCENT" value="${compute.getPERCENT()}">
		    </div>
	  	</div>
		<%-- 参数1：<input type="text" name="J2PARAMETER" value="${compute.getJ2PARAMETER()}"></input>
		<br> --%>
		<div class="form-group">
		    <label for="J2PARAMETER" class="col-sm-2 control-label">参数1:</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="J2PARAMETER"  name="J2PARAMETER" value="${compute.getJ2PARAMETER()}">
		    </div>
	  	</div>
		<%-- 参数2：<input type="text" name="J3PARAMETER" value="${compute.getJ3PARAMETER()}"></input>
		<br> --%>
		<div class="form-group">
		    <label for="J3PARAMETER" class="col-sm-2 control-label">参数2:</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="J3PARAMETER"  name="J3PARAMETER" value="${compute.getJ3PARAMETER()}">
		    </div>
	  	</div>
		<%-- 参数3：<input type="text" name="J4PARAMETER" value="${compute.getJ4PARAMETER()}"></input>
		<br> --%>
		<div class="form-group">
			<label for="NEW_J4PARAMETER" class="col-sm-2 control-label">参数3:</label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="NEW_J4PARAMETER"  name="NEW_J4PARAMETER" value="${compute.getNEW_J4PARAMETER()}">
			</div>
		</div>

		<div class="form-group">
			<label for="NEW_J5PARAMETER" class="col-sm-2 control-label">参数4:</label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="NEW_J5PARAMETER"  name="NEW_J5PARAMETER" value="${compute.getNEW_J5PARAMETER()}">
			</div>
		</div>



		<div class="form-group">
		    <label for="J4PARAMETER" class="col-sm-2 control-label">参数5:</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="J4PARAMETER"  name="J4PARAMETER" value="${compute.getJ4PARAMETER()}">
		    </div>
	  	</div>
		<%-- 参数4：<input type="text" name="PARAMETER_OF_THREESTEP" value="${compute.getPARAMETER_OF_THREESTEP()}"></input>
		<br> --%>
		<div class="form-group">
		    <label for="PARAMETER_OF_THREESTEP" class="col-sm-2 control-label">参数6:</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="PARAMETER_OF_THREESTEP"  name="PARAMETER_OF_THREESTEP" value="${compute.getPARAMETER_OF_THREESTEP()}">
		    </div>
	  	</div>
		<%-- 参数5：<input type="text" name="PARAMETER_OF_FOUR_ONE_STEP" value="${compute.getPARAMETER_OF_FOUR_ONE_STEP()}"></input>
		<br> --%>
		<div class="form-group">
		    <label for="PARAMETER_OF_FOUR_ONE_STEP" class="col-sm-2 control-label">参数7:</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="PARAMETER_OF_FOUR_ONE_STEP"  name="PARAMETER_OF_FOUR_ONE_STEP" value="${compute.getPARAMETER_OF_FOUR_ONE_STEP()}">
		    </div>
	  	</div>
		<!-- <input type="submit" value="提交"></input> -->
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
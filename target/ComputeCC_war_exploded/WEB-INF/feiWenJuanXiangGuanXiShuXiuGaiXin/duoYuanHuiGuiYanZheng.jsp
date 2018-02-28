<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>多元回归验证</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body class="container">
<br>
	<h3>非问卷数据多元回归验证</h3>
	<form action="<%=basePath%>fWJXGXSXGXin/DYHGYZ.do" method="post" class="form-horizontal">
	<div class=" col-md-8">
	  <table class="table table-bordered" >
			<tr>
				<th class="text-center col-md-1">------</th>
				<th class="text-center  col-md-2">因变量</th>
				<th class="text-center  col-md-2">自变量</th>
			</tr>
			<c:forEach var="i" begin="1" end="${duoYuanNum+0}" step="1">   
				<tr>
				  <td class="text-center">
				  	<label >${i}:</label>
				  </td>
				  
				  <td class=" text-center active">
				  	<input type="text" id="dependentVar${i-1}" name="dependentVar${i-1}">
				  </td>
				  
				  <td class="text-center success">
				  	<input type="text" class="form-control" id="independentVar${i-1}" name="independentVar${i-1}">
				  </td>
				</tr>
			</c:forEach>
			<tr>
				<td></td>
				<td></td>

				<td class="text-right">
				 <input type="submit" value="提交"  class="btn btn-primary"></input>
				</td>
			</tr>
		</table>
		</div>
	</form>
	<%-- <form action="<%=basePath%>fWJXGXSXGXin/DYHGYZ.do" method="post" class="form-horizontal">
	  <div class="form-group">
	    <label for="dependentVar" class="col-sm-2 control-label">因变量</label>
	    <div class="col-sm-3">
	      <input type="text" class="form-control" id="dependentVar" name="dependentVar">
	    </div>
	  </div>
	  <div class="form-group">
	    <label for="independentVar" class="col-sm-2 control-label">自变量</label>
	    <div class="col-sm-3">
	      <input type="text" class="form-control" id="independentVar" name="independentVar">
	    </div>
	  </div>
	  
	  <div class="form-group">
	    <div class="col-sm-offset-2 col-sm-10">
	      <button type="submit" class="btn btn-primary">提交</button>
	    </div>
	  </div>
	</form> --%>
	<div class=" col-md-8">
	<a href="<%=basePath%>">返回主页</a>
	</div>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="<%=basePath%>bootstrap-3.3.7-dist/js/bootstrap.min.js">
	</script>
</body>
</html>
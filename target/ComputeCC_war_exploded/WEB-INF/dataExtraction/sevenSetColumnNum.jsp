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
<title>数据抽取-设置每组的列数</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body class="container">
	<h3>数据抽取-设置每组的列数</h3>
	<form action="<%=basePath%>DEC/sevenDataExtraction.do" method="post" class="form-horizontal">
		<c:set var="groupNum" value="${groupNum}"/>
		<c:forEach  begin="0" end="${groupNum-1}" varStatus="varStatus">
			<c:choose>
					<c:when test="${varStatus.index == 0}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第A组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
    				<c:when test="${varStatus.index == 1}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第B组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 2}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第C组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 3}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第D组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 4}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第E组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 5}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第F组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index == 6}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第G组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 7}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第H组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 8}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第I组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 9}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第J组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 10}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第K组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index == 11}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第L组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index == 12}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第M组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 13}">
						<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第N组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 14}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第O组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 15}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第P组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 16}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第Q组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 17}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第R组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 18}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第S组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 19}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第S组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 20}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第T组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 21}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第U组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 22}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第V组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 23}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第W组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:when test="${varStatus.index== 24}">
						<div class="form-group">
							<label for="column${varStatus.index}" class="col-sm-2 control-label">第X组：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
							</div>
						</div>
					</c:when>
					<c:otherwise>
	     				<div class="form-group">
						    <label for="column${varStatus.index}" class="col-sm-2 control-label">第Z组：</label>
						    <div class="col-sm-3">
						      <input type="text" class="form-control" id="column${varStatus.index}" name="column${varStatus.index}">
						    </div>
						</div>
	     			</c:otherwise>
			</c:choose> 
		</c:forEach>
		
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
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
<title>下载修改后的excel表格和相关系数excel表格</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body  class="container">
<h3>下载修改后的excel表格</h3>
	<a href="<%=basePath%>downloadFile/WJXDXDTCTable.do">剔除后的excel表格</a>
	<br>
	<a href="<%=basePath%>wenJuanXinXiaoDuTuiChu.do">继续进行问卷信度效度数据剔除</a>
	<br>
	<a href="<%=basePath%>">返回主页</a>
	<br>
	<table  border="1" style="border-collapse:collapse;">
		<thead>
			<tr>
				<td>----------</td>
			    <c:forEach var="result" items="${resultList}" varStatus="varStatusColumn"> 
     				<c:choose>
     					<c:when test="${varStatusColumn.index == 0}">
     						<td>第A组</td>
     					</c:when>
					    <c:when test="${varStatusColumn.index == 1}">
     						<td>第B组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 2}">
     						<td>第C组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 3}">
     						<td>第D组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 4}">
     						<td>第E组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 5}">
     						<td>第F组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index == 6}">
     						<td>第G组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 7}">
     						<td>第H组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 8}">
     						<td>第I组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 9}">
     						<td>第J组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 10}">
     						<td>第K组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index == 11}">
     						<td>第L组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index == 12}">
     						<td>第M组</td>
     					</c:when>
     					<c:when test="${varStatusColumn.index== 13}">
     						<td>第N组</td>
     					</c:when>
					</c:choose>
     				<c:set value="0" var="groupNum"/> 
				</c:forEach> 
				<td>第
				<c:choose>
     					<c:when test="${resultList.size()== 0}">
     						A
     					</c:when>
					    <c:when test="${resultList.size()== 1}">
     						B
     					</c:when>
     					<c:when test="${resultList.size()== 2}">
     						C
     					</c:when>
     					<c:when test="${resultList.size()== 3}">
     						D
     					</c:when>
     					<c:when test="${resultList.size()== 4}">
     						E
     					</c:when>
     					<c:when test="${resultList.size()== 5}">
     						F
     					</c:when>
     					<c:when test="${resultList.size()== 6}">
     						G
     					</c:when>
     					<c:when test="${resultList.size()== 7}">
     						H
     					</c:when>
     					<c:when test="${resultList.size()== 8}">
     						I
     					</c:when>
     					<c:when test="${resultList.size()== 9}">
     						J
     					</c:when>
     					<c:when test="${resultList.size()== 10}">
     						K
     					</c:when>
     					<c:when test="${resultList.size()== 11}">
     						L
     					</c:when>
     					<c:when test="${resultList.size()== 12}">
     						M
     					</c:when>
     					<c:when test="${resultList.size()== 13}">
     						N
     					</c:when>
     					<c:otherwise>
     						O
     					</c:otherwise>
					</c:choose>
				
				组
				</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="result" items="${resultList}" varStatus="varLine">
				<tr>
					<td>第
					<c:choose>
     					<c:when test="${varLine.index== 0}">
     						A
     					</c:when>
					    <c:when test="${varLine.index== 1}">
     						B
     					</c:when>
     					<c:when test="${varLine.index== 2}">
     						C
     					</c:when>
     					<c:when test="${varLine.index== 3}">
     						D
     					</c:when>
     					<c:when test="${varLine.index== 4}">
     						E
     					</c:when>
     					<c:when test="${varLine.index== 5}">
     						F
     					</c:when>
     					<c:when test="${varLine.index== 6}">
     						G
     					</c:when>
     					<c:when test="${varLine.index== 7}">
     						H
     					</c:when>
     					<c:when test="${varLine.index== 8}">
     						I
     					</c:when>
     					<c:when test="${varLine.index== 9}">
     						J
     					</c:when>
     					<c:when test="${varLine.index== 10}">
     						K
     					</c:when>
     					<c:when test="${varLine.index== 11}">
     						L
     					</c:when>
     					<c:when test="${varLine.index== 12}">
     						M
     					</c:when>
     					<c:when test="${varLine.index== 13}">
     						N
     					</c:when>
     					<c:otherwise>
     						O
     					</c:otherwise>
					</c:choose>
					
					组</td>
					<c:if test="${varLine.index == 0}">
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 1}">
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 2}">
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 3}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 4}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 5}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 6}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 7}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 8}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 9}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 10}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 11}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 12}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 13}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${varLine.index == 14}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					
					
					
					<c:set value="${result.getCorrelationCoefficientList()}" var="correlationCoefficientList"/>  
					<c:forEach items="${correlationCoefficientList}" var="correlationCoefficient" varStatus="CCStatusColumn">
						<td>${correlationCoefficient}</td>
           			</c:forEach>
				</tr>  
     				
			</c:forEach> 
		</tbody>
		<tfoot>
			<tr>
					<td>第
				<c:choose>
     					<c:when test="${resultList.size()== 0}">
     						A
     					</c:when>
					    <c:when test="${resultList.size()== 1}">
     						B
     					</c:when>
     					<c:when test="${resultList.size()== 2}">
     						C
     					</c:when>
     					<c:when test="${resultList.size()== 3}">
     						D
     					</c:when>
     					<c:when test="${resultList.size()== 4}">
     						E
     					</c:when>
     					<c:when test="${resultList.size()== 5}">
     						F
     					</c:when>
     					<c:when test="${resultList.size()== 6}">
     						G
     					</c:when>
     					<c:when test="${resultList.size()== 7}">
     						H
     					</c:when>
     					<c:when test="${resultList.size()== 8}">
     						I
     					</c:when>
     					<c:when test="${resultList.size()== 9}">
     						J
     					</c:when>
     					<c:when test="${resultList.size()== 10}">
     						K
     					</c:when>
     					<c:when test="${resultList.size()== 11}">
     						L
     					</c:when>
     					<c:when test="${resultList.size()== 12}">
     						M
     					</c:when>
     					<c:when test="${resultList.size()== 13}">
     						N
     					</c:when>
     					<c:otherwise>
     						O
     					</c:otherwise>
					</c:choose>
				
				组
				</td>
					<c:if test="${resultList.size() == 0}">
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 1}">
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 2}">
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 3}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 4}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 5}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 6}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 7}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 8}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 9}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 10}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 11}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 12}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 13}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
					<c:if test="${resultList.size() == 14}">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</c:if>
			</tr>
		</tfoot>
	</table>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="<%=basePath%>bootstrap-3.3.7-dist/js/bootstrap.min.js">
	</script>
</body>
</html>
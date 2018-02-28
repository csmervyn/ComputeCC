<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主页</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->

<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body class="container">
<h3>数据校正系统主页</h3>
	<a href="<%=basePath%>goUploadPage.do">信效度分析</a>
	<br>
	<a href="<%=basePath%>goUploadPage1.do">问卷相关</a>
	<br>
	<a href="<%=basePath%>WJTJ/goUploadWJTJPage.do">问卷调节</a>
	<br>
	<a href="<%=basePath%>goUploadFWJXGXSXG.do">非问卷相关1</a>
	<br>
	<a href="<%=basePath%>fWJXGXSXGXin/goUploadPage.do">非问卷相关2</a>
	<br>
	<a href="<%=basePath%>FWJTJXG/goFWJTJXGUploadPage.do">非问卷调节</a>
	<br>
	<a href="<%=basePath%>createShuLie/goCreate.do">数列生成</a>
	<br>
	<a href="<%=basePath%>DEC/goSelect.do">问卷抽取</a>
	<br>
	<a href="<%=basePath%>DEC1/goSingleGroup.do">人口学抽取</a>
	<br>
	<a href="<%=basePath%>goUploadWJXDXDTC.do">信度效度剔除</a>
	<br>
	<a href="<%=basePath%>goUploadXGXSBDTC.do">相关剔除</a>
	<br>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="<%=basePath%>bootstrap-3.3.7-dist/js/bootstrap.min.js">
	</script>
</body>
</html>
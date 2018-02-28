<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据抽取-设置每组中的每个值的百分比</title>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link href="<%=basePath%>bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<script>
    var sum = function(orignalId) {
		//var groupNumElement =document.getElementById("hidden_groupNum");
        //var groupNum = parseInt(groupNumElement.value);
		var sum = 0;
		var last_At = orignalId.id.lastIndexOf("_");
		var first_At = orignalId.id.indexOf("_");
		var subId = orignalId.id.substring(0,last_At);
		var max_id = orignalId.id.substring(first_At,last_At);
        var max_Element = document.getElementById("max" + max_id);
        var lineMaxValue = parseInt(max_Element.value);
		for(var i = 0; i < lineMaxValue; i++){
		    var id = subId + "_" + i;
		    if("" != document.getElementById(id).value){
                var lineItemValue = parseFloat(document.getElementById(id).value);
                sum += lineItemValue;
            }
		}
		document.getElementById("sum" + max_id).innerHTML = parseFloat(sum).toFixed(2);

    }
</script>
</head>
<body class="container">
	<h3>数据抽取-设置每组中的每个值的百分比</h3>
	<form action="<%=basePath%>DEC1/getData.do" method="post" class="form-horizontal">
		<table class="table table-bordered row">
			<tr>
				<th class="text-center col-md-1">单组号\值</th>
				<c:forEach  begin="1" end="${maxElement}" varStatus="varStatus">
					<th class="text-center">${varStatus.index}</th>
				</c:forEach>
				<th class="text-center col-md-1">每行总和</th>
			</tr>
			
			<!-- 隐藏域-->
			<input id="hidden_groupNum" value="${groupNum}" type="hidden"/>
			<c:forEach  begin="0"  end="${groupNum-1}" varStatus="varStatus">
				<c:choose>
						<c:when test="${varStatus.index == 0}">
							<tr>
								<td class="text-center col-md-1">
									<label >第A组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_0" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_0">0</label>
								</td>
							</tr>
						</c:when>
	    				<c:when test="${varStatus.index == 1}">
							<tr>
								<td class="text-center col-md-1">
									<label >第B组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_1" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_1">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 2}">
							<tr>
								<td class="text-center col-md-1">
									<label >第C组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})">
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_2" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_2">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 3}">
							<tr>
								<td class="text-center col-md-1">
									<label >第D组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})">
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_3" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_3">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 4}">
							<tr>
								<td class="text-center col-md-1">
									<label >第E组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})">
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_4" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_4">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 5}">
							<tr>
								<td class="text-center col-md-1">
									<label >第F组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_5" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_5">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index == 6}">
							<tr>
								<td class="text-center col-md-1">
									<label >第G组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})">
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_6" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_6">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 7}">
							<tr>
								<td class="text-center col-md-1">
									<label >第H组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_7" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_7">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 8}">
							<tr>
								<td class="text-center col-md-1">
									<label >第I组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})">
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_8" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_8">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 9}">
							<tr>
								<td class="text-center col-md-1">
									<label >第J组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_9" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_9">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 10}">
							<tr>
								<td class="text-center col-md-1">
									<label >第K组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_10" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_10">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index == 11}">
							<tr>
								<td class="text-center col-md-1">
									<label >第L组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_11" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_11">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index == 12}">
							<tr>
								<td class="text-center col-md-1">
									<label >第M组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_12" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_12">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 13}">
							<tr>
								<td class="text-center col-md-1">
									<label >第N组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_13" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_13">0</label>
								</td>
							</tr>
						</c:when>
					    <c:when test="${varStatus.index== 14}">
							<tr>
								<td class="text-center col-md-1">
									<label >第O组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_14" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_14">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 15}">
							<tr>
								<td class="text-center col-md-1">
									<label >第P组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_15" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_15">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 16}">
							<tr>
								<td class="text-center col-md-1">
									<label >第Q组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_16" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_16">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 17}">
							<tr>
								<td class="text-center col-md-1">
									<label >第R组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_17" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_17">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 18}">
							<tr>
								<td class="text-center col-md-1">
									<label >第S组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_18" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_18">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 19}">
							<tr>
								<td class="text-center col-md-1">
									<label >第T组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_19" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_19">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 20}">
							<tr>
								<td class="text-center col-md-1">
									<label >第U组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_20" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_20">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 21}">
							<tr>
								<td class="text-center col-md-1">
									<label >第V组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_21" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_21">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 22}">
							<tr>
								<td class="text-center col-md-1">
									<label >第W组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_22" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_22">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 23}">
							<tr>
								<td class="text-center col-md-1">
									<label >第X组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_23" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_23">0</label>
								</td>
							</tr>
						</c:when>
						<c:when test="${varStatus.index== 24}">
							<tr>
								<td class="text-center col-md-1">
									<label >第Y组：</label>
								</td>

								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
										<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
											<td class="text-center col-md-1">
												<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
											</td>
										</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_24" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_24">0</label>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
		     				<tr>
								<td class="text-center col-md-1">
									<label >第Z组：</label>
								</td>
								
								<c:forEach  begin="0" var="max" items="${maxList}"  varStatus="maxStatus">
									<c:if test="${maxStatus.index == varStatus.index}">
									  	<c:forEach  begin="0"  end="${max-1}" varStatus="tempMaxStatus">
										  <td class="text-center col-md-1">
											<input type="text" class="form-control" id="pecent_${varStatus.index}_${tempMaxStatus.index}" name="pecent_${varStatus.index}_${tempMaxStatus.index}" onblur="sum(pecent_${varStatus.index}_${tempMaxStatus.index})" >
										  </td>
									  	</c:forEach>
										<c:forEach  begin="${max}"  end="${maxElement-1}" varStatus="tempBlank">
											<td class="text-center col-md-1">
											</td>
										</c:forEach>
										<!-- 隐藏域-->
										<input id="max_25" value="${max}" type="hidden"/>
									</c:if>
								</c:forEach>
								<td class="text-center col-md-1">
									<label id="sum_25">0</label>
								</td>
							</tr>
		     			</c:otherwise>
				</c:choose> 
			</c:forEach>
			<tr>
				<c:forEach  begin="1" end="${maxElement+1}" varStatus="varStatus">
					<td></td>
				</c:forEach>
				<td class="text-right">
				 <input type="submit" value="提交"  class="btn btn-primary"></input>
				</td>
			</tr>
		</table>

	</form>
	<br>
	<a href="<%=basePath%>">返回主页</a>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="<%=basePath%>bootstrap-3.3.7-dist/js/bootstrap.min.js">
	</script>
</body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<h1>
<%
Object msg=request.getAttribute("msg");
if(null==msg){
	%>
	
	<%
}else{
	if("打卡成功".equals(msg.toString())){
		%>
		<font color=red>我的信息 </font><br>
		姓名：${userTrueName} <br>
 		工号：${userNum} <br>
 		当前所在位置：${address} <br>
		<%
	}else{
		%>
		${msg} 
		<%
	}
}
%>
 </h1>
 
 
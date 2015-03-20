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
		<font color=red>${msg} </font><br>
		姓名：${userTrueName} <br>
 		地址：${address} <br>
		<%
	}else{
		%>
		${msg} 
		<%
	}
}
%>
 </h1>
 
 
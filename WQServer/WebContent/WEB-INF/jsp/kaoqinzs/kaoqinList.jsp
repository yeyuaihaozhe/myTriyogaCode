<%@ page language="java" import="java.util.*,com.comba.util.CombaCommonUtil" pageEncoding="UTF-8"%>
<h1>
<%
List<Map<String, Object>> list=(List<Map<String, Object>>)request.getAttribute("data");
for(Map<String, Object> map:list){
	String createTime=CombaCommonUtil.format(CombaCommonUtil.getValueTimestamp(map, "createTime"), "yyyy-MM-dd HH-mm-ss");
	String userTrueName=CombaCommonUtil.getValueString(map, "userTrueName");
	String address=CombaCommonUtil.getValueString(map, "address");
	%>
	<%=userTrueName %>
	<font color=red><%=createTime %></font>
	<%=address %><br>
	<%
}
%>
 </h1>
 
 
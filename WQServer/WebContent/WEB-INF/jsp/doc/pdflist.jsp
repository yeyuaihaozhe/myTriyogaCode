<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String filename=request.getParameter("filename");
filename=new String(filename.getBytes("ISO-8859-1"),"utf-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=filename %></title>
</head>
<body>
<c:forEach begin="1" end="${pagecount}" var="i">
<img style="width: 100%;height: auto;" src="/weaver/test?path=${path}&page=${i}">
</c:forEach>

</body>
</html>
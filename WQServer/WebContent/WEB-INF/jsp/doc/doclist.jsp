<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>标题内容附件链接</title>
</head>
<body>
<table border="1px">
<c:forEach begin="0" end="2" var="i">
${i }

</c:forEach>
<tr><td>标题</td><td>内容</td><td>附件链接</td></tr>
<tr><td>${title0[0].DOCSUBJECT }</td><td>${content0[0].DOCCONTENT}</td><td><a href="download?filepath='+${link0[0].FILEREALPATH}">${link0[0].FILEREALPATH}</a></td></tr>
<tr><td>${title1[0].DOCSUBJECT }</td><td>${content1[0].DOCCONTENT}</td><td><a href="download?filepath='+${link1[0].FILEREALPATH}">${link1[0].FILEREALPATH}</a></td></tr>
<tr><td>${title2[0].DOCSUBJECT }</td><td>${content2[0].DOCCONTENT}</td><td><a href="download?filepath='+${link2[0].FILEREALPATH}">${link2[0].FILEREALPATH}</a></td></tr>
</table>
</body>
</html>
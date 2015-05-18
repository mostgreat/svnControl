<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/docType.jspf"%>
<html>
<head>
<title>Login Page</title>
<%@ include file="/common/include/head.jspf"%>
<%
/* =================================================================
 * 
 * 작성일 : 2015. 3. 4.
 *  
 * 작성자 : mostgreat2
 * 
 * 상세설명 : 
 *   
 * =================================================================
 * 수정일         작성자             내용     
 * -----------------------------------------------------------------------
 * 
 * =================================================================
 */ 
%>

</head>
<body>
page3
<form action="${flowExecutionUrl}&_eventId=gotoNextPage" method="post">
<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
<input type="submit" value="Next Page">
</form>
</body>
</html>
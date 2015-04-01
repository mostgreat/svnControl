<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ include file="/common/include/docType.jspf"%>
<html>
<head>
<title>WUA Error Page</title>
<%@ include file="/common/include/head.jspf"%>

</head>

<body>

	요청 처리 과정에서 에러가 발생하였습니다.<br>
	<br>
	에러 타입 : <%= exception.getClass().getName() %><br>
	에러 메세지 : <%= exception.getMessage() %>
	
</body>

</html>
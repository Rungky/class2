<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>회원 수정</h1>
	<form action="/pro17/member/modMember.do">
		empno: ${ memInfo.empno }<br>
		ename: <input type="text" name="ename" value="${ memInfo.ename }"><br>
		sal: <input type="text" name="sal" value="${ memInfo.sal }"><br>
		<input type="hidden" name="empno" value="${ memInfo.empno }">
		<input type="submit" value="수정">
	</form>
</body>
</html>

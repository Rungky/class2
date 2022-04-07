<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Login</title>
</head>
<body>
	<h1>로그인 페이지</h1>
	<br>
	<form method="post" action="/pro17/login/loginCheck.do">
		empno : <input type="text" name="empno">
		<input type="submit" value="로그인">
	</form>
</body>
</html>
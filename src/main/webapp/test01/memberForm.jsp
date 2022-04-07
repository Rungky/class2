<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>회원 가입</h1>
	<form action="/pro17/member/addMember.do">
		empno: <input type="text" name="empno"><br>
		ename: <input type="text" name="ename"><br>
		sal: <input type="text" name="sal"><br>
		<input type="submit" value="가입">
	</form>
</body>
</html>
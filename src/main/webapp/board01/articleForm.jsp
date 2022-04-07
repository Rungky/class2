<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath" value="${ pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
	function goList(){
		location.href="${contextPath}/board/listArticles.do";
	}
</script>
</head>
<body>
<h1>새글쓰기</h1>
<form name="articleForm" method="post" action="${contextPath}/board/addArticle.do">
	제목 : <input type="text" name="title">
	<br>
	내용<br>
	<textarea name="content" rows="10" cols="65" maxlength="4000"></textarea>
	<br>
	<br>
	<input type="submit" value="글쓰기">
	<input type="button" value="목록보기" onclick="goList();">
</form>
</body>
</html>
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
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script>
	function fn_process(){
		
		var option = {
			"type": "get",
			dataType: "text",
			url: "/pro17/board/plusLikeCount.do",  //주고
			data: {
				articleNO: ${ article.articleNO }
			},
			success: function (data1, textStatus){
				console.log("data1 : " + data1);   //받음
				
				var data = JSON.parse(data1);
				$("#like_count").text(data.like_count);
			}
		}
		
		$.ajax( option );
	}
	
	$(document).ready(function() {
		$("#like").off("click").on("click", function(){
			fn_process();
		});
		
		$("#like_refresh").off("click").on("click", function(){
			location.href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}&plusLike=true";
		});
	})
	
	function goList(){
		location.href="${contextPath}/board/listArticles.do";
	}
	
</script>
</head>
<body>
	작성자 : ${ article.ename }
	<br>
	조회수 : ${ article.view_count }
	<br>
<form name="articleForm" method="post" action="${contextPath }/board/modArticle.do"> 	
	제목 : <input name="title" type="text" value="${ article.title }">
	<br>
	내용<br>
	<textarea name="content" rows="10" cols="65" maxlength="4000">${ article.content}</textarea>
	<br><br>
	<input type="hidden" name="articleNO" value="${article.articleNO }">
	<input type="submit" value="등록하기">
	<input type="button" value="취소하기" onclick="history.back(-1);">
</form>
	
</body>
</html>
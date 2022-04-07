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
	
	function goModify(){
		location.href="${contextPath}/board/modifyArticle.do?articleNO=${article.articleNO}";
	}
	
	function goRemove(){
		var isDel = window.confirm("정말 삭제하시겠습니까?");
		if(isDel){
			location.href="${contextPath}/board/removeArticle.do?articleNO=${article.articleNO}";
		}
	}
	
	function goReply(){
		location.href="${contextPath}/board/replyForm.do?parentNO=${article.articleNO}";
	}
	
</script>
</head>
<body>
	작성자 : ${ article.ename }
	<br>
	조회수 : ${ article.view_count }
	<br>
	제목 : ${ article.title }
	<br>
	내용<br>
	<div style="border:1px green solid; padding:10px">
		${ article.content}
	</div>
	<br>
	<br>
	<input type="button" value="좋아요" id="like"><span id="like_count">${ article.like_count }</span><br><input type="button" value="좋아요(새로고침)" id="like_refresh">${ article.like_count }
	<br><br>
	<input type="button" value="수정하기" onclick="goModify();">
	<input type="button" value="삭제하기" onclick="goRemove();">
	<input type="button" value="리스트로 돌아가기" onclick="goList();">
	<input type="button" value="답글쓰기" onclick="goReply();">
	
</body>
</html>
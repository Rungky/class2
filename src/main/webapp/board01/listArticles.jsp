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
	<c:if test=" ${empty empno}">
		location.href="/pro17/login/login.do";
	</c:if>
	$(document).ready(function() {
		$("#btn_search").off("click").on("click", function(){
			location.href="${contextPath}/board/search.do?keyword="+ $("#keyword").val();
		});
	
	})

</script>
</head>
<body>
	<table align="center" border="1" width="80%">
		<thead>
			<tr>
				<td>글번호</td>
				<td>작성자</td>
				<td>제목</td>
				<td>작성일</td>
				<td>조회수</td>
			</tr>
		</thead>
		
		<tbody>
		<c:choose>
			<c:when test="${ empty articlesList }">
				<tr height="10">
					<td colspan="4">등록된 글이 없습니다</td>
				</tr>
			</c:when>
			<c:when test="${! empty articlesList }">
				<c:forEach var="article" items="${ articlesList }" varStatus="articleNum">
					<tr align="center">
						<td width="5%">${articleNum.count }</td>
						<td width="10%">${article.ename }</td>
						<td align="left" width="35%">
							<span style="padding-right:30px;"></span>
							<c:choose>
								<c:when test="${article.level > 1 }">
									<c:forEach begin="1" end="${article.level }" step="1">
										<span style="padding-right:20px;"></span>
									</c:forEach>
									<span style="font-size:12px;">[답변]</span>
									<a href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title }</a>
								</c:when>
								<c:otherwise>
									<a href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title }</a>
								</c:otherwise>
							</c:choose>
						</td>
						<td width="10%">
							<fmt:formatDate value="${article.writeDate }" pattern="yyyy-MM-dd HH:mm:ss" />
						</td>
						<td>
							${article.view_count}
						</td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
		</tbody>
	</table>
	<div style="text-align:center; margin: 20px">
		<%
//			int total = (int)request.getAttribute("total");
			Integer total = (Integer)request.getAttribute("total");
			int pageNum = (int)request.getAttribute("pageNum");
			int countPerPage = (int)request.getAttribute("countPerPage");

			double totalPaging = Math.ceil((double)total/countPerPage);	
			
			final int section = 2;
			//몇번째 덩어리에 위치하는가 
			//pageNum == 3
			int posiotion = (int)Math.ceil((double)pageNum / section);
			int begin = ((posiotion - 1)*section ) + 1;
			int end = (begin + section) - 1;
			if(end > totalPaging){
				end = (int)totalPaging;
			}
			String style = "";
			style = "style='color:red; font-weight:bold;'";
			//el태그 사용하기 위해 써줌 
			pageContext.setAttribute("style", style);
		%>
		
		<% if(begin != 1) { %>
			<a href="${contextPath }/board/listArticles.do?pageNum=<%= begin - 1 %>&countPerPage=5" style="margin: 10px;">[ 이전 ]</a>  
		<%} %>
		<c:forEach begin="<%=begin %>" end="<%=end %>" var="paging1">
			<c:if test="${ pageNum == paging1 }">
				<a ${style} href="${contextPath }/board/listArticles.do?pageNum=${paging1}&countPerPage=5" style="margin: 10px;">[${ paging1 }]</a>  
			</c:if>
			<c:if test="${ pageNum != paging1 }">
				<a href="${contextPath }/board/listArticles.do?pageNum=${paging1}&countPerPage=5" style="margin: 10px;">[${ paging1 }]</a>  
			</c:if>
		</c:forEach>
		<% if(end != totalPaging) { %>
			<a href="${contextPath }/board/listArticles.do?pageNum=<%= end + 1 %>&countPerPage=5" style="margin: 10px;">[ 다음 ]</a>  
		<%} %>	
	</div>
	<div>
		<input type="text" id="keyword" name="keyword"><button id="btn_search">검색</button>
	</div>
	<a href="${contextPath }/board/articleForm.do">
		<p>글쓰기</p>
	</a>	
</body>
</html>
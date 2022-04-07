<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List"
    import="sec01.ex01.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script>
	
	
	$(document).ready(function(){
		
		var msg = "${msg}";
		if(msg != ""){
			alert(msg);
		}
		
		$(".delete").off("click").on("click", function(){
			var empno = $(this).attr("data-empno");
			console.log(empno);
			
			var isDel = window.confirm("삭제 하시겠습니까? [empno : "+ empno +"]");
			if(isDel){
				location.href = "/pro17/member/delMember.do?empno="+empno;
			}
		});
		
	});
</script>
</head>
<body>
	<p class="cls1">회원정보 <a href="/pro17/member/memberForm.do">회원가입</a></p>
	<table align="center" border="1">
		<tr align="center" bgcolor="lightgreen">
			<td width="7%"><b>Empno</b></td>
			<td width="7%"><b>Ename</b></td>
			<td width="7%"><b>Job</b></td>
			<td width="7%"><b>Mgr</b></td>
			<td width="7%"><b>Hiredate</b></td>
			<td width="7%"><b>Sal</b></td>
			<td width="7%"><b>Comm</b></td>
			<td width="7%"><b>Deptno</b></td>
			<td width="7%"><b>수정</b></td>
			<td width="7%"><b>삭제</b></td>
		</tr>
		
<c:choose>
	<c:when test="${empty membersList}">
		<tr>
			<td colspan="10">등록된 자료가 없습니다.</td>
		</tr>
	</c:when>
	<c:when test="${!empty membersList}">
		<c:forEach var="mem" items="${membersList}">
			<tr align="center">
				<td>${mem.empno }</td>
				<td>${mem.ename }</td>
				<td>${mem.job }</td>
				<td>${mem.mgr }</td>
				<td>${mem.hiredate }</td>
				<td>${mem.sal }</td>
				<td>${mem.comm }</td>
				<td>${mem.deptno }</td>
				<td><a href="/pro17/member/modMemberForm.do?empno=${mem.empno }">수정</a></td>
				<td><a class="delete" data-empno="${mem.empno }">삭제</a></td>
			</tr>
		</c:forEach>
	</c:when>
</c:choose>
	</table>
</body>
</html>
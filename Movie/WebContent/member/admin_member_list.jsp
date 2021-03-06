<%@page import="member.vo.MemberBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
td{
	border : 1px solid black;
}
table {
		margin: auto;
		width: 800px;
		border: 1px solid darkgray;
	}
	
	#listForm tr {
	border: 1px solid darkgray;
	}
	
	#listForm td {
	border: 1px solid darkgray;
	}
	
	a {
		text-decoration: none;
	}

	#tr_top {
		background: orange;
		width: 800px; 
		text-align: center;
	}
	
	h2 {
		text-align: center;
	}
</style>
<%
ArrayList<MemberBean> memberList = (ArrayList<MemberBean>)request.getAttribute("memberList");
%>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="css/default.css" rel="stylesheet" type="text/css">
</head>
<body> 
	<!-- 헤더 -->
	<div style="width:100%;position: sticky !important;top:0;z-index: 100;">
		<jsp:include page="/inc/top.jsp"/>
		<div class="clear"></div>
	</div>
	<!-- 헤더 -->

<section id="main">
	<section id="listForm">
		<h2>회원 목록</h2>
		<table>
			<tr id="tr_top">
				<td width="250">idx</td>
				<td width="400">email</td>
				<td width="250">pass</td>
				<td width="250">nick</td>
				<td width="400">phone</td>
				<td width="300">date</td>
			</tr>
		<% for(MemberBean mb : memberList) {%>
				<tr>
					<td align="center"><%= mb.getIdx() %></td>
					<td align="center"><%= mb.getEmail() %></td>
					<td align="center"><%= mb.getPass() %></td>
					<td align="center"><%= mb.getNick() %></td>
					<td align="center"><%= mb.getPhone() %></td>
					<td align="center"><%= mb.getDate() %></td>
				</tr>
			<%}%>
		</table>
	</section>
</section>
</body>
</html>
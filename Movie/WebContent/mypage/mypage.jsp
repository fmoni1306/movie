<%@page import="member.vo.MemberBean"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    MemberBean memberBean = (MemberBean)request.getAttribute("memberBean");
    String nick = (String)session.getAttribute("nick");
	%>
<!DOCTYPE html>
<html>
<head>
<script src="../../../Movie/js/jquery-3.5.1.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var sortData = [];
		var sortData2 = [];
		var nick = $('#nick').val();
		$.ajax('MypageGener.mp', {
			method : "get",
			dataType : "json",
			data : {
				nick : nick
			},
			success : function(data) {
				$.each(data[0],function(idx,item){
					sortData.push({key:idx,value:item});
					sortData.sort(function(a,b){
						return(a.value>b.value)?-1:(a.value<b.value)?1:0;
					});
				});
				
				$.each(data[1],function(idx,item){
					sortData2.push({key:idx,value:item});
					sortData2.sort(function(a,b){
						return(a.value>b.value)?-1:(a.value<b.value)?1:0;
					});
				});
				
				$.each(sortData,function(idx,item){
					if(item.value!=0){
						$.each(sortData2,function(idx2,item2){
							if(item2.value!=0){
								if(item.key==item2.key){
									var count = item2.value/2/item.value;
									$('.genre').append(item.key +" : "+item.value+ " " +count.toFixed(1)+"점 <br>");
								}
							}
						});
						
					}
				});
				
			}
		});
		
// 		$.ajax('MypageTitle.me',{
// 			method : "get",
// 			dataType : "json",
// 			data : {
// 				nick : nick
// 			},
// 			success : function(data) {
// 				$.each(data,function(idx,item){
// 					});
				
// 			}
// 		});
		
	});
</script>
<meta charset="UTF-8">
<title>MyPage - MyPage</title>
<link href="${pageContext.request.contextPath}/css/default.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/mypageboard.css" rel="stylesheet" type="text/css">
</head>
<body>
<!-- 헤더 -->
<jsp:include page="/inc/top.jsp" />
<div class="clear"></div> 
<!-- 헤더 -->

<section id="main">
	<!-- 왼쪽메뉴 -->
	<div id="sub_menu"> 
		<ul>
			<li><a href="Mypage.mp">마이 페이지</a></li>
			<li><a href="MypageGrade.mp">평가한 영화</a></li>
			<li><a href="MypageSelectWish.mp">좋아요 누른 영화</a></li>
			<li><a href="BoardReviewList.bo">리뷰</a></li>
            
		</ul> 
	</div> 
	
	<div id="myinfo">
	<!-- css / 프로필 박스 (프로필 사진 및 수정 기능 넣을 곳) -->
		<div id="profilebox">
			<img class="profile-background" src="img/profile_yellow.jpg" onerror="this.src='img/profile_yellow.jpg'" />
			<article id="profile-article">
				<div class="img-box">
					<img class="profile" src="img/noneprofile.jpg">
				</div>
				
				<div class="info-box">
					<%=nick%><br>
					<%=memberBean.getEmail()%><br>
					아직 소개글이 없습니다.
				</div>
				
				<div class="clear"></div>
				<div class="profile-update">
					<a href="MypageForm.mp">프로필수정</a>
				</div>
			</article>
		
		</div>
		
		
		<fieldset>
		<legend>선호 장르</legend>
		<span class="genre"></span> <span class="count"></span>
		</fieldset>		
	</div>
</section>
</body>
</html>
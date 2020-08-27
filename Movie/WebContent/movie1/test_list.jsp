<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script src="../../../Movie/js/jquery-3.5.1.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
// 		$('#btn').click(function(){
			var name = $("#na").val();
			var movieSeq = $("#movieSeq").val();
			var query = $("#query").val();
			$.ajax({
				url:"MovieDetail.mo",
				method:"get",
				dataType :"json",
				data:{
					movieSeq:movieSeq,
					query:query
					},
				success:function(data){
					
					$.each(data.Data,function(idx,item){
						
						
						$.each(item.Result,function(idx,item2){
							
							var title = item2.title
							var titleNoSpace = title.replace(/ /g, '');
							var title2 = titleNoSpace.replace(/!HS/g,'')
							var title3 = title2.replace(/!HE/g,'')
							var title5 = title3.trim();
							
							var image = item2.posters.split("|")
							var keyword = item2.keywords.split(",")
							var actors="";
							for(var num = 0; num < item2.staff.length ; num++){
								if(item2.staff.length>11){
									if(num==11){
										actors = actors + item2.staff[num].staffNm;
										break;
									}else{
									actors = actors + item2.staff[num].staffNm + ", ";	
										
									}
					
								}else {
									if(num==item2.staff.length-1){
										actors = actors + item2.staff[num].staffNm;
										break;
									}else{
									actors = actors + item2.staff[num].staffNm + ", ";	
										
									}
								}
								
								
								
								
							}
							
							$('#detail').append('<div class=title>'+title5+'</div>')
							$('#detail').append('<div class=title>'+item2.repRlsDate+'</div>')
							$('#detail').append('<div class=titleEng>'+item2.titleEng+'</div>')
							$('#detail').append('<div class=nation>'+item2.nation+'</div>')
							$('#detail').append('<div class=runtime>'+item2.runtime+'</div>')
							$('#detail').append('<div class=rating>'+item2.rating[0].ratingGrade+'</div>')
							$('#detail').append('<div class=actors>'+actors+'</div>')
							$('#detail').append('<div class=company>'+item2.company+'</div>')
							$('#detail').append('<div class=plot>'+item2.plot+'</div>')
							for(var i in image){
							$('#posters').append('<div style=float:left; class=image><img src='+image[i]+'></div>')
							}
								if(keyword[0]!=""){
									
								for(var i in keyword){
										
									$('#keyword').append('<div style=float:left; class=keyword><a href=MovieSearch.mo?query='+keyword[i]+'>#'+keyword[i]+'&nbsp;</div>')
								}
								}
							
							
							
		                  });
						
						
						
						
					});
					
					
				}
				
			});
			
// 		});
	});
</script>
</head>
<body>
<%String movieSeq = request.getParameter("movieSeq");
  String query = request.getParameter("query");%>
	<input type="hidden" id="movieSeq" value="<%=movieSeq%>">
	<input type="hidden" id="query" value="<%=query%>">
	<section id="detail">
	</section>
	<section id="posters">
	</section>
	<section style=float:left; id="keyword">
	</section>
</body>
</html>


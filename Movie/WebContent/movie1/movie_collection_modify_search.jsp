<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
   <% String add = request.getParameter("addId"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="${pageContext.request.contextPath}/css/default.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/movieboard.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.9.0/slick.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.9.0/slick-theme.min.css">
<script src="${pageContext.request.contextPath}/js/jquery-3.5.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.9.0/slick.min.js"></script>
<script type="text/javascript">
// 기본검색기능을 담당하는 뷰페이지
$(document).ready(function(){
   var latitude, longitude;
   var API_KEY = '19eab104c69d6fa4c412bfe0078fdd0d';
   var temp = '0';
   var weather = 'no';
   
   function getLocation(){
      window.navigator.geolocation.getCurrentPosition(current_position);
   }
   
   function current_position(position){
      latitude = position.coords.latitude;
      longitude = position.coords.longitude;
      $.ajax("https://api.openweathermap.org/data/2.5/weather?lat="+latitude
            +"&lon="+longitude+"&appid="+API_KEY+"&units=metric&lang=kr",{
         dataType:"json",
         async:false,
         success:function(data){
            $('#temp').val(data.main.temp);
            $('#weather').val(data.weather[0].main);
            temp = $('#temp').val();
            weather = $('#weather').val();
         }
      });
   }
      
   window.addEventListener("load",getLocation);
   
      var query = $("#query").val();
      var nick = $("#nick").val();
      query = query.replace(/ /g,'');
      $.ajax('MovieSearchPro.mo',{
         method:"post",
         dataType :"json",
         data:{query:query},
         success:function(data){
            // 처음 결과 4개의 배열구조
            $.each(data.Data,function(idx,item){
               // 4번째 오브젝드 Data 안에 것을 가져옴
               var count = item.Count
                  
               $.each(item.Result,function(idx,item2){
                  // 가져오너 Data 안에 Result 데이터를가져옴( 우리가원하는 값들이 여기 모두 들어있음)
                  var title = item2.title // 타이블을 변수 지정
                  var titleNoSpace = title.replace(/ /g, ''); // 타이틀 공백제거
                  var title2 = titleNoSpace.replace(/!HS/g,'') // 검색어는 !HS , !HE 로 둘러 싸여있어서 제거해줌
                  var title3 = title2.replace(/!HE/g,'')
                  var title5 = title3.trim(); // 양쪽끝에 공백을 제거해줌
                  var title6 =  encodeURIComponent(title5);
                  var actors="";
                  
                  var image = item2.posters.split("|") // 포스터 데이터는 | 로 구분되어있어서 스플리 처리함 ( 여러개 있음 )
                  var nation = item2.nation
                  if(nation == "대한민국"){ // 국내 국외 영화구분을 위한 제어문
                     
                     for(var num = 0; num < item2.actor.length ; num++){
                        actors = actors + item2.actor[num].actorNm + ", ";   
                     }
                        
                     if(image[0]){
                        $('#koreaList').append("<div class=koreaMovie>"+
                              '<div class=poster><a href=MovieDetailBySearch.mo?movieId='+item2.movieId+'&movieSeq='+item2.movieSeq+'&query='+title6+'&image='+image[0]+'&temp='+temp+'&weather='+weather+'><img style=width:250px;height:350px;padding-right:20px; src='+image[0]+'></a></div>'+
                              '<div class=nation>'+item2.nation+'</div>'+
                              '<div class=rating>'+item2.rating[0].ratingGrade+'</div>'+
                              '<div class=title>'+title5+'</div>' +
                              "<input type='button' value='담기' id='" + item2.movieSeq + "'</div>");
                     } 
                     
                     $('#koreaList').on('click', '#'+item2.movieSeq, function() {
//                     	 $("#movies", opener.document).text("<li>"+item2.movieSeq+"</li>");

						 var movieSeq = item2.movieSeq;
						 var poster = image[0];
                    	 $.ajax('CollectionMovieAdd.mp',{
     	   					data:{nick:nick,movieSeq:movieSeq,title:title5,poster:poster},
     	   					success:function(rdata){
//      	   							$("#movies", parent.opener.document).val();
//      	  			 				opener.location.reload();
     	  			      		   $('#' + '<%=add%>', opener.document).after(
     	  			      			    "<br>" +
     	  			      				"<img src='" +image[0]+"'>"+ "<br>" +
     	  			      				title5+ 
     	  			      				"<input type='hidden' value='"+ movieSeq + "'name = 'movieSeq' id='movieSeq'>" +
     	  			      				"<input type='hidden' value='"+ poster + "'name = 'poster'>" + "<br>" + 
     	  			      				"<input type='hidden' value='"+ title5 + "'name = 'title'>" 
     	  			      		   );  

     	   						}
     	 					});
                    	});
//                      else{
//                         $('#koreaList').append('<div class=poster><img src=../../../Movie/img/noImage.gif></div>');
//                      }
                  }
                  else {
                     
                     for(var num = 0; num < item2.actor.length ; num++){
                        actors = actors + item2.actor[num].actorNm + ", ";   
                     }
                        
                     if(image[0]){
                        $('#foreignList').append("<div class=foreignMovie>"+
                              '<div class=poster><a href=MovieDetailBySearch.mo?movieId='+item2.movieId+'&movieSeq='+item2.movieSeq+'&query='+title6+'&image='+image[0]+'&temp='+temp+'&weather='+weather+'><img style=width:250px;height:350px;padding-right:20px; src='+image[0]+'></a></div>'+
                              '<div class=nation>'+item2.nation+'</div>'+
                              '<div class=rating>'+item2.rating[0].ratingGrade+'</div>'+
                              '<div class=title>'+title5+'</div>' +
                              "<input type='button' value='담기' id='" + item2.movieSeq + "'</div>");
                     } 
                     $('#foreignList').on('click', '#'+item2.movieSeq, function() {
//                     	 $("#movies", opener.document).text("<li>"+item2.movieSeq+"</li>");

                    	 var movieSeq = item2.movieSeq;
						 var poster = image[0];
                    	 $.ajax('CollectionMovieAdd.mp',{
     	   					data:{nick:nick,movieSeq:movieSeq,title:title5,poster:poster},
     	   					success:function(rdata){
//      	   							$("#movies", parent.opener.document).val();
//      	  			 				opener.location.reload();
     	  			      		   $('#' + '<%=add%>', opener.document).after(
     	  			      			    "<br>" +
     	  			      				"<img src='" +image[0]+"'>"+ "<br>" +
     	  			      				title5+ 
     	  			      				"<input type='hidden' value='"+ movieSeq + "'name = 'movieSeq' id='movieSeq'>" +
     	  			      				"<input type='hidden' value='"+ poster + "'name = 'poster'>" + "<br>" + 
     	  			      				"<input type='hidden' value='"+ title5 + "'name = 'title'>" 
     	  			      		   );  

     	   						}
     	 					});
                    	});


//                      else{ 포스터 없는거는 거의 대부분 안유명한거라 뺌
//                         $('#foreignList').append('<div class=poster><img src=../../../Movie/img/noImage.gif></div>');
//                      }
                  
                  }
                  
               
                  
                     });
            });
            
            $('#koreaList').slick({
                     dots: false,
                     infinite: false,
                     arrows: true,
                     variableWidth:true,
                     speed: 300,
                     slidesToShow: 4,
                     slidesToScroll: 3,
                     responsive: [
                       {
                         breakpoint: 1024,
                         settings: {
                           slidesToShow: 3,
                           slidesToScroll: 3,
                           infinite: true,
                           dots: true
                         }
                       },
                       {
                         breakpoint: 600,
                         settings: {
                           slidesToShow: 2,
                           slidesToScroll: 2
                         }
                       },
                       {
                         breakpoint: 480,
                         settings: {
                           slidesToShow: 1,
                           slidesToScroll: 1
                         }
                       }
                       // You can unslick at a given breakpoint now by adding:
                       // settings: "unslick"
                       // instead of a settings object
                     ]
                   }); // slick(koreaList)끝
                   
                   $('#foreignList').slick({
                       dots: false,
                       infinite: false,
                       arrows: true,
                       variableWidth:true,
                       speed: 300,
                       slidesToShow: 4,
                       slidesToScroll: 3,
                       responsive: [
                         {
                           breakpoint: 1024,
                           settings: {
                             slidesToShow: 3,
                             slidesToScroll: 3,
                             infinite: true,
                             dots: true
                           }
                         },
                         {
                           breakpoint: 600,
                           settings: {
                             slidesToShow: 2,
                             slidesToScroll: 2
                           }
                         },
                         {
                           breakpoint: 480,
                           settings: {
                             slidesToShow: 1,
                             slidesToScroll: 1
                           }
                         }
                         // You can unslick at a given breakpoint now by adding:
                         // settings: "unslick"
                         // instead of a settings object
                       ]
                     }); // slick(foreignList)끝
                   
                   
                   
                   
                   
                   
         }
   });
      // 배우 검색 기능을 담당하는 Jquery문
      $.ajax('MovieSearchActorPro.mo',{
         method:"post",
         dataType :"json",
         data:{query:query},
         success:function(data){
            $.each(data.Data,function(idx,item){
               
               var count = item.Count
                  
               $.each(item.Result,function(idx,item2){
                  
                  var title = item2.title
                  var titleNoSpace = title.replace(/ /g, '');
                  var title2 = titleNoSpace.replace(/!HS/g,'')
                  var title3 = title2.replace(/!HE/g,'');
                  var title5 = title3.trim();
                  var title6 =  encodeURIComponent(title5);
                  var actors="";
                  var image = item2.posters.split("|")
                  
                  for(var num = 0; num < item2.actor.length ; num++){
                     actors = actors + item2.actor[num].actorNm + ", ";   
                  }
                     
                  if(image[0]){
                     $('#actorList').append("<div class=koreaMovie>"+
                           '<div class=poster><a href=MovieDetailBySearch.mo?movieId='+item2.movieId+'&movieSeq='+item2.movieSeq+'&query='+title6+'&image='+image[0]+'&temp='+temp+'&weather='+weather+'><img style=width:250px;height:350px;padding-right:20px; src='+image[0]+'></a></div>'+
                           '<div class=nation>'+item2.nation+'</div>'+
                           '<div class=rating>'+item2.rating[0].ratingGrade+'</div>'+
                           '<div class=title>'+title5+'</div>' +
                           "<input type='button' value='담기' id='" + item2.movieSeq + "'</div>");
                  }  
                  $('#actorList').on('click', '#'+item2.movieSeq, function() {
//                  	 $("#movies", opener.document).text("<li>"+item2.movieSeq+"</li>");

                	  var movieSeq = item2.movieSeq;
						 var poster = image[0];
                 	 $.ajax('CollectionMovieAdd.mp',{
  	   					data:{nick:nick,movieSeq:movieSeq,title:title5,poster:poster},
  	   					success:function(rdata){
//   	   							$("#movies", parent.opener.document).val();
//   	  			 				opener.location.reload();
  	  			      		   $('#' + '<%=add%>', opener.document).after(
  	  			      			    "<br>" +
  	  			      			
  	  			      				"<img src='" +image[0]+"'>"+ "<br>" +
  	  			      				"<a href='MovieDetailPro.mo?movieSeq=" + movieSeq + '&query=' + title6 + ">"+ title5 + "</a>" + 
  	  			      				"<input type='hidden' value='"+ movieSeq + "'name = 'movieSeq' id='movieSeq'>" +
  	  			      				"<input type='hidden' value='"+ poster + "'name = 'poster'>" + "<br>" + 
  	  			      				"<input type='hidden' value='"+ title5 + "'name = 'title'>" 
  	  			      		   );  

  	   						}
  	 					});
                 	});
//                   else{
//                      $('#actorList').append('<div class=poster><img src=../../../Movie/img/noImage.gif></div>');
//                   }
                     });
            });
            
            $('#actorList').slick({
                dots: false,
                infinite: false,
                arrows: true,
                variableWidth:true,
                speed: 300,
                slidesToShow: 4,
                slidesToScroll: 3,
                responsive: [
                  {
                    breakpoint: 1024,
                    settings: {
                      slidesToShow: 3,
                      slidesToScroll: 3,
                      infinite: true,
                      dots: true
                    }
                  },
                  {
                    breakpoint: 600,
                    settings: {
                      slidesToShow: 2,
                      slidesToScroll: 2
                    }
                  },
                  {
                    breakpoint: 480,
                    settings: {
                      slidesToShow: 1,
                      slidesToScroll: 1
                    }
                  }
                  // You can unslick at a given breakpoint now by adding:
                  // settings: "unslick"
                  // instead of a settings object
                ]
              }); // slick(actorList)끝
            
            
            
         }
   });
      // 감독검색을 담당하는 Jquery문
      $.ajax('MovieSearchDirectorPro.mo',{
         method:"post",
         dataType :"json",
         data:{query:query},
         success:function(data){
            
            $.each(data.Data,function(idx,item){
               
               var count = item.Count
                  
               $.each(item.Result,function(idx,item2){
                  
                  var title = item2.title
                  var titleNoSpace = title.replace(/ /g, '');
                  var title2 = titleNoSpace.replace(/!HS/g,'');
                  var title3 = title2.replace(/!HE/g,'');
                  var title5 = title3.trim();
                  var title6 =  encodeURIComponent(title5);
                  var actors="";
                  
                  var image = item2.posters.split("|")
                  
                  for(var num = 0; num < item2.actor.length ; num++){
                     actors = actors + item2.actor[num].actorNm + ", ";   
                  }
                     
                  if(image[0]){
                     $('#directorList').append("<div class=koreaMovie>"+
                           '<div class=poster><a href=MovieDetailBySearch.mo?movieId='+item2.movieId+'&movieSeq='+item2.movieSeq+'&query='+title6+'&image='+image[0]+'&temp='+temp+'&weather='+weather+'><img style=width:250px;height:350px;padding-right:20px; src='+image[0]+'></a></div>'+
                           '<div class=nation>'+item2.nation+'</div>'+
                           '<div class=rating>'+item2.rating[0].ratingGrade+'</div>'+
                           '<div class=title>'+title5+'</div></div>');
                  }
                  $('#directorList').on('click', '#'+item2.movieSeq, function() {
//               	 $("#movies", opener.document).text("<li>"+item2.movieSeq+"</li>");

             	  var movieSeq = item2.movieSeq;
						 var poster = image[0];
              	 $.ajax('CollectionMovieAdd.mp',{
	   					data:{nick:nick,movieSeq:movieSeq,title:title5,poster:poster},
	   					success:function(rdata){
//	   							$("#movies", parent.opener.document).val();
//	  			 				opener.location.reload();
	  			      		   $('#' + '<%=add%>', opener.document).after(
	  			      			    "<br>" +
	  			      				"<img src='" +image[0]+"'>"+ "<br>" +
	  			      				title5+ 
	  			      				"<input type='hidden' value='"+ movieSeq + "'name = 'movieSeq' id='movieSeq'>" +
	  			      				"<input type='hidden' value='"+ poster + "'name = 'poster'>" + "<br>" + 
	  			      				"<input type='hidden' value='"+ title5 + "'name = 'title'>" 
	  			      		   );  

	   						}
	 					});
              	});
//                   else{
//                      $('#directorList').append('<div class=poster><img src=../../../Movie/img/noImage.gif></div>');
//                   }
                     });
            });
            
            
            $('#directorList').slick({
                dots: false,
                infinite: false,
                arrows: true,
                variableWidth:true,
                speed: 300,
                slidesToShow: 4,
                slidesToScroll: 3,
                responsive: [
                  {
                    breakpoint: 1024,
                    settings: {
                      slidesToShow: 3,
                      slidesToScroll: 3,
                      infinite: true,
                      dots: true
                    }
                  },
                  {
                    breakpoint: 600,
                    settings: {
                      slidesToShow: 2,
                      slidesToScroll: 2
                    }
                  },
                  {
                    breakpoint: 480,
                    settings: {
                      slidesToShow: 1,
                      slidesToScroll: 1
                    }
                  }
                  // You can unslick at a given breakpoint now by adding:
                  // settings: "unslick"
                  // instead of a settings object
                ]
              }); // slick(directorList)끝
            
            
         }
   });
      
});
</script>
</head>
<body>
<section id="main">
<jsp:include page="/inc/collectionModifyTop.jsp" >
	<jsp:param value="<%=add %>" name="add"/>
</jsp:include>
<div class="clear"></div>
<%String query=request.getParameter("query"); %>
<%String nick = (String)session.getAttribute("nick"); %>
   <input type="hidden" id="query" name=query value="<%=query%>">
   <input type="hidden" id="nick" name=nick value="<%=nick%>">
   <input type="hidden" id="temp">
   <input type="hidden" id="weather">
<h1>국내영화</h1>
   <section id="koreaList">
   </section>
<h1>국외영화</h1>
   <section id="foreignList">
   </section>
<h1>영화인 검색 결과</h1>
   <section id="actorList"></section>
<h1>감독 검색 결과</h1>
   <section id="directorList"></section>
</section>
</body>
</html>
package mypage.action;

import java.io.PrintWriter;
import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.StringUtils;

import action.Action;
import mypage.svc.MypageAddCollectionService;
import mypage.svc.MypageGradeService;
import mypage.vo.CollectionBean;
import vo.ActionForward;

public class MypageCollectionUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("CollectionUpdateAction 작동..제발");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		String nick = (String)session.getAttribute("nick");
		String[] title = request.getParameterValues("title");
		String[] movieSeq = request.getParameterValues("movieSeq");
		String[] poster = request.getParameterValues("poster");
		int idx = Integer.parseInt(request.getParameter("idx"));
//		String subject = request.getParameter("subject");
//		String content = request.getParameter("content");
		System.out.println("콜렉션업데이트 작동해라");
		System.out.println(title[0]);
		String joinTitle = String.join(",", title);
		String joinPoster = String.join(",", poster);
		String joinMovieSeq = String.join("," , movieSeq);
		
		System.out.println("뀨ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ");
		
		
		
		for(int i = 0; i<poster.length;i++) {
			System.out.println((i+1)+"번째 포스터값 : " + poster[i]);
		}
		
		
		
		int[] intArr = null;
		if( movieSeq != null ){
		intArr = new int[ movieSeq.length ];
		for( int i=0;i <movieSeq.length; i++ ) {
			intArr[i] = Integer.parseInt( movieSeq[i] );
			}
		}
		System.out.println(joinTitle + joinPoster + joinMovieSeq);
		CollectionBean collectionBean = new CollectionBean();
		collectionBean.setNick(nick);
		collectionBean.setPoster(joinPoster);
		collectionBean.setTitle(joinTitle);
		collectionBean.setMovieSeq(joinMovieSeq);
		collectionBean.setIdx(idx);
		
		MypageAddCollectionService mypageAddCollectionService = new MypageAddCollectionService();
		boolean isSuccess = mypageAddCollectionService.updateCollection(collectionBean);
		
		if(isSuccess) {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('등록되었습니다')");
//			out.println("window.location.href = 'MypageCollection.mp'");
			out.println("window.location.href = 'Mypage.mp'");
			out.println("</script>");
//			forward = new ActionForward();
//			forward.setPath("Main.me");
//			forward.setRedirect(true);
		}
		
//		collectionBean.setCollection_name(subject);
//		collectionBean.setNick(nick);
//		for(int i = 0; i < title.length; i++) {
//			title = title[i];
//		}
//		collectionBean.setTitle(title[1]);
		
//		forward = new ActionForward();
//		forward.setPath("/mypage/mypage.jsp");
//		forward.setPath("Mypage.mp");
		
		return forward;
	}
	

}



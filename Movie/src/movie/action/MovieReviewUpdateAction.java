package movie.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import action.Action;
import movie.svc.MovieReviewService;
import movie.vo.ReviewBean;
import vo.ActionForward;

public class MovieReviewUpdateAction implements Action {

	@Override 
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nick = request.getParameter("nick");
		int movieSeq = Integer.parseInt(request.getParameter("movieSeq"));
		String typeName = request.getParameter("typeName");
		String review = request.getParameter("review");
		System.out.println(review);
		ReviewBean rb = new ReviewBean();
		rb.setNick(nick);
		rb.setMovieSeq(movieSeq);
		rb.setType(typeName);
		rb.setContent(review);
		
		MovieReviewService movieReviewService = new MovieReviewService();
		boolean isUpdate = movieReviewService.isUpdate(rb);
		
		if(isUpdate) {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(nick+ "님의 리뷰 : " + review);
		}
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
	    forward.setPath("/movie1/movie_detail.jsp");
//	    request.setAttribute("returnCmt", comment);
	    return forward;
		
	}

}

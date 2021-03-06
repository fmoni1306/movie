package board.action;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import javax.servlet.http.*;

import action.*;
import board.svc.*;
import board.vo.*;
import vo.ActionForward;

public class BoardReplyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardReplyProAction");
		
		ActionForward forward = null;
		
		
		int idx = Integer.parseInt(request.getParameter("idx"));
		HttpSession session = request.getSession();
		String nick = (String)session.getAttribute("nick");
		int movieSeq = Integer.parseInt(request.getParameter("movieSeq"));
		String reply = request.getParameter("reply");
		Date date = new Date(System.currentTimeMillis());
		String currDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		currDate = request.getParameter("date");
		
		
		ReplyBean replyBean = new ReplyBean();
		replyBean.setIdx(idx);
		replyBean.setNick(nick);
		replyBean.setMovieSeq(movieSeq);
		replyBean.setReply(reply);
		replyBean.setDate(date); 
		
		
		if(reply.equals("")) {
			String out = "out";
			HttpSession session2 = request.getSession();
			session2.setAttribute("out", out);
			
		} else {
			
			BoardReplyService boardReplyService = new BoardReplyService();
			boolean isSuccess = boardReplyService.insertReply(replyBean, idx);
			
			
			forward = new ActionForward();
			forward.setRedirect(false);
			forward.setPath("BoardReviewView.bo");
		}
			
		
	    return forward;
		
	}

}

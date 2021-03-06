package member.controller;

import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import member.action.MemberEmailAction;
import member.action.MemberForgetAction;
import member.action.MemberForgetEmailAction;
import member.action.MemberForgetPasswordAction;
import member.action.MemberJoinProAction;
import member.action.MemberListAction;
import member.action.MemberLoginProAction;
import member.action.MemberLogoutAction;
import member.action.MemberMessageAction;
import member.action.MemberMessageUpdateAction;
import member.action.MemberNickAction;
import member.action.MemberUpdateAction;
import member.action.MemberUpdatePasswordAction;
import movie.action.MovieNaverRankingAction;
import mypage.action.MypageModifyProfileAction;
import mypage.action.MypageSelectProfileTopAction;
import vo.ActionForward;


@WebServlet("*.me")
public class MemberFrontController extends HttpServlet {

	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String command = request.getServletPath();
		System.out.println(command);
		Action action = null;
		ActionForward forward = null;
		if (command.equals("/MemberJoinForm.me")) {
			forward = new ActionForward();
			forward.setPath("/member/member_insert_form.jsp");

		} else if (command.equals("/MemberJoinPro.me")) {
			action = new MemberJoinProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/dupNick.me")) {
			action = new MemberNickAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
	  		
    	} else if (command.equals("/MemberLoginForm.me")) {
			forward = new ActionForward();
			forward.setPath("/member/member_login.jsp");
		} else if (command.equals("/MemberLoginPro.me")) {
			action = new MemberLoginProAction();

			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/ForgetForm.me")) {
			forward = new ActionForward();
			forward.setPath("/member/member_forget.jsp");
			
		} else if(command.equals("/ForgetPro.me")) {
			action = new MemberForgetAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/ForgetEmailChForm.me")) { // 삭제해도 될듯함(낙원:0917)
			forward = new ActionForward();
			forward.setPath("/member/forget_emailch.jsp");
		} else if(command.equals("/MemberLogout.me")) {
			action = new MemberLogoutAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}  else if(command.equals("/MemberList.me")) {
			action = new MemberListAction();
			
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (command.equals("/Main.me")) { 
			forward = new ActionForward();
			forward.setPath("/index.jsp");
		} else if (command.equals("/Message.me")) {
			action = new MemberMessageAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (command.equals("/Email.me")) {
			action = new MemberEmailAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (command.equals("/MemberLogout.me")) {
			action = new MemberLogoutAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(command.equals("/ForgetPassword.me")) {
			forward = new ActionForward();
			forward.setPath("/member/forget_email.jsp");
			
		} else if(command.equals("/ForgetPasswordPro.me")) {
			action = new MemberForgetPasswordAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/ForgetEmail.me")) {
			forward = new ActionForward();
			forward.setPath("/member/forget_phone.jsp");
			
		} else if(command.equals("/ForgetEmailPro.me")) {
			action = new MemberForgetEmailAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  else if(command.equals("/MemberUpdatePasswordPro.me")) {
			action = new MemberUpdatePasswordAction();
			
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  else if(command.equals("/MemberUpdatePro.me")) { // 마이페이지 회원정보수정 추가 - 낙원 : 1012
			action = new MemberUpdateAction();
			
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (command.equals("/MessageUpdate.me")) {
			action = new MemberMessageUpdateAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  
		

		if (forward != null) {
			if (forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

}

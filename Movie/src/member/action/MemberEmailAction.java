package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import api.SendEmail;
import member.svc.DupCheckService;
import vo.ActionForward;


public class MemberEmailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MemberEmailAction!");
		
		
		String email = request.getParameter("email");
		System.out.println("email주소는 " + email);
		SendEmail sendEmail = new SendEmail();
		DupCheckService dupCheck = new DupCheckService();
		boolean checkResult = dupCheck.dupCheck(email, "email");
		
		if(!checkResult) {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script>");
			out.print("alert('중복된 E-Mail입니다.')");
			out.print("</script>");
		} else {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script>");
			out.print("alert('사용할 수 있는 E-Mail 입니다.')");
			out.print("</script>");
			String certificationNum_email = sendEmail.getCertificationNum(email);
			out.print("인증 메세지가 전송되었습니다.");
			out.print("<input type='text' id='hiddenCnum_email' value=" + certificationNum_email + ">");
			System.out.print(certificationNum_email);
		}
			
			
			
		
		return null;
		
	}
	
}

















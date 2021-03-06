package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import api.SendMessage;
import member.svc.DupCheckService;
import vo.ActionForward;

public class MemberMessageAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
		SendMessage sendMessage = new SendMessage();
		String phone = request.getParameter("phone");
		String cNum = request.getParameter("cNum");

		System.out.println(cNum);

		DupCheckService dupCheck = new DupCheckService();
		boolean result = dupCheck.dupCheck(phone, "phone");

		if (phone != null) {
			if (!result) {
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("중복된 번호");

			} else {
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				String certificationNum = sendMessage.getCertificationNum(phone);
				out.print("인증 메세지가 전송되었습니다.");
				out.print("<input type='text' id='hiddenCnum' value=" + certificationNum + ">");
				System.out.println(certificationNum);

			}
		}

		return null;
	}

}

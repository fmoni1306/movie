package member.svc;

import static db.JdbcUtil.close;
import static db.JdbcUtil.getConnection;

import java.sql.Connection;

import member.dao.MemberDAO;
import member.vo.MemberBean;

public class MemberLoginProService {
	
	// 전달받은 이메일, 패스워드 -> 로그인 작업 요청하는 login메서드 정의
	public String login(MemberBean member) throws Exception{
		System.out.println("MemberLoginProService~~!!!!!");
		
		Connection con = getConnection();
		
		MemberDAO memberDAO = MemberDAO.getInstance();
		
		memberDAO.setConnection(con);
		
		// MemberDAO 클래스의 login 메서드 호출하여 일치여부 판별
		String nick = memberDAO.login(member);
		
		close(con);
		
		return nick;
	}

}

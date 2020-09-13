package mypage.svc;

import static db.JdbcUtil.close;
import static db.JdbcUtil.commit;
import static db.JdbcUtil.getConnection;
import static db.JdbcUtil.rollback;

import java.sql.Connection;

import member.dao.MemberDAO;
import mypage.dao.MypageDAO;
import vo.MemberBean;

public class MypageService {

	public MemberBean selectMypageinfo(String nick) {
		System.out.println("MypageService 도착!!");
		
		MemberBean memberBean = new MemberBean();
		
		Connection con = getConnection();
		MypageDAO  dao = MypageDAO.getInstance();
		dao.setConnection(con);
		
		memberBean = dao.selectMypageinfo(nick);
		
		System.out.println("Service - " + nick);
		System.out.println("Service - dao에서 갖고 온 이메일" + memberBean.getEmail());
		
		close(con);
		
		return memberBean;
		
		
	}



}

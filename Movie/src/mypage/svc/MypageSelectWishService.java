package mypage.svc;

import static db.JdbcUtil.close;
import static db.JdbcUtil.getConnection;

import java.sql.Connection;
import java.util.ArrayList;

import mypage.dao.MypageDAO;
import mypage.vo.MypageBean;

public class MypageSelectWishService {

	public MypageBean selectWish(String nick,int movieSeq) {
		System.out.println("MypageWishService - getWishMovie");
		
		Connection con = getConnection();
		MypageDAO mypageDAO = MypageDAO.getInstance();
		mypageDAO.setConnection(con);
		
		MypageBean wishMovie =mypageDAO.selectWish(nick,movieSeq);
		
		close(con);
		
		return wishMovie;
	}

}

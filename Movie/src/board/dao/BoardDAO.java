package board.dao;

import static db.JdbcUtil.*;

import java.sql.*;
import java.util.*;

import board.vo.*;

public class BoardDAO {
	private BoardDAO() {
	}

	private static BoardDAO instance;

	public static BoardDAO getInstance() {
		if (instance == null) {
			instance = new BoardDAO();
		}

		return instance;
	}

	Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}

	PreparedStatement pstmt;
	ResultSet rs;

	// -------------------------------------------------

	public int selectReviewListCount() {
		System.out.println("BoardDAO - selectReviewListCount()");

		int listCount = 0;

		try {
			String sql = "select count(*) from review";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				listCount = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - selectReviewListCount 에러 : " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return listCount;
	}

	public ArrayList<ReviewBean> selectReviewList(int page, int limit, String nick) {
		System.out.println("BoardDAO - selectReviewList");

		ArrayList<ReviewBean> reviewList = null;

		try {
			int startRow = (page - 1) * 10;
			String sql = "SELECT * FROM review WHERE nick=? ORDER BY idx DESC LIMIT ?,?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, limit);
			rs = pstmt.executeQuery();

			reviewList = new ArrayList<ReviewBean>();

			while (rs.next()) {
				ReviewBean reviewBean = new ReviewBean();
				reviewBean.setIdx(rs.getInt(1));
				reviewBean.setNick(rs.getString(2));
				reviewBean.setGrade(rs.getInt(3));
				reviewBean.setGenre(rs.getString(4));
				reviewBean.setMovieSeq(rs.getInt(5));
				reviewBean.setTitle(rs.getString(6));
				reviewBean.setType_name(rs.getString(7));
				reviewBean.setContent(rs.getString(8));
				reviewBean.setLike_count(rs.getInt(9));
				reviewBean.setReport(rs.getInt(10));
				reviewBean.setSpoiler(rs.getInt(11));

				reviewList.add(reviewBean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - selectReviewList() 에러" + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return reviewList;
	}

	// -----------------------------------------------------------------------------------
	
	public ArrayList<ReviewBean> getReview(ReviewBean reviewBean){

		ArrayList<ReviewBean> list = null;
		try {
			String sql = "select * from review where movieSeq=? ORDER BY idx DESC";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, reviewBean.getMovieSeq());
			rs = pstmt.executeQuery();
			list = new ArrayList<ReviewBean>();
			while (rs.next()) {
				ReviewBean reviewB = new ReviewBean();
				reviewB.setIdx(rs.getInt("idx"));
				reviewB.setNick(rs.getString("nick"));
				reviewB.setMovieSeq(rs.getInt("movieSeq"));
				reviewB.setTitle(rs.getString("title"));
				reviewB.setContent(rs.getString("content"));
				reviewB.setGrade(rs.getInt("grade"));
				reviewB.setLike_count(rs.getInt("like_count"));
				reviewB.setReport(rs.getInt("report"));
				reviewB.setSpoiler(rs.getInt("spoiler"));
				
				list.add(reviewB);

			}

		} catch (SQLException e) {

		} finally {
			close(rs);
			close(pstmt);
		}

		return list;
	}


	public ReviewBean getReviewDetail(int idx, int movieSeq) {
		System.out.println("BoardDAO");
		
		ReviewBean reviewBean = new ReviewBean();
		
		try {
			String sql = "SELECT * FROM review WHERE idx=? AND movieSeq=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.setInt(2, movieSeq);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				reviewBean.setIdx(rs.getInt("idx"));
				reviewBean.setGrade(rs.getInt("grade"));
				reviewBean.setMovieSeq(rs.getInt("movieSeq"));
				reviewBean.setTitle(rs.getString("title"));
				reviewBean.setContent(rs.getString("content"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - getReviewDetail() 에러 : " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return reviewBean;
	}

	public int insertReply(ReplyBean replyBean, int idx) {
		System.out.println("BoardDAO - insertReply()");
		
		int insertCount = 0;
		
		try {
			String sql = "INSERT INTO reply VALUES(idx,?,?,?,?,now(),0)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, replyBean.getNick());
			pstmt.setInt(2, replyBean.getMovieSeq());
			pstmt.setString(3, replyBean.getReply());
			pstmt.setInt(4, idx);
			
			insertCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - insertReply() 에러 : " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return insertCount;
	}
	

	public ArrayList<ReplyBean> getListReply(ReplyBean replyBean) {
		System.out.println("BoardDAO - getListReply()");
		
		ArrayList<ReplyBean> replyList = new ArrayList<ReplyBean>();
		
		try {
			String sql = "select * from reply where movieSeq=? and re_ref=? ORDER BY date DESC";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, replyBean.getMovieSeq());
			pstmt.setInt(2, replyBean.getIdx());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReplyBean reply = new ReplyBean();
				reply.setIdx(rs.getInt("idx"));
				reply.setNick(rs.getString("nick"));
				reply.setMovieSeq(rs.getInt("movieSeq"));
				reply.setReply(rs.getString("reply"));
				reply.setRe_ref(rs.getInt("re_ref"));
				reply.setDate(rs.getDate("date"));
				reply.setReport(rs.getInt("report"));
				
				replyList.add(reply);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - getListReply() 에러: " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return replyList;
	}

	public int updateReply(ReplyBean replyBean) {
		System.out.println("BoardDAO - updateReply()");
		
		int insertCount = 0;
		
		try {
			String sql = "update reply set reply=? where idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, replyBean.getReply());
			pstmt.setInt(2, replyBean.getIdx());
			insertCount = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - updateReply() 에러: " + e.getMessage());
		} finally {
			close(pstmt);
		}
				
		return insertCount;
	}

	public int deleteReply(int idx) {
		System.out.println("BoardDAO - deleteReply()");
		
		int insertCount = 0;
		
		try {
			String sql = "delete from reply where idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			
			insertCount = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - deleteReply() 에러: " + e.getMessage());
		} finally {
			close(pstmt);
		}
				
		return insertCount;
	}

	public int reportReply(ReplyBean replyBean) {
		System.out.println("BoardDAO - reportReply()");
		
		int insertCount = 0;
		
		try {
			String sql = "update reply set report=report+1 where idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, replyBean.getIdx());
			insertCount = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - reportReply() 에러: " + e.getMessage());
		} finally {
			close(pstmt);
		}
				
		return insertCount;
	}

	public int selectReport(ReplyBean replyBean, int idx) {
		System.out.println("BoardDAO - selectReport()");
		
		int selectCount = 0;
		
		try {
			String sql = "SELECT * FROM replyReport WHERE nick=? AND re_ref=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, replyBean.getNick());
			pstmt.setInt(2, replyBean.getIdx());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
			} else {
				sql = "INSERT INTO replyReport VALUES(idx,?,?,now())";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, replyBean.getNick());
				pstmt.setInt(2, idx);
				
				selectCount = pstmt.executeUpdate();
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - selectReport() 에러: " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}
				
		return selectCount;
		
	}

	public int reviewLike(ReviewBean reviewBean) {
		System.out.println("BoardDAO - reportReply()");
		
		int insertCount = 0;
		
		try {
			String sql = "update review set like_count=like_count+1 where idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, reviewBean.getIdx());
			insertCount = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - likeReview() 에러: " + e.getMessage());
		} finally {
			close(pstmt);
		}
				
		return insertCount;
	}

	public int selectLike(ReviewBean reviewBean) {
		System.out.println("BoardDAO - selectReport()");
		
		int selectCount = 0;
		
		try {
			String sql = "SELECT * FROM reviewLike WHERE nick=? AND idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reviewBean.getNick());
			pstmt.setInt(2, reviewBean.getIdx());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
			} else {
				sql = "INSERT INTO reviewLike VALUES(?,?,now())";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, reviewBean.getIdx());
				pstmt.setString(2, reviewBean.getNick());
				
				selectCount = pstmt.executeUpdate();
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - selectLike() 에러: " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}
				
		return selectCount;
		
	}

	public int reviewReport(ReviewBean reviewBean) {
		System.out.println("BoardDAO - reviewReport()");
		
		int insertCount = 0;
		
		try {
			String sql = "update review set report=report+1 where idx=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, reviewBean.getIdx());
			insertCount = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - reviewReport() 에러: " + e.getMessage());
		} finally {
			close(pstmt);
		}
				
		return insertCount;
	}

	public int selectReport(ReviewBean reviewBean) {
		System.out.println("BoardDAO - selectReport()");
		
		int selectCount = 0;
		
		try {
			String sql = "SELECT * FROM reviewReport WHERE nick=? AND review_ref=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reviewBean.getNick());
			pstmt.setInt(2, reviewBean.getIdx());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
			} else {
				sql = "INSERT INTO reviewReport VALUES(idx,?,?,now())";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, reviewBean.getNick());
				pstmt.setInt(2, reviewBean.getIdx());
				
				selectCount = pstmt.executeUpdate();
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO - selectReport() 에러: " + e.getMessage());
		} finally {
			
			close(rs);
			close(pstmt);
		}
				
		return selectCount;
	}


}

package movie.dao;

import static db.JdbcUtil.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import board.vo.*;
import movie.vo.MovieBean;
import movie.vo.ReviewBean;

public class MovieDAO {
	private MovieDAO() {
	};

	private static MovieDAO instance;

	public static MovieDAO getInstance() {
		if (instance == null) {
			instance = new MovieDAO();
		}

		return instance;
	}

	Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}

	PreparedStatement pstmt;
	ResultSet rs;

	public int insertGrade(MovieBean movieBean) {
		int insertCount = 0;

		try {

			String sql = "SELECT grade from grade where nick = ? and movieSeq = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, movieBean.getNick());
			pstmt.setString(2, movieBean.getMovieSeq());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				sql = "update grade set grade = ? where nick = ? and movieSeq = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, movieBean.getMovieGrade());
				pstmt.setString(2, movieBean.getNick());
				pstmt.setString(3, movieBean.getMovieSeq());
				insertCount = -1;
				pstmt.executeUpdate();
			} else {
				sql = "INSERT INTO grade values(idx,?,?,?,?,?,?,?,?,?) ";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, movieBean.getNick());
				pstmt.setString(2, movieBean.getMovieGrade());
				pstmt.setString(3, movieBean.getMovieGenre());
				pstmt.setString(4, movieBean.getMovieSeq());
				pstmt.setString(5, movieBean.getMovieTitle());
				pstmt.setString(6, movieBean.getDirector());
				pstmt.setString(7, movieBean.getNation());
				pstmt.setString(8, movieBean.getMovieRuntime());
				pstmt.setString(9, movieBean.getMoviePoster());

				insertCount = pstmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return insertCount;
	}

	public int selectGrade(MovieBean movieBean) {
		int grade = 0;
		String sql = "SELECT grade from grade where nick = ? and movieSeq = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, movieBean.getNick());
			pstmt.setString(2, movieBean.getMovieSeq());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				grade = rs.getInt("grade");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return grade;
	}

	public String selectComment(MovieBean movieBean) {
		String comment = "";
		String sql = "SELECT content from review where nick = ? and movieSeq = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, movieBean.getNick());
			pstmt.setString(2, movieBean.getMovieSeq());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				comment = rs.getString("content");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return comment;
	}

	public int insertComment(ReviewBean reviewBean) {

		int insertCount = 0;

		try {

			String sql = "SELECT * from grade where nick =? and movieSeq =?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reviewBean.getNick());
			pstmt.setInt(2, reviewBean.getMovieSeq());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reviewBean.setGrade(rs.getInt("grade"));
				reviewBean.setGenre(rs.getString("genre"));
				reviewBean.setTitle(rs.getString("title"));
			}

			sql = "INSERT INTO review values(idx,?,?,?,?,?,?,?,0)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reviewBean.getNick());
			pstmt.setInt(2, reviewBean.getGrade());
			pstmt.setString(3, reviewBean.getGenre());
			pstmt.setInt(4, reviewBean.getMovieSeq());
			pstmt.setString(5, reviewBean.getTitle());
			pstmt.setString(6, reviewBean.getType());
			pstmt.setString(7, reviewBean.getContent());

			insertCount = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return insertCount;
	}

	public int deleteComment(ReviewBean reviewBean) {

		int deleteCount = 0;

		try {

			String sql = "delete from review where nick = ? and movieSeq = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reviewBean.getNick());
			pstmt.setInt(2, reviewBean.getMovieSeq());
			deleteCount = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return deleteCount;
	}

	public int updateComment(ReviewBean reviewBean) {
		int insertCount = 0;

		try {


			String sql = "update review set content = ? where nick =? and movieSeq = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reviewBean.getContent());
			pstmt.setString(2, reviewBean.getNick());
			pstmt.setInt(3, reviewBean.getMovieSeq());

			insertCount = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return insertCount;
	}

	public int insertReply(ReplyBean replyBean) {
		System.out.println("MovieDAO - insertReply()");
		
		int insertCount = 0;

		try {

			String sql = "SELECT * from reply where nick=? and movieSeq=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, replyBean.getNick());
			pstmt.setInt(2, replyBean.getMovieSeq());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				replyBean.setReply(rs.getString("reply"));
				replyBean.setReply_count(rs.getInt("reply_count"));
				
			}

			sql = "INSERT INTO reply values(idx,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, replyBean.getNick());
			pstmt.setInt(2, replyBean.getMovieSeq());
			pstmt.setString(3, replyBean.getReply());
			pstmt.setInt(4, replyBean.getReply_count());
			pstmt.setInt(5, replyBean.getRe_ref());
			pstmt.setInt(6, replyBean.getRe_seq());

			insertCount = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("insertReply() 오류 - " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return insertCount;
	}

}

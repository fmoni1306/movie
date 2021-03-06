package mypage.dao;

import static db.JdbcUtil.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import member.vo.MemberBean;
import movie.vo.MovieBean;
import mypage.vo.CollectionBean;
import mypage.action.Mypage;
import mypage.vo.MypageBean;
import mypage.vo.MypageGenreBean;
import mypage.vo.ProfileBean;

public class MypageDAO {

	private static MypageDAO instance;

	private MypageDAO() {
	}

	public static MypageDAO getInstance() {
		// 기존 MypageDAO 인스턴스가 없을 때만 생성하고, 있을 경우 생성하지 않음
		if (instance == null) {
			instance = new MypageDAO();
		}

		return instance;
	}

	// --------------------------------------------------------------
	// Service 클래스로부터 JdbcUtil 에서 제공받은 Connection 객체를 전달받기
	Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}

	// 내 정보 조회 - 세은
	public MemberBean selectMypageinfo(String nick) {
		System.out.println("MypageDAO - getMypageInfo 도착");
		System.out.println(nick);

		MemberBean memberBean = new MemberBean();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM member where nick=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				memberBean.setEmail(rs.getString("email"));
				memberBean.setNick(rs.getString("nick"));
				memberBean.setPhone(rs.getString("phone"));
			}

		} catch (SQLException e) {
			System.out.println("MemberDAO - getMypageInfo() 에러! " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return memberBean;

	}

	// 내 정보 수정 - 세은
	public void updateMypage(MemberBean memberBean) {
//		PreparedStatement pstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "update member set email=?, pass=?, phone=? where nick=?";
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, memberBean.getEmail());
			pstmt.setString(2, memberBean.getPass());
			pstmt.setString(3, memberBean.getPhone());
			pstmt.setString(4, memberBean.getNick());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 평가한 영화 개수 조회 메서드 - NHJ
	public int selectGradeListCount(String nick) {
		int listCount = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

//		PreparedStatement pstmt = null;
//		ResultSet rs = null;

		try {
			String sql = "SELECT COUNT(*) FROM grade where nick =?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();

			// 평가한 영화가 조회될 경우 (영화가 하나라도 존재할 경우)
			// => listCount 에 영화 수 저장
			if (rs.next()) {
				listCount = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("MypageDAO - selectGradeListCount() 에러!");
		} finally {
			close(rs);
			close(pstmt);
		}

		return listCount;
	}

	// 평가 영화 조회 메서드 - NHJ
	public ArrayList<MypageBean> selectGradeList(String nick) {
		ArrayList<MypageBean> gradeList = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM grade where nick=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();

			gradeList = new ArrayList<MypageBean>();
			while (rs.next()) {
				MypageBean list = new MypageBean();

				list.setIdx(rs.getInt("idx"));
				list.setGenre(rs.getString("genre"));
				list.setGrade(rs.getInt("grade"));
				list.setMovieSeq(rs.getInt("movieSeq"));
				list.setNick(rs.getString("nick"));
				list.setRuntime(rs.getInt("runtime"));
				list.setTitle(rs.getString("title"));
				list.setPoster(rs.getString("poster"));
				gradeList.add(list);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("MypageDAO - selectGradeList() 에러!");
		} finally {
			close(rs);
			close(pstmt);
		}

		return gradeList;
	}

	// 좋아요(wish) 리스트 조회 메서드 - 낙원
	public MypageBean selectWish(String nick, int movieSeq) {
		System.out.println("MypageDAO - selectWish()");
		MypageBean wishMovie = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM wish WHERE nick=? and wish='Y' and movieSeq=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			pstmt.setInt(2, movieSeq);
			rs = pstmt.executeQuery();
			MypageBean wishInfo = new MypageBean();

			wishMovie = new MypageBean();
			if (rs.next()) {
				wishInfo.setWish(rs.getString("wish"));
				wishInfo.setPoster(rs.getString("poster"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println("MypageDAO - selectWishMovie 에러!: " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}
		return wishMovie;
	}

	// 좋아요(wish) 리스트 조회 메서드 - 낙원
	public ArrayList<MypageBean> selectWishList(String nick) {
		System.out.println("MypageDAO - selectWishMovie()");
		ArrayList<MypageBean> wishMovie = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM wish WHERE nick=? and wish='Y'";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();

			wishMovie = new ArrayList<MypageBean>();
			while (rs.next()) {
				MypageBean wishInfo = new MypageBean();
				wishInfo.setIdx(rs.getInt("idx"));
				wishInfo.setNick(rs.getString("nick"));
				wishInfo.setMovieSeq(rs.getInt("movieSeq"));
				wishInfo.setTitle(rs.getString("title"));
				wishInfo.setWish(rs.getString("wish"));
				wishInfo.setPoster(rs.getString("poster"));
				wishMovie.add(wishInfo);
			}
		} catch (SQLException e) {
			System.out.println("MypageDAO - selectWishMovie 에러!: " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}
		return wishMovie;
	}

	// 좋아요 및 좋아요취소를 동시에하는 메서드(낙원:0917)
	// deleteWish()메서드 삭제(낙원:0917)
	public int changeWish(MypageBean mypageBean) {
		int completeCount = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String nick = mypageBean.getNick();
			int movieSeq = mypageBean.getMovieSeq();
			String title = mypageBean.getTitle();
			String poster = mypageBean.getPoster();
			String movieId = mypageBean.getMovieId();
			String sql = "SELECT * FROM wish where nick=? and movieSeq=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			pstmt.setInt(2, movieSeq);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sql = "DELETE FROM wish where nick=? and movieSeq=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, nick);
				pstmt.setInt(2, movieSeq);
				completeCount = pstmt.executeUpdate();
			} else {
				sql = "INSERT INTO wish VALUES(null,?,?,?,?,'Y')";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, nick);
				pstmt.setInt(2, movieSeq);
				pstmt.setString(3, title);
				pstmt.setString(4, poster);
				completeCount = pstmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return completeCount;
	}

	// ------------------------------------------------------------------- 별점 용 메서드
	// 태윤
	public ArrayList<MypageGenreBean> selectGener(String nick) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT grade,genre from grade where nick = ?";
		ArrayList<MypageGenreBean> list = new ArrayList<MypageGenreBean>();

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				return null;
			}
			
			while (rs.next()) {
				MypageGenreBean mgb = new MypageGenreBean();
				mgb.setGrade(rs.getInt("grade"));
				mgb.setGenre(rs.getString("genre"));
				list.add(mgb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}

	public ArrayList<MovieBean> selectTitle(String nick) {
		String sql = "SELECT grade,title from grade where nick = ?";
		ArrayList<MovieBean> list = new ArrayList<MovieBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MovieBean mb = new MovieBean();
				mb.setMovieGrade(rs.getString("grade"));
				mb.setMovieTitle(rs.getString("title"));
				list.add(mb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return list;
	}

	public JsonObject selectNation(String nick) {
		String sql = "SELECT nation, COUNT(*), SUM(grade) FROM grade where nick = ? GROUP BY nation HAVING COUNT(*) > 1 order by count(*) desc limit 0,10";
		JsonObject jo1 = new JsonObject();
		JsonObject jo2 = new JsonObject();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				JsonObject jo3 = new JsonObject();
				jo3.addProperty("nation", rs.getString(2));
				jo3.addProperty("avgGrade", rs.getString(3));
				jo2.add(rs.getString(1), jo3);
			}
			jo1.add(nick, jo2);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return jo1;
	}

	public JsonObject selectDirector(String nick) {
		String sql = "SELECT director,COUNT(*) FROM grade where nick = ? GROUP BY director HAVING COUNT(*) > 1 order by count(*) desc limit 0,10";
		JsonObject jo1 = new JsonObject();
		JsonObject jo2 = new JsonObject();
		int i = 1;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				JsonObject jo3 = new JsonObject();
				jo3.addProperty("director", rs.getString(1));
				jo2.add("No." + i++, jo3);
			}
			jo1.add(nick, jo2);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return jo1;
	}
	
	
//	Collection
	
	public int addCollectionMovie(CollectionBean collectionBean) {
		int isSuccess = 0;
		PreparedStatement pstmt = null;
//		ResultSet rs = null;
		  try {
			String sql = "insert into collection values(idx,?,'test',?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, collectionBean.getNick());
			pstmt.setString(2, collectionBean.getMovieSeq());
			pstmt.setString(3, collectionBean.getTitle());
			pstmt.setString(4, collectionBean.getPoster());
			
			isSuccess = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
//			close(rs);
			close(pstmt);
		}
		  
		  
		
		return isSuccess;
	}
	
	public int addCollection(CollectionBean collectionBean) {
		int isSuccess = 0;
		PreparedStatement pstmt = null;
//		ResultSet rs = null;
		  try {
			String sql = "insert into collection values(idx,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, collectionBean.getNick());
			pstmt.setString(2, collectionBean.getCollection_name());
			pstmt.setString(3, collectionBean.getMovieSeq());
			pstmt.setString(4, collectionBean.getTitle());
			pstmt.setString(5, collectionBean.getPoster());
			pstmt.setString(6, collectionBean.getContent());
			
			isSuccess = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
//			close(rs);
			close(pstmt);
		}
		  
		  
		
		return isSuccess;
	}
	
	public int updateCollection(CollectionBean collectionBean) {
		int isSuccess = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		  try {
			String sql = "update collection set movieSeq=?,title=?,poster=? where idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, collectionBean.getMovieSeq());
			pstmt.setString(2, collectionBean.getTitle());
			pstmt.setString(3, collectionBean.getPoster());
			pstmt.setInt(4, collectionBean.getIdx());
			
			isSuccess = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		  
		  
		
		return isSuccess;
	}
	
	public ArrayList<CollectionBean> selectCollection(String nick) {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<CollectionBean> list = new ArrayList<CollectionBean>();
		  try {
			String sql = "select * from collection where nick = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				CollectionBean cb = new CollectionBean();
				cb.setCollection_name(rs.getString("collection_name"));
				cb.setMovieSeq(rs.getString("movieSeq"));
				cb.setTitle(rs.getString("title"));
				cb.setPoster(rs.getString("poster"));
				cb.setContent(rs.getString("content"));
				cb.setIdx(rs.getInt("idx"));
				list.add(cb);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return list;
	}

	public int setMypage(String nick, ArrayList<String> list, Mypage type) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "SELECT * from mypage where nick =?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				switch (type) {
				case genre:
					sql = "UPDATE mypage set genre=? where nick =? ";
					break;
				case director:
					sql = "UPDATE mypage set director=? where nick =? ";
					break;
				case nation:
					sql = "UPDATE mypage set nation=? where nick =? ";
					break;
				}
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, list.toString());
				pstmt.setString(2, nick);
				result = pstmt.executeUpdate();
			} else {

				switch (type) {
				case genre:
					sql = "INSERT INTO mypage values(null,?,?,null,null) ";
					break;
				case director:
					sql = "INSERT INTO mypage values(null,?,null,null,?) ";
					break;
				case nation:
					sql = "INSERT INTO mypage values(null,?,null,?,null) ";
					break;
				}
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, nick);
				pstmt.setString(2, list.toString());
				result = pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return result;
	}

	public ArrayList<String> getInformation(String nick, Mypage type) {
		String sqlType = "";
		
		switch (type) {
		case genre:
			break;
		case nation:
			sqlType="nation";
			break;
		case director:
			sqlType="director";
			break;
		}

		String sql = "SELECT " + sqlType + ", COUNT(*) FROM grade where nick = ? GROUP BY " + sqlType
				+ " HAVING COUNT(*) > 1 order by count(*) desc limit 0,10";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				return null;
			}
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return list;
	}

	public int deleteCollection(int idx) {
		int isSuccess = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "delete from collection where idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			isSuccess = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return isSuccess;
	}

	public int updateProfile(ProfileBean profileBean) {
		int updateCount = -1;
		PreparedStatement pstmt = null;
//		ResultSet rs = null;
		String nick; // 닉네임
		String orgFileName; // 원본 파일 이름
		String uploadFileName; // 서버에 업로드된 파일 이름
		String savePath; // 업로드되는 경로
			try {
				nick=profileBean.getNick();
				orgFileName=profileBean.getOrgFileName();
				uploadFileName=profileBean.getUploadFileName();
				savePath=profileBean.getSavePath();
				System.out.println("DAO에 받아온 nick : " + nick);
				System.out.println("DAO에 받아온 orgFileName : " + orgFileName);
				System.out.println("DAO에 받아온 uploadFileName : " + uploadFileName);
				System.out.println("DAO에 받아온 savePath : " + savePath);
				String sql = "UPDATE profile SET orgFileName=?, uploadFileName=?, savePath=? where nick=?";
				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, orgFileName);
				pstmt.setString(2, uploadFileName);
				pstmt.setString(3, savePath);
				pstmt.setString(4, nick);

				updateCount = pstmt.executeUpdate();
				
				System.out.println("DAO updateCount : " + updateCount);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("프로필 업데이트 DAO 에러");
				e.printStackTrace();
			}finally {
//				close(rs);
				close(pstmt);
			}

		return updateCount;
	}

	public boolean selectProfile(ProfileBean profileBean) {
		boolean isSelect = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String nick = "";
		try {
			nick = profileBean.getNick();
			String sql = "SELECT * FROM profile where nick=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				isSelect = true;
			}

		} catch (SQLException e) {
			System.out.println("MemberDAO - selectProfile() 에러! " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return isSelect;
	}

	public int insertProfile(ProfileBean profileBean) {
		int isSuccess = 0;
		PreparedStatement pstmt = null;
		String nick; // 닉네임
		String orgFileName; // 원본 파일 이름
		String uploadFileName; // 서버에 업로드된 파일 이름
		String savePath; // 업로드되는 경로
//		ResultSet rs = null;
		  try {
			nick=profileBean.getNick();
		  	orgFileName=profileBean.getOrgFileName();
			uploadFileName=profileBean.getUploadFileName();
			savePath=profileBean.getSavePath();
			String sql = "insert into profile values(idx,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			pstmt.setString(2, orgFileName);
			pstmt.setString(3, uploadFileName);
			pstmt.setString(4, savePath);
			
			isSuccess = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
//			close(rs);
			close(pstmt);
		}
		  
		  
		
		return isSuccess;
	}

	public ProfileBean getProfile(String nick) {
		ProfileBean profileBean = new ProfileBean();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM profile where nick=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				profileBean.setOrgFileName(rs.getString("orgFileName"));
				profileBean.setUploadFileName(rs.getString("uploadFileName"));
				profileBean.setSavePath(rs.getString("savePath"));
			}

		} catch (SQLException e) {
			System.out.println("MemberDAO - getMypageInfo() 에러! " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return profileBean;
	}

}

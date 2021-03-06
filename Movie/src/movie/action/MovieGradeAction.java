package movie.action;

import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import action.Action;
import api.kmdbApi;
import vo.ActionForward;

public class MovieGradeAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MovieGradeAction");
		String type = request.getParameter("type");
		Random random = new Random();
		int rNum = random.nextInt(16);// 장르 선택을 랜덤하게 선택하기위한 난수생성 메서드
		StringBuffer sb = new StringBuffer(); // 메모리 할당을 줄이기위한 Buffer 메서드
		String genre = sb.append("코메디/느와르/범죄/드라마/로맨스/스릴러/전쟁/가족/판타지/액션/SF/애니메이션/인물/공포/미스터리/어드벤처/멜로").toString(); // 장르저장
		int createDts = 0;
		String[] getGenre = genre.split("/"); // 장르 스플릿

//		type = getGenre[rNum];

		if (type.equals("random")) {
			type = getGenre[rNum];
		}

//		int startCount = random.nextInt(28623)+1;
		int startCount = 0;
		String genre2 = "genre";
		switch (type) {
		case "코메디":
			startCount = random.nextInt(9298) + 1;
			break;
		case "느와르":
			startCount = random.nextInt(106) + 1;
			break;
		case "드라마":
			startCount = random.nextInt(27967) + 1;
			break;
		case "로맨스":
			startCount = random.nextInt(395) + 1;
			createDts = 1980;
			break;
		case "스릴러":
			startCount = random.nextInt(6024) + 1;
			break;
		case "전쟁":
			startCount = random.nextInt(1571) + 1;
			break;
		case "가족":
			startCount = random.nextInt(3001) + 1;
			break;
		case "판타지":
			startCount = random.nextInt(2962) + 1;
			break;
		case "액션":
			startCount = random.nextInt(9211) + 1;
			break;
		case "SF":
			startCount = random.nextInt(2572) + 1;
			break;
		case "애니메이션":
			startCount = random.nextInt(7027) + 1;
			genre2 = "type";
			break;
		case "인물":
			startCount = random.nextInt(1805) + 1;
			break;
		case "공포":
			startCount = random.nextInt(3464) + 1;
			break;
		case "범죄":
			startCount = random.nextInt(3994) + 1;
			break;
		case "미스터리":
			startCount = random.nextInt(1888) + 1;
			break;
		case "어드벤처":
			startCount = random.nextInt(3697) + 1;
			break;
		case "멜로":
			startCount = random.nextInt(395) + 1;
			createDts = 1980;
			break;
		}

//		System.out.println(getGenre[rNum]); // 랜덤인덱스 접근
		
		kmdbApi movie = new kmdbApi();
		String json = movie.getMovieByGenre(genre2, type, startCount,createDts);
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject) jsonParser.parse(json);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
//			System.out.println(jsonObject);
		System.out.println(jsonObject);
		out.print(jsonObject);
		out.flush();

		return null;

	}
}

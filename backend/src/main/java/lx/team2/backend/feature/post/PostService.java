package lx.team2.backend.feature.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

@Service
public class PostService {

	@Autowired
	PostDAO dao;
	
	@Autowired
	HttpSession session;
	
	// ========== 게시글 써지면 게시판 찾아다가 pk넣고 db insert ==========	
	@Transactional
	public boolean insertPostDB(PostRequestDTO dto) {
		
		// 게시판 카테고리 번호 넣기
		BoardVO board = 
				new BoardVO(dto.getRegionName(), 
						dto.getBoardCategoryName(),
						dto.getPostCategoryName());
		int boardNumber = dao.selectBoardNumber(board);
		
		// 유저 번호 넣기
		int userNumber = (Integer) session.getAttribute("userNumber");
		
		// post 객체 생성해서 db insert 하기
		PostVO post =
				new PostVO(dto.getTitle(),
						dto.getContent(),
						boardNumber,
						userNumber);
		
		int num = dao.insertPostDB(post);
		return (num == 1) ? true : false;
	}
	
	// ========== 게시글 list 뽑기 ( board 컨디션도 들어간 dto로 내보내야 함) ==========	
	@Transactional
	public List<PostRequestDTO> selectPostList() {
		return dao.selectPostList();
	}
	
	// ========== 게시글 id(number)로 상세정보 뽑기 (detail view) ==========	
	@Transactional
	public PostRequestDTO selectPostDetailByPostId(int postNumber) {
		// 세션 보면서 같은 사람은 30분에 한번씩 올라가도록 하면 좋을듯
		
		// 클릭할때마다 조회수 증가 dao
		dao.updateIncrementViewCount(postNumber);
		
		// postNumber로 상세정보 뽑아서 dto에 넣기
		PostRequestDTO dto = dao.selectPostDetailByPostId(postNumber);

		// 보는 유저가 추천을 눌렀는지 안눌렀는지 정보 보내기
		PostRecommendRequestDTO recommendDto = new PostRecommendRequestDTO();
		
		// 현재 게시글을 보는 유저 정보
		recommendDto.setPostNumber(postNumber);
		recommendDto.setUserNumber((Integer) session.getAttribute("userNumber"));
		dto.setRecommendedByMe(dao.selectPostRecommendByMe(recommendDto));
		
		return dto;
	}
	
	// ========== 게시글 추천 api ==========
	@Transactional
	public Map<String, Object> insertPostRecommend(PostRecommendRequestDTO dto) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 유저 번호 넣기
		dto.setUserNumber((Integer) session.getAttribute("userNumber"));
		
		// 추천되어있으면 추천 삭제로 가기
		if (dao.selectPostRecommendByMe(dto)) {
			dao.deletePostRecommend(dto);
			map.put("recommendedByMe", false);
			map.put("recommendCount", dao.selectRecommendCount(dto));
			return map;
		};
		
		// 추천 안했으면 추천하기
		dao.insertPostRecommend(dto);
		map.put("recommendedByMe", true);
		map.put("recommendCount", dao.selectRecommendCount(dto));
		return map;
	}
	
	
}

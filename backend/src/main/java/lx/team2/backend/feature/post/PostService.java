package lx.team2.backend.feature.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

	@Autowired
	PostDAO dao;
	
	// ========== 게시글 써지면 게시판 찾아다가 pk넣고 db insert ==========	
	@Transactional
	public boolean insertPostDB(PostRequestDTO dto) {

		BoardVO board = 
				new BoardVO(dto.getRegionName(), 
						dto.getBoardCategoryName(),
						dto.getPostCategoryName());
		int boardNumber = dao.selectBoardNumber(board);
		
		int userNumber = 1;
		
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
	
	// ========== 게시글 id로 상세정보 뽑기 (detail view) ==========	
	@Transactional
	public PostRequestDTO selectPostDetailByPostId(int id) {
		// 세션 보면서 같은 사람은 30분에 한번씩 올라가도록 하면 좋을듯
		dao.updateIncrementViewCount(id);
		return dao.selectPostDetailByPostId(id);
	}
	
	
}

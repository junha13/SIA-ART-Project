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
				new BoardVO(dto.getBoard().getRegionName(), 
						dto.getBoard().getBoardCategoryName(),
						dto.getBoard().getPostCategoryName());
		int boardNumber = dao.selectBoardNumber(board);
		
		int userNumber = 1;
		
		PostVO post =
				new PostVO(dto.getPost().getTitle(),
						dto.getPost().getContent(),
						boardNumber,
						userNumber);
		
		int num = dao.insertPostDB(post);
		return (num == 1) ? true : false;
	}
	
	// ========== 게시글 써지면 게시판 찾아다가 pk넣고 db insert ==========	
	@Transactional
	public List<PostVO> selectPostList() {
		
		List<PostVO> list = dao.selectPostList();
		return list;
	}
	
	
}

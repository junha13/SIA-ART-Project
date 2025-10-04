package lx.team2.backend.feature.post;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostDAO {

	@Autowired
	SqlSession session;
	
	/*
	 * ========== 게시글 insert ==========
	 */
	public int insertPostDB(PostVO post) {
		return session.insert("insertPostDB", post);
	}
	
	/*
	 * ========== board number select ( 게시글 insert 하려면 필요함 ) ==========
	 */
	public int selectBoardNumber(BoardVO board) {
		return session.selectOne("selectBoardNumber", board);
	}
	
	/*
	 * ========== 게시글 리스트 뽑기 ( listview onmounted 시 필요 ) ==========
	 */
	public List<PostRequestDTO> selectPostList() {
		return session.selectList("selectPostList");
	}
	
	/*
	 * ========== 게시글 id로 상세정보 뽑기 (detail view) ==========
	 */
	public PostRequestDTO selectPostDetailByPostId(int PostNumber) {
		return session.selectOne("selectPostDetailByPostId", PostNumber);
	}
	
	/*
	 * ========== 게시글 들어갈때마다 조회수 1 증가 (detail view) ==========
	 */
	public int updateIncrementViewCount(int PostNumber) {
		return session.update("updateIncrementViewCount", PostNumber);
	}
}

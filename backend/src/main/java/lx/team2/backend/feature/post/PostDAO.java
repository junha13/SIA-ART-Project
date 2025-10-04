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
	public List<PostVO> selectPostList() {
		return session.selectList("selectPostList");
	}
}

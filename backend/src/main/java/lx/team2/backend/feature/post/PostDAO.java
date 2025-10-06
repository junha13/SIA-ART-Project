package lx.team2.backend.feature.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
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
	
	/*
	 * ========== 유저 로그인아이디로 유저 pk뽑기 ==========
	 */
	public int selectUserNumberByUserLoginId(String userLoginId) {
		return session.selectOne("selectUserNumberByUserLoginId", userLoginId);
	}
	
	/*
	 * ========== 보는 사람이 추천 햇는지 안햇는지 보기 ==========
	 */
	public boolean selectPostRecommendByMe(PostRecommendRequestDTO recommendDto) {
		return session.selectOne("selectPostRecommendByMe", recommendDto);
	}
	
	/*
	 * ========== 추천수 올리기 ==========
	 */
	public int insertPostRecommend(PostRecommendRequestDTO recommendDto) {
		return session.insert("insertPostRecommend", recommendDto);
	}
	/*
	 * ========== 추천수 내리기 ==========
	 */
	public int deletePostRecommend(PostRecommendRequestDTO recommendDto) {
		return session.insert("deletePostRecommend", recommendDto);
	}
	/*
	 * ========== 추천수 보기 ==========
	 */
	public int selectRecommendCount(PostRecommendRequestDTO recommendDto) {
		return session.selectOne("selectRecommendCount", recommendDto);
	}
	/*
	 * ========== 댓글 insert 하기 ==========
	 */
	public int insertPostCommentDB(PostCommentRequestDTO commentDto) {
		return session.insert("insertPostCommentDB", commentDto);
	}
	/*
	 * ========== 댓글 list 뽑기 ==========
	 */
	public List<PostCommentRequestDTO> selectCommentList(int postNumber) {
		return session.selectList("selectCommentList", postNumber);
	}
}

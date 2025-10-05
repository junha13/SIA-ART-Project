// src/main/java/.../PostDAO.java

package lx.team2.backend.feature.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // @Param 필요

@Mapper // @Repository와 SqlSession 주입 코드를 제거하고 이것만 남깁니다.
public interface PostDAO {

    /*
     * ========== 게시글 insert ==========
     */
    int insertPostDB(PostVO post); // 메서드 이름과 XML ID가 일치해야 함

    /*
     * ========== board number select ==========
     */
    int selectBoardNumber(BoardVO board);

    /*
     * ========== 게시글 리스트 뽑기 ==========
     */
    List<PostRequestDTO> selectPostList();

    /*
     * ========== 게시글 id로 상세정보 뽑기 ==========
     */
    PostRequestDTO selectPostDetailByPostId(int PostNumber);

    /*
     * ========== 게시글 들어갈때마다 조회수 1 증가 ==========
     */
    int updateIncrementViewCount(int PostNumber);
}
package lx.team2.backend.feature.post;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/post")
@RestController
public class PostController {
	
	@Autowired
	PostService service;
	
	// ========== db insert ==========
	@RequestMapping("/post")
	public ResponseEntity<?> insertPostDB(@RequestBody PostRequestDTO dto) {
		boolean result = service.insertPostDB(dto);
		return ResponseEntity
				.ok()
				.header("api", "post/post")
				.body(Map.of("result", result));
	}
	
	// ========== postListview 초기 onmounted list 보여주기 ==========
	@RequestMapping("/getPostList")
	public ResponseEntity<?> selectPostList() {
		List<PostRequestDTO> list = service.selectPostList();
		return ResponseEntity
				.ok()
				.header("api", "post/post")
				.body(Map.of("result", list));
	}
	
	// ========== 각 게시글 디테일 화면 보여주기 ==========
	@RequestMapping("/getPostDetail/{id}")
	public ResponseEntity<?> selectPostDetail(@PathVariable int id) {
		PostRequestDTO post = service.selectPostDetailByPostId(id);
		return ResponseEntity
				.ok()
				.header("api", "post/post")
				.body(Map.of("result", post));
	}
	

}

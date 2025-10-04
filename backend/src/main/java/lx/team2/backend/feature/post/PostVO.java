package lx.team2.backend.feature.post;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostVO {
	private int postNumber;
	private String title;
	private String content;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm") // 응답을 분까지만 보이게
	private LocalDateTime createdAt;
	
	private int boardNumber;
	private int userNumber;
	
	// DB 추가해야됨
	private String tags;
	
	PostVO(String title, String content, int boardNumber, int userNumber) {
		this.title = title;
		this.content = content;
		this.boardNumber = boardNumber;
		this.userNumber = userNumber;
	}
}

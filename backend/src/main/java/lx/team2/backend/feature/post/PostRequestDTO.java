package lx.team2.backend.feature.post;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostRequestDTO {
	private int postNumber;
	private String title;
	private String content;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm") // 응답을 분까지만 보이게
	private LocalDateTime createdAt;
	
	private int boardNumber;
	private int userNumber;
	
	private int viewCount;
	
	// DB 추가해야됨
	private String tags;

    // board 컨디션
    private String regionName;
    private String boardCategoryName;
    private String postCategoryName;
    
    // user name (활동명)
    private String userName;
    
    // 추천
    private int recommendCount;    // 총 추천 수
    //private boolean recommendedByMe; // 내가 추천했는지 (viewer 기준)
}

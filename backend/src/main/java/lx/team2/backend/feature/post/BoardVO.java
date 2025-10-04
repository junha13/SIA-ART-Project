package lx.team2.backend.feature.post;

import lombok.Data;

@Data
public class BoardVO {
	private int boardNumber;
	private String regionName;
	private String boardCategoryName;
	private String postCategoryName;
	
	BoardVO(String regionName, String boardCategoryName, String postCategoryName) {
		this.regionName = regionName;
		this.boardCategoryName = boardCategoryName;
		this.postCategoryName = postCategoryName;
	}

}

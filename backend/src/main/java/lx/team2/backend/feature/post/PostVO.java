package lx.team2.backend.feature.post;

import lombok.Data;

@Data
public class PostVO {
	private int publicArtNo;
	private String title;
	private String Content;
	private String image;
	private double latitude;
	private double longitude;
	private int categoryNo;
}

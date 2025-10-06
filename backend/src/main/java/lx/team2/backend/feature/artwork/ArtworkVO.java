package lx.team2.backend.feature.artwork;

import lombok.Data;

@Data
public class ArtworkVO {
    private Long artworkNumber;
    private String artworkTitle;
    private String artworkDescription;
    private String artworkImage;
    private Integer yearOfArtwork;
    private Long userNumber;
    private Integer workCategoryNumber;
    private Double lat;
    private Double lng;
}

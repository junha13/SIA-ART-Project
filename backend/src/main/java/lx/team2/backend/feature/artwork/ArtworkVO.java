package lx.team2.backend.feature.artwork;

import lombok.Data;

@Data
public class ArtworkVO {
    private Long id;
    private String title;
    private String imagePath;
    private Double lat;
    private Double lng;
}

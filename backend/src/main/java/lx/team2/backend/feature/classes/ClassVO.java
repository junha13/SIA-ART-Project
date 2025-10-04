package lx.team2.backend.feature.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ClassVO {
    private int id;
    private String name;
    private String description;
    private String image;
    private int userNumber;
    private Point location;
    private int categoryNumber;

}
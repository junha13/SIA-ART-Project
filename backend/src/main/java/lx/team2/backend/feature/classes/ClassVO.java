package lx.team2.backend.feature.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ClassVO {
    private int classNumber;
    private String className;
    private String classDescription;
    private String classImage;
    private int userNumber;
    private Point location;
    private int classCategoryNumber;
    private Date startDate;
    private Date endDate;
    private boolean isDaily;
    private int durationWeeks;
    private String classTime;
    // ✨ 검색/필터링을 위한 필드
    private String categoryName; // 카테고리 이름 필터링 (tb_class_category 조인 필요)
    private String searchKeyword; // 검색어 (제목, 위치 등에 사용)
}
package lx.team2.backend.feature.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassVO {
    // 기본 정보
    private int classNumber;
    private String className;
    private String subtitle;
    private String classDescription;
    private String classImage;
    private int userNumber;
    private String instructor;
    private String instructorAvatar;

    // 상세 정보
    private String level;
    private int capacity;
    private int price;
    private boolean materialsIncluded;
    private String curriculum;

    // 일정/기간 정보
    private Date startDate;
    private Date endDate;
    private String classTime;
    private String days;
    private int durationWeeks;
    private boolean isDaily;

    // 외래 키 및 공간 데이터
    private int classCategoryNumber;
    private Point location;

    // --- MyBatis 조인 결과 또는 검색 파라미터를 위한 필드 ---
    private String categoryName; // JOIN 결과
    private String searchKeyword; // 검색 파라미터
}
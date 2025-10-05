package lx.team2.backend.feature.classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
public class ClassDTO {

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL) // JSON으로 변환 시 null인 필드는 제외
    public static class DetailResponse {
        // ClassDetailView.vue에서 필요한 정보들
        private int id;
        private String title;
        private String subtitle; // DB에 추가 필요
        private String description;
        private String image;
        private String category;
        private String level; // DB에 추가 필요
        private String instructor; // user 테이블과 조인 또는 DB에 추가
        private String instructorAvatar; // user 테이블과 조인 또는 DB에 추가
        private String location; // Point -> "서울시 구로구" 형태의 문자열로 변환 필요
        private String days; // DB에 추가 필요
        private int capacity; // DB에 추가 필요
        private long price; // DB에 추가 필요
        private boolean materialsIncluded; // DB에 추가 필요
        private List<String> curriculum; // DB에 추가 필요 (별도 테이블 또는 TEXT)

        // ClassDTO.java 파일 내부의 from 메서드
        public static DetailResponse from(ClassVO vo) {
            log.info("DetailResponse 호출됨");
            // Point 객체를 WKT 문자열로 변환
            String locationStr = (vo.getLocation() != null) ? vo.getLocation().toText() : "위치 정보 없음";

            // curriculum 필드를 줄바꿈 기준으로 분리하여 List<String>으로 변환
            List<String> curriculumList = (vo.getCurriculum() != null && !vo.getCurriculum().isEmpty())
                    ? List.of(vo.getCurriculum().split("\n"))
                    : List.of();

            return DetailResponse.builder()
                    .id(vo.getClassNumber())
                    .title(vo.getClassName())
                    .subtitle(vo.getSubtitle()) // 임시값 제거
                    .description(vo.getClassDescription())
                    .image(vo.getClassImage())
                    .category(vo.getCategoryName())
                    .level(vo.getLevel()) // 임시값 제거
                    .instructor(vo.getInstructor()) // 임시값 제거
                    .instructorAvatar(vo.getInstructorAvatar()) // 임시값 제거
                    .location(locationStr)
                    .days(vo.getDays()) // 임시값 제거
                    .capacity(vo.getCapacity()) // 임시값 제거
                    .price(vo.getPrice()) // 임시값 제거
                    .materialsIncluded(vo.isMaterialsIncluded()) // 임시값 제거
                    .curriculum(curriculumList) // 임시값 제거 및 변환된 리스트 사용
                    .build();
        }

    }
    /**
     * 클래스 목록 조회를 위한 DTO
     */
    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ListResponse {
        private int classNumber;
        private String className;
        private String classImage;
        private String subtitle;
        private String instructor;
        private int durationWeeks;
        private String location; // WKT 문자열로 전달

        // VO를 ListResponse DTO로 변환하는 정적 팩토리 메서드
        public static ListResponse from(ClassVO vo) {
            log.info("ListResponse 호출됨");
            String locationStr = (vo.getLocation() != null) ? vo.getLocation().toText() : "위치 정보 없음";

            return ListResponse.builder()
                    .classNumber(vo.getClassNumber())
                    .className(vo.getClassName())
                    .classImage(vo.getClassImage())
                    .subtitle(vo.getSubtitle())
                    .instructor(vo.getInstructor())
                    .durationWeeks(vo.getDurationWeeks())
                    .location(locationStr)
                    .build();
        }
    }
}
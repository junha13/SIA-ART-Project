package lx.team2.backend.feature.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어줍니다.
public class ClassService {

    private final ClassDAO classDAO; // MyBatis Mapper 인터페이스 주입

    /**
     * 클래스 상세 정보를 조회합니다.
     * @param classNumber 조회할 클래스의 ID
     * @return 클래스 상세 정보 DTO
     */
    public ClassDTO.DetailResponse getClassDetail(int classNumber) {
        // 1. DAO를 통해 DB에서 ClassVO 객체를 가져옵니다.
        ClassVO classVO = classDAO.selectClassDetail(classNumber);

        if (classVO == null) {
            // 예외 처리: 해당 ID의 클래스가 없을 경우
            // 여기서는 간단히 null을 반환하지만, 실제로는 사용자 정의 예외를 던지는 것이 좋습니다.
            return null;
        }

        // 2. VO 객체를 DTO 객체로 변환하여 반환합니다.
        return ClassDTO.DetailResponse.from(classVO);
    }

    /**
     * 필터링 조건에 맞는 전체 클래스 목록을 조회합니다.
     * @param category 카테고리 이름
     * @param query 검색어
     * @return 클래스 목록 DTO 리스트
     */
    public List<ClassDTO.ListResponse> getAllClasses(String category, String query) {
        // 1. 검색 조건을 담을 VO 객체 생성
        ClassVO params = new ClassVO();
        params.setCategoryName(category); // Mybatis Mapper에서 사용할 카테고리 이름
        params.setSearchKeyword(query);   // Mybatis Mapper에서 사용할 검색어

        // 2. DAO를 통해 데이터베이스에서 List<ClassVO>를 가져옴
        List<ClassVO> voList = classDAO.selectClasses(params);

        // 3. Stream API를 사용해 List<ClassVO>를 List<ClassDTO.ListResponse>로 변환
        return voList.stream()
                .map(ClassDTO.ListResponse::from)
                .collect(Collectors.toList());
    }
}
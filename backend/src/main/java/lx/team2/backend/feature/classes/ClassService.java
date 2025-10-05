package lx.team2.backend.feature.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("getClassDetail 호출됨");
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
        log.info("getAllClasses 호출됨: category={}, query={}", category, query);
        // 1. 검색 조건을 담을 VO 객체 생성
        ClassVO params = new ClassVO();
        params.setCategoryName(category); // Mybatis Mapper에서 사용할 카테고리 이름
        params.setSearchKeyword(query);   // Mybatis Mapper에서 사용할 검색어

        // 2. DAO를 통해 데이터베이스에서 List<ClassVO>를 가져옴
        List<ClassVO> voList = classDAO.selectClasses(params);

        // Stream을 사용해 DTO 리스트로 변환
        List<ClassDTO.ListResponse> dtoList = voList.stream()
                .map(ClassDTO.ListResponse::from) // 여기서 각 DTO의 from이 호출됨
                .collect(Collectors.toList());

        // 👇 DTO 변환이 끝난 후, 결과 리스트의 크기를 한 번만 로그로 남긴다.
        log.info("{}개의 클래스 DTO 변환 완료.", dtoList.size());

        return dtoList;
    }
}
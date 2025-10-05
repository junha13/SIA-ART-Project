package lx.team2.backend.feature.classes;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List; // java.util.List 임포트 필요

@Mapper
public interface ClassDAO {
    // 클래스 목록을 조회하고 List<ClassVO> 형태로 반환
    List<ClassVO> selectClasses(ClassVO vo);

    // 클래스 상세 조회를 위한 메서드
    ClassVO selectClassDetail(int classNumber);
    // ✨ 1단계: 메인 카테고리 번호로 상세 키워드 이름 목록 조회 (프론트엔드 드롭다운 채우기용)
    List<String> selectDetailKeywordsByMainCategory(int classCategoryNumber);

    // ✨ 2단계: 신규 클래스 등록 (useGeneratedKeys 설정 필요)
    void insertClass(ClassVO vo);

    // ✨ 3단계: 클래스-키워드 관계 설정
    void insertClassKeywordLink(@Param("classNumber") int classNumber, @Param("keywordNumber") int keywordNumber);

    // (선택 사항: 키워드 이름으로 번호를 찾는 메서드도 서비스 레이어에서 필요)
    int selectKeywordNumberByName(String keywordName);
}
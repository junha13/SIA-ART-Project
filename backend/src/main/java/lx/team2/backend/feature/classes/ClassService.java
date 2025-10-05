package lx.team2.backend.feature.classes;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassDAO classDAO;
    private final RestTemplate restTemplate; // <<얘 때문에 RestTemplateConfig생김 알아서 인식못해서 config에서 bin으로 인식시켜줘야함

    // application.properties에서 설정한 Naver API 키 주입
    @Value("${naver.api.clientId}")
    private String naverClientId;
    @Value("${naver.api.clientSecret}")
    private String naverClientSecret;

    // --- 클래스 상세 정보 조회 ---
    public ClassDTO.DetailResponse getClassDetail(int classNumber) {
        log.info("[Service] 클래스 상세 정보 조회 시작: classNumber={}", classNumber);
        ClassVO classVO = classDAO.selectClassDetail(classNumber);
        if (classVO == null) {
            log.warn("[Service] 클래스를 찾을 수 없음: classNumber={}", classNumber);
            return null;
        }

        // DB에서 가져온 Point 객체를 주소 문자열로 변환
        String address = reverseGeocode(classVO.getLocation());
        // 변환된 주소와 함께 DTO 생성
        return ClassDTO.DetailResponse.from(classVO, address);
    }

    // --- 클래스 전체 목록 조회 ---
    public List<ClassDTO.ListResponse> getAllClasses(String category, String query) {
        log.info("[Service] 전체 클래스 목록 조회 시작: category={}, query={}", category, query);
        ClassVO params = new ClassVO();
        params.setCategoryName(category);
        params.setSearchKeyword(query);

        List<ClassVO> voList = classDAO.selectClasses(params);

        // ⚠️ 성능 주의: 목록의 각 항목마다 API를 호출하므로, 항목이 많으면 느려질 수 있습니다.
        List<ClassDTO.ListResponse> dtoList = voList.stream()
                .map(vo -> {
                    // 각 클래스의 Point 객체를 주소 문자열로 변환
                    String address = reverseGeocode(vo.getLocation());
                    // 변환된 주소와 함께 DTO 생성
                    return ClassDTO.ListResponse.from(vo, address);
                })
                .collect(Collectors.toList());

        log.info("[Service] {}개의 클래스 DTO 변환 완료.", dtoList.size());
        return dtoList;
    }

    /**
     * Naver Maps API를 호출하여 좌표를 주소로 변환하는 메서드
     */
    private String reverseGeocode(Point point) {
        if (point == null) {
            return "위치 정보 없음";
        }

        // Naver Reverse Geocoding API URL
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";

        // URL과 파라미터 생성
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("coords", point.getX() + "," + point.getY()) // 경도,위도 순서
                .queryParam("output", "json")
                .queryParam("orders", "roadaddr") // 도로명 주소 우선
                .build(true)
                .toUri();

        // HTTP 헤더에 Naver API 인증 정보 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
        headers.set("X-NCP-APIGW-API-KEY", naverClientSecret);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            log.info("[Geocoding] Naver API 호출: {}", uri);
            // API 호출 및 응답 받기
            ResponseEntity<JsonNode> response = restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode results = response.getBody().path("results");
                if (results.isArray() && results.size() > 0) {
                    // 응답받은 JSON에서 주소 정보 추출
                    JsonNode region = results.get(0).path("region");
                    String area1 = region.path("area1").path("name").asText(); // 시/도
                    String area2 = region.path("area2").path("name").asText(); // 시/군/구

                    // 도로명 주소가 있는 경우 도로명 + 건물번호 사용
                    JsonNode land = results.get(0).path("land");
                    String roadName = land.path("name").asText();
                    String buildingNumber = land.path("number1").asText();

                    String address = String.format("%s %s %s %s", area1, area2, roadName, buildingNumber).trim();
                    log.info("[Geocoding] 변환 성공: {} -> {}", point.toText(), address);
                    return address;
                }
            }
        } catch (Exception e) {
            log.error("[Geocoding] API 호출 중 에러 발생: {}", e.getMessage(), e);
        }

        return "주소 변환 실패";
    }
}
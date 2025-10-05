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
    private final RestTemplate naverRestTemplate; // <<얘 때문에 RestTemplateConfig생김 알아서 인식못해서 config에서 bin으로 인식시켜줘야함

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
            log.warn("[Geocoding] Point가 null입니다.");
            return "위치 정보 없음";
        }

        // Naver Reverse Geocoding API URL
        String apiUrl = "https://maps.apigw.ntruss.com/map-reversegeocode/v2/gc";

        // URL과 파라미터 생성 (⚠ 경도, 위도 순서)
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("coords", point.getX() + "," + point.getY()) // 경도, 위도
                .queryParam("output", "json")
                .queryParam("orders", "roadaddr") // 도로명 주소 우선
                .build(true)
                .toUri();

        log.info("[Geocoding] 요청 URI: {}", uri);

        try {
            // 인터셉터에서 Key를 붙이므로 HttpEntity 불필요
            ResponseEntity<JsonNode> response = naverRestTemplate.getForEntity(uri, JsonNode.class);
            log.info("[Geocoding] HTTP 상태 코드: {}", response.getStatusCode());

            JsonNode body = response.getBody();
            if (body == null) {
                log.error("[Geocoding] API 응답이 null입니다.");
                return "주소 변환 실패";
            }

            log.debug("[Geocoding] API 응답 JSON:\n{}", body.toPrettyString());

            JsonNode results = body.path("results");
            if (!results.isArray() || results.size() == 0) {
                log.warn("[Geocoding] results 배열이 비어있거나 잘못됨.");
                return "주소 변환 실패";
            }

            JsonNode firstResult = results.get(0);
            JsonNode region = firstResult.path("region");
            String area1 = region.path("area1").path("name").asText(""); // 시/도
            String area2 = region.path("area2").path("name").asText(""); // 시/군/구

            JsonNode land = firstResult.path("land");
            String roadName = land.path("name").asText("");             // 도로명
            String buildingNumber = land.path("number1").asText("");     // 건물번호

            String address = String.format("%s %s %s %s", area1, area2, roadName, buildingNumber).trim();
            log.info("[Geocoding] 변환 성공: {} -> {}", point.toText(), address);
            return address;

        } catch (Exception e) {
            log.error("[Geocoding] API 호출 중 예외 발생: {}", e.getMessage(), e);
            return "주소 변환 실패";
        }
    }

}
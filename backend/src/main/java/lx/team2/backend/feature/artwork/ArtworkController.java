package lx.team2.backend.feature.artwork;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/artworks")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkService artworkService;

    /**
     * 작품 등록 API
     * multipart/form-data 형태로 작품 정보(json)와 이미지 파일을 함께 받습니다.
     */
    @PostMapping
    public ResponseEntity<ArtworkVO> upload(
            @RequestPart("artwork") ArtworkVO artworkVO,
            @RequestPart("image") MultipartFile image
    ) {
        ArtworkVO savedArtwork = artworkService.saveArtwork(artworkVO, image);
        // 생성된 객체 정보(ID 포함)를 클라이언트에 반환
        return ResponseEntity.ok(savedArtwork);
    }

    /**
     * 지도 마커 검색 API (핵심 기능)
     * 예: /api/artworks/search?lat=37.5665&lng=126.9780&radius=5000
     */
    @GetMapping("/search")
    public ResponseEntity<List<ArtworkVO>> searchNearbyArtworks(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "5000") Double radius // 기본 반경 5km
    ) {
        List<ArtworkVO> artworks = artworkService.selectMarker(lat, lng, radius);
        return ResponseEntity.ok(artworks);
    }

    /**
     * 작품 상세 정보 조회 API
     * 예: /api/artworks/101
     */
    @GetMapping("/{artworkNumber}")
    public ResponseEntity<ArtworkVO> getArtworkDetails(
            @PathVariable long artworkNumber
    ) {
        ArtworkVO artwork = artworkService.selectArtwork(artworkNumber);
        return ResponseEntity.ok(artwork);
    }

    /**
     * 작품 정보 수정 API
     * 예: PUT /api/artworks/101
     */
    @PutMapping("/{artworkNumber}")
    public ResponseEntity<String> updateArtwork(
            @PathVariable long artworkNumber,
            @RequestBody ArtworkVO artworkVO
            // TODO: 실제로는 @AuthenticationPrincipal 등으로 로그인 사용자 정보(currentUserId)를 받아야 함
    ) {
        artworkVO.setArtworkNumber(artworkNumber); // URL의 ID를 VO에 설정
        long currentUserId = 1L; // 임시 사용자 ID
        artworkService.updateArtwork(artworkVO, currentUserId);
        return ResponseEntity.ok("수정 완료");
    }

    /**
     * 작품 삭제 API
     * 예: DELETE /api/artworks/101
     */
    @DeleteMapping("/{artworkNumber}")
    public ResponseEntity<String> deleteArtwork(
            @PathVariable long artworkNumber
            // TODO: 실제로는 @AuthenticationPrincipal 등으로 로그인 사용자 정보(currentUserId)를 받아야 함
    ) {
        long currentUserId = 1L; // 임시 사용자 ID
        artworkService.deleteArtwork(artworkNumber, currentUserId);
        return ResponseEntity.ok("삭제 완료");
    }
}
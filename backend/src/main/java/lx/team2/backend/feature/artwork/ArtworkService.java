package lx.team2.backend.feature.artwork;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkMapper artworkMapper;

    // 이미지 파일이 저장될 실제 경로 (프로젝트 루트 기준)
    private final String uploadPath = Paths.get("uploads").toAbsolutePath().toString();

    /**
     * 작품 정보를 DB에 저장하고 파일을 업로드합니다.
     * @return 저장 완료된 ArtworkVO (생성된 ID 포함)
     */
    public ArtworkVO saveArtwork(ArtworkVO artworkVO, MultipartFile image) {
        // 1. 파일 저장 로직
        String savedFilename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        File dest = new File(uploadPath, savedFilename);

        // uploads 디렉토리가 없으면 생성
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            image.transferTo(dest);
        } catch (IOException e) {
            // TODO: 좀 더 구체적인 예외 처리 필요
            throw new RuntimeException("이미지 저장에 실패했습니다.", e);
        }

        // 2. ArtworkVO에 파일 경로 및 추가 정보 설정
        artworkVO.setArtworkImage("/uploads/" + savedFilename); // 웹에서 접근 가능한 경로

        // TODO: 실제로는 SecurityContext에서 로그인한 사용자 정보를 가져와 userNumber를 설정해야 합니다.
        // 예시로 임시 사용자 번호(1L)를 하드코딩합니다.
        artworkVO.setUserNumber(1L); 

        // 3. DB에 저장 (mapper의 insertArtwork 호출)
        artworkMapper.insertArtwork(artworkVO);

        // useGeneratedKeys 설정에 의해 artworkVO 객체에 artworkNumber가 자동으로 채워짐
        return artworkVO;
    }

    /**
     * 지도 마커 검색: 특정 지점 반경 내의 작품을 검색합니다.
     */
    public List<ArtworkVO>selectMarker(Double lat, Double lng, Double radius) {
        // Mapper에 파라미터를 전달하기 위해 Map 사용
        Map<String, Object> params = Map.of(
            "lat", lat,
            "lng", lng,
            "radius", radius // 미터(m) 단위
        );
        // mapper의 selectMarker 호출
        return artworkMapper.selectMarker(params);
    }

    /**
     * 작품 상세 정보 조회
     */
    public ArtworkVO selectArtwork(long artworkNumber) {
        // mapper의 selectArtwork 호출
        return artworkMapper.selectArtwork(artworkNumber);
    }

    /**
     * 작품 정보 수정 (권한 확인 로직 포함)
     */
    public int updateArtwork(ArtworkVO artworkVO, /* UserDetailsImpl currentUser */ long currentUserId) {
        // 실제로는 SecurityContext 등에서 로그인한 사용자 정보를 가져와야 합니다.
        ArtworkVO originalArtwork = artworkMapper.selectArtwork(artworkVO.getArtworkNumber());
        
        // 관리자 또는 작성자 본인만 수정 가능
        if (currentUserId == 1L /* isAdmin */ || originalArtwork.getUserNumber() == currentUserId) {
            return artworkMapper.updateArtwork(artworkVO);
        } else {
            throw new RuntimeException("수정 권한이 없습니다."); // AccessDeniedException 등 사용 권장
        }
    }

    /**
     * 작품 삭제 (권한 확인 로직 포함)
     */
    public int deleteArtwork(long artworkNumber, /* UserDetailsImpl currentUser */ long currentUserId) {
        ArtworkVO artwork = artworkMapper.selectArtwork(artworkNumber);

        // 관리자 또는 작성자 본인만 삭제 가능
        if (currentUserId == 1L /* isAdmin */ || artwork.getUserNumber() == currentUserId) {
            return artworkMapper.deleteArtwork(artworkNumber);
        } else {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
    }
}
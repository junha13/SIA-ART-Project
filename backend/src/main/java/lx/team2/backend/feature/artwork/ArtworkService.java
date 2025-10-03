package lx.team2.backend.feature.artwork;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtworkService {

	@Autowired
    private ArtworkMapper artworkMapper;

    public void saveArtwork(String title, Double lat, Double lng, MultipartFile image) {
        String uploadPath = "uploads/";
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        File file = new File(uploadPath + fileName);

        try {
            image.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패");
        }

        ArtworkVO vo = new ArtworkVO();
        vo.setTitle(title);
        vo.setLat(lat);
        vo.setLng(lng);
        vo.setImagePath("/uploads/" + fileName);
        artworkMapper.insertArtwork(vo);
    }

    public List<ArtworkVO> getAllArtworks() {
        return artworkMapper.selectAllArtworks();
    }
}

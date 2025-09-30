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

    @PostMapping
    public ResponseEntity<?> upload(
            @RequestParam String title,
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam MultipartFile image
    ) {
        artworkService.saveArtwork(title, lat, lng, image);
        return ResponseEntity.ok("업로드 완료");
    }

    @GetMapping
    public ResponseEntity<List<ArtworkVO>> getAllArtworks() {
        return ResponseEntity.ok(artworkService.getAllArtworks());
    }
}

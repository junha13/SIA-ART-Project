package lx.team2.backend.feature.publicart;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/publicart")
@RestController
public class PublicArtController {
	
	@Autowired
	PublicArtService service;
	
	// ========== 공공예술 핑찍은거 정보 받으면 db insert ==========
	@RequestMapping("/post")
	public ResponseEntity<?> insertPublicArtDB(PublicArtVO publicArt) {
		boolean result = service.insertPublicArtDB(publicArt);
		return ResponseEntity
				.ok()
				.header("api", "publicart/post")
				.body(Map.of("result", result));
	}
	
	/*
	@RequestMapping("/POST")
	public ResponseEntity<?> test() {
		return ResponseEntity
				.ok()
				.header("", "")
				.body(Map.of("message","Created"));
	}
	*/
}

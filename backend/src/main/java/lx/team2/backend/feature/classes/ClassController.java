package lx.team2.backend.feature.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; // List import 추가

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    /**
     * 클래스 상세 정보 조회 API
     * GET /api/classes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassDTO.DetailResponse> getClassDetail(@PathVariable int id) {
        ClassDTO.DetailResponse responseDTO = classService.getClassDetail(id);

        if (responseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 클래스 전체 목록 조회 API
     * GET /api/classes?category=순수 미술&query=검색어
     */
    @GetMapping
    public ResponseEntity<List<ClassDTO.ListResponse>> getAllClasses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String query) {

        // 👇 ClassDTO.ClassDTO.ListResponse -> ClassDTO.ListResponse 로 수정
        List<ClassDTO.ListResponse> dtoList = classService.getAllClasses(category, query);
        return ResponseEntity.ok(dtoList);
    }
}
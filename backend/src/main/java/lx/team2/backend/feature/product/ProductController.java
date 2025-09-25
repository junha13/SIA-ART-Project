package lx.team2.backend.feature.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productservice;

/*  @PostMapping("/getProductList")
    public List<ProductDTO> getProductList(){
    log.info("======= api/getProductList실행됨 =======");
        return productservice.getProductList();
    }*/
}

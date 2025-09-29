package lx.team2.backend.feature.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productservice;

/*    @PostMapping("/getProductList")
    public List<ProductDTO> getProductList(){
        return productservice.getProductList();
    }*/
}

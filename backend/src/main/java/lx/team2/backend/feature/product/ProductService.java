package lx.team2.backend.feature.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository ProductMapper;

/*    public List<ProductDTO> getProductList(){
        return ProductMapper.getProductList();
    }*/

}

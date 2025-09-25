package lx.team2.backend.feature.product;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductRepository {
    List<ProductDTO> ProductList();
}

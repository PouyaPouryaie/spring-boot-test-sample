package ir.bigz.springTest.service;

import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.entity.Product;
import ir.bigz.springTest.exception.ProductNotFoundException;
import ir.bigz.springTest.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductDto productDto) throws Exception {
        if(Objects.nonNull(productDto)){
            Product save = productRepository.save(productDtoToProduct(productDto));

            if(Objects.isNull(save.getProductId())){
                throw new Exception("product is not save");
            }
        }
    }

    @Override
    public ProductDto getProductByName(String name) {
        return productRepository.getProductByName(name)
                .map(this::productToProductDto)
                .orElse(null);
    }

    @Override
    public ProductDto getProductById(Long id) throws ProductNotFoundException {

        return productRepository.findById(id)
                .map(this::productToProductDto)
                .orElseThrow(ProductNotFoundException::new);
    }

    private Product productDtoToProduct(ProductDto productDto){
        Product product = Product.builder()
                .price(productDto.getPrice())
                .name(productDto.getName())
                .build();
        return product;
    }

    private ProductDto productToProductDto(Product product){
        ProductDto productDto = ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .build();
        return productDto;
    }
}

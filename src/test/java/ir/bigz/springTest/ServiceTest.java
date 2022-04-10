package ir.bigz.springTest;

import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.repository.ProductRepository;
import ir.bigz.springTest.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;


    @Test
    public void createProductTest(){
        ProductDto productDto = ProductDto.builder().price(1250.90).name("book").build();
        try {
            productService.createProduct(productDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProductDto productByName = productService.getProductByName(productDto.getName());
        assertEquals(productDto.getPrice(), productByName.getPrice());
    }
}

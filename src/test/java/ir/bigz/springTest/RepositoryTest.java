package ir.bigz.springTest;

import ir.bigz.springTest.entity.Product;
import ir.bigz.springTest.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void createProduct(){
        Product product = Product.builder().price(1500D).name("bottle").build();
        productRepository.save(product);
        Assertions.assertTrue(Objects.nonNull(product.getProductId()));
    }
}

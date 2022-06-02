package ir.bigz.springTest.integration;

import ir.bigz.springTest.TestH2Repository;
import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {

    @LocalServerPort
    int randomPort;

    @Autowired
    private TestH2Repository testH2Repository;
    private static TestRestTemplate restTemplate;
    private URI uri;

    @BeforeAll
    public static void init() {
        restTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setup() throws URISyntaxException {
        String baseUrl = "http://localhost:" + randomPort + "/product/api/v1";
        uri = new URI(baseUrl);
    }

    @Test
    public void createProductTest() {

        //given
        ProductDto productDto = ProductDto.builder().name("Ginger").price(54_000D).build();

        //when
        ResponseEntity<ProductDto> responseEntity = restTemplate.postForEntity(uri + "/", productDto, ProductDto.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(testH2Repository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Sql(statements = "INSERT INTO PRODUCT (name, price, product_id) values ('Shoes', 34000, 2)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM PRODUCT where name='Shoes'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllProductTest() {

        //when
        ResponseEntity<List> products = restTemplate.getForEntity(uri + "/", List.class);

        //then
        Assertions.assertThat(products.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(testH2Repository.findAll().size()).isEqualTo(Objects.requireNonNull(products.getBody()).size());
    }

    @Test
    @Sql(statements = "INSERT INTO PRODUCT (name, price, product_id) values ('phone', 26500, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteProductTest() {

        //given
        int recordCount = testH2Repository.findAll().size();

        Assertions.assertThat(testH2Repository.findAll().size()).isEqualTo(recordCount);

        //when
        restTemplate.delete(uri + "/byId/{productId}", 1);

        //then
        Assertions.assertThat(testH2Repository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Sql(statements = "INSERT INTO PRODUCT (name, price, product_id) values ('Car', 344000, 2)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM PRODUCT where name='Car'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateProductTest() {

        //given
        ProductDto productDto = ProductDto.builder().name("Car").price(554_000D).build();

        //when
        restTemplate.put(uri + "/byId/{productId}", productDto, 2);

        Product product = testH2Repository.findById(2L).orElseGet(() -> Product.builder().build());

        //then
        Assertions.assertThat(product.getPrice()).isEqualTo(productDto.getPrice());
    }
}

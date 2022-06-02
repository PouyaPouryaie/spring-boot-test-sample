package ir.bigz.springTest.controller;

import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * This class demonstrates how to test a controller using Spring Boot Test
 *  * (what makes it much closer to an Integration Test)
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerSpringbootTest {

    @LocalServerPort
    int randomServerPort;

    @MockBean
    private ProductService productService;

    private static TestRestTemplate restTemplate;

    private String baseUrl;
    private URI uri;

    @BeforeAll
    public static void init(){
        restTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setUp() throws URISyntaxException{
        baseUrl = "http://localhost:"+randomServerPort+"/product/api/v1";
        uri = new URI(baseUrl);
    }

    @Test
    public void canRetrieveByIdWhenExists() {
        //given
        ProductDto productDto = ProductDto.builder().productId(2L).name("Ginger").price(54_000D).build();
        given(productService.getProductById(2L))
                .willReturn(productDto);

        //when
        ResponseEntity<ProductDto> response = restTemplate.getForEntity(uri + "/byId?id=2", ProductDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(ProductDto.builder().productId(2L).name("Ginger").price(54_000D).build()));
    }

    @Test
    public void canCreateANewProduct() throws Exception {

        // when
        ResponseEntity<ProductDto> response = restTemplate.postForEntity(uri + "/",
                ProductDto.builder().productId(2L).name("Ginger").price(54_000D).build(), ProductDto.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
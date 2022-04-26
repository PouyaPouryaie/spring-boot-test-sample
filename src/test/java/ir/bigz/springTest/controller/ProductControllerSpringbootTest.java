package ir.bigz.springTest.controller;

import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * This class demonstrates how to test a controller using Spring Boot Test
 *  * (what makes it much closer to an Integration Test)
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerSpringbootTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void canRetrieveByIdWhenExists(){
        //given
        ProductDto productDto = new ProductDto(2L, "Ginger", 54_000D);
        given(productService.getProductById(2L))
                .willReturn(productDto);

        //when
        ResponseEntity<ProductDto> response = restTemplate.getForEntity("/product/api/v1/byId?id=2", ProductDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().equals(new ProductDto(2L, "Ginger", 54_000D)));
    }

    @Test
    public void canCreateANewProduct() throws Exception {
        // when
        ResponseEntity<ProductDto> response = restTemplate.postForEntity("/product/api/v1/",
                new ProductDto(2L, "Ginger", 54_000D), ProductDto.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
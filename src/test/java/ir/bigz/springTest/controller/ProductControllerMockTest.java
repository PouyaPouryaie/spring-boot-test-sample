package ir.bigz.springTest.controller;

import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * This class demonstrates how to test a controller using Spring Boot Test
 * with a MOCK web environment, which makes it similar to just using @WebMvcTest
 *
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ProductControllerMockTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ProductDto> jsonProductDto;

    @MockBean
    private ProductService productService;


    @Test
    public void canRetrieveByIdWhenExists() throws Exception {
        //given
        ProductDto productDto = ProductDto.builder().productId(2L).name("Ginger").price(54_000D).build();
        given(productService.getProductById(2L))
                .willReturn(productDto);

        //when
        MockHttpServletResponse response = mvc
                .perform(get("/product/api/v1/byId?id=2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonProductDto.write(productDto).getJson());
    }

}

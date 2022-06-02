package ir.bigz.springTest.controller;

import ir.bigz.springTest.controller.ProductController;
import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.exception.ProductNotFoundException;
import ir.bigz.springTest.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/*
This time, we need to use a JUnit 5 extension named SpringExtension,
which is provided by Spring. This extension causes the initialization
of part of the Spring context.

We also don’t need to make any reference to our controller class apart
from the one in the WebMVCTest annotation, since the controller will be
injected in the context automatically.
 */

@AutoConfigureJsonTesters
@WebMvcTest(ProductController.class)
public class ProductControllerMockWithApplicationContextTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    // This object will be initialized thanks to @AutoConfigureJsonTesters
    @Autowired
    private JacksonTester<ProductDto> jsonProductDto;

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

    @Test
    public void canRetrieveByIdWhenDoseNotExists() throws Exception {
        //given
        given(productService.getProductById(3L))
                .willThrow(ProductNotFoundException.class);

        //when
        MockHttpServletResponse response = mvc.perform(get("/product/api/v1/byId?id=3")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();


        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void canRetrieveByNameWhenExists() throws Exception {
        // given
        given(productService.getProductByName("RobotMan"))
                .willReturn(ProductDto.builder().productId(2L).name("Ginger").price(54_000D).build());

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/product/api/v1/byname?name=RobotMan")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(
                jsonProductDto.write(ProductDto.builder().productId(2L).name("Ginger").price(54_000D).build()).getJson());
    }

    @Test
    public void canRetrieveByNameWhenDoesNotExist() throws Exception {
        // given
        given(productService.getProductByName("ginger"))
                .willReturn(null);

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/product/api/v1/byname?name=ginger")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    public void canCreateANewProduct() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/product/api/v1/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProductDto.write(
                                ProductDto.builder().name("crops").price(120_000D).build())
                                .getJson()))
                .andReturn()
                .getResponse();

        // then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void headerIsPresent() throws Exception {
        // when
        MockHttpServletResponse response = mvc
                .perform(get("/product/api/v1/byId?id=2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getHeader("X-SprintTest-APP")).containsOnlyOnce("spring-header");
    }
}

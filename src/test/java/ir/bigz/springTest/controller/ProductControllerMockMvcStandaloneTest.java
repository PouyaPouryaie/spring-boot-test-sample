package ir.bigz.springTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.bigz.springTest.controller.ProductController;
import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.exception.ProductExceptionHandler;
import ir.bigz.springTest.exception.ProductNotFoundException;
import ir.bigz.springTest.filter.RequestFilter;
import ir.bigz.springTest.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/*
Prior to JUnit 5, we would use MockitoJUnitRunner to run our unit test.
In the new JUnit version, the runner behaviors have been replaced by Extension.
 */
@ExtendWith(MockitoExtension.class)
public class ProductControllerMockMvcStandaloneTest {

    private MockMvc mvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;


    // This object will be magically initialized by the initFields method below.
    //a utility class included in the Spring Boot Test module to generate and parse JSON.
    private JacksonTester<ProductDto> jsonProductDto;

    @BeforeEach
    public void setUp() {

        /*
        MockMvc standalone approach, you have to define controller, advice , ...
        because that you don’t have any Spring context that can inject them automatically.
         */
        mvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ProductExceptionHandler())
                .addFilters(new RequestFilter())
                .build();

        /*
        We would need this line if we would not use the MockitoExtension
        MockitoAnnotations.initMocks(this);
        Here we can't use @AutoConfigureJsonTesters because there isn't a Spring context
         */
        JacksonTester.initFields(this, new ObjectMapper());
    }

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

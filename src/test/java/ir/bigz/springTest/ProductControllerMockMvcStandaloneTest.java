package ir.bigz.springTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.bigz.springTest.controller.ProductController;
import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.exception.ProductExceptionHandler;
import ir.bigz.springTest.exception.ProductNotFoundException;
import ir.bigz.springTest.filter.RequestFilter;
import ir.bigz.springTest.service.ProductService;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class ProductControllerMockMvcStandaloneTest {

    private MockMvc mvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;


    // This object will be magically initialized by the initFields method below.
    private JacksonTester<ProductDto> jsonProductDto;

    @BeforeEach
    public void setUp(){
        mvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ProductExceptionHandler())
                .addFilters(new RequestFilter())
                .build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void canRetrieveByIdWhenExists() throws Exception{
        //given
        ProductDto productDto = new ProductDto(2L, "Ginger", 54_000D);
        given(productService.getProductById(2L))
                .willReturn(productDto);

        //when
        MockHttpServletResponse response = mvc
                .perform(get("/product/api/v1/byId?id=2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(response.getContentAsString(), jsonProductDto.write(productDto).getJson());
    }

    @Test
    public void canRetrieveByIdWhenDoseNotExists() throws Exception{
        //given
        given(productService.getProductById(3L))
                .willThrow(ProductNotFoundException.class);

        //when
        MockHttpServletResponse response = mvc.perform(get("/product/api/v1/byId?id=3")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();


        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        Assertions.assertTrue(response.getContentAsString().isEmpty());
    }
}

package ir.bigz.springTest.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.bigz.springTest.controller.ProductController;
import ir.bigz.springTest.entity.Product;
import ir.bigz.springTest.service.ProductService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

/**
 * You should favor the first strategy (MockMVC) if you want to code a real Unit Test,
 * whereas you should make use of RestTemplate if you intend to write an Integration Test
 *
 *
 * unitTesting with MockMvc
 */

@SpringBootTest
public class FirstSampleControllerTest {

    protected MockMvc mvc;

    @Autowired
    ProductService productService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ProductController productController;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    public void createProduct() throws Exception {
        String uri = "/product/api/v1";
        Product product = new Product();
        product.setPrice(120_000D);
        product.setName("Ginger");

        String inputJson = mapToJson(product);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(201, status);
    }
}

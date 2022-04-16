package ir.bigz.springTest.service;


import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.exception.ProductNotFoundException;

public interface ProductService {

    void createProduct(ProductDto productDto) throws Exception;

    ProductDto getProductByName(String name);
    ProductDto getProductById(Long id) throws ProductNotFoundException;
}

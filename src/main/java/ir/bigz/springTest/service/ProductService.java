package ir.bigz.springTest.service;


import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {

    void createProduct(ProductDto productDto) throws Exception;

    ProductDto getProductByName(String name);

    ProductDto getProductById(Long id) throws ProductNotFoundException;

    ProductDto updateProduct(Long id, ProductDto productDto) throws Exception;

    void deleteProductById(Long id) throws Exception;

    List<ProductDto> getAllProduct();
}

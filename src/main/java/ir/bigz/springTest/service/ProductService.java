package ir.bigz.springTest.service;


import ir.bigz.springTest.dto.ProductDto;

public interface ProductService {

    void createProduct(ProductDto productDto) throws Exception;

    ProductDto getProductByName(String name);
}

package ir.bigz.springTest.controller;

import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/api/v1")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) throws Exception {
        try {
            productService.createProduct(productDto);
            return ResponseEntity.accepted().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byname")
    public ResponseEntity<?> getProductByName(@RequestParam(value = "name", required = true) String productName) throws Exception {
        try {
            ProductDto productByName = productService.getProductByName(productName);
            return ResponseEntity.ok().body(productByName);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

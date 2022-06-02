package ir.bigz.springTest.controller;

import ir.bigz.springTest.dto.ProductDto;
import ir.bigz.springTest.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/api/v1")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) throws Exception {
        try {
            productService.createProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byname")
    public ResponseEntity<?> getProductByName(@RequestParam(value = "name", required = true) String productName) {
        try {
            ProductDto productByName = productService.getProductByName(productName);
            return ResponseEntity.ok().body(productByName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/byId")
    public ResponseEntity<?> getProductById(@RequestParam(value = "id") Long productId) {

        ProductDto productById = productService.getProductById(productId);
        return ResponseEntity.ok().body(productById);
    }

    @PutMapping("/byId/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto, @PathVariable Long productId) {
        try {
            ProductDto resultProduct = productService.updateProduct(productId, productDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resultProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/byId/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) throws Exception {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(String.format("product by %s dose not exist anymore", productId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProduct() {

        List<ProductDto> allProduct = productService.getAllProduct();
        return ResponseEntity.ok().body(allProduct);
    }
}

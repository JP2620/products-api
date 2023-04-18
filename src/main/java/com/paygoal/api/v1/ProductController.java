package com.paygoal.api.v1;

import com.paygoal.api.common.dtos.Product.CreateProductRequest;
import com.paygoal.api.common.dtos.Product.ProductDto;
import com.paygoal.api.common.dtos.Product.UpdateProductRequest;
import com.paygoal.api.common.exceptions.ProductNotFoundException;
import com.paygoal.api.common.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService
    ) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return new ResponseEntity<>(this.productService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        return new ResponseEntity<>(this.productService.createProduct(createProductRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest) {
        return new ResponseEntity<>(this.productService.updateProduct(id, updateProductRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        this.productService.deleteProduct(id);
        return;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void productNotFoundHandler(ProductNotFoundException e) {}
}

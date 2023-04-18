package com.paygoal.api.common.services;

import com.paygoal.api.common.dtos.Product.CreateProductRequest;
import com.paygoal.api.common.dtos.Product.ProductDto;
import com.paygoal.api.common.dtos.Product.UpdateProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductInternalService productInternalService;

    public ProductService(
            ProductInternalService productInternalService
    ) {
        this.productInternalService = productInternalService;
    }
    public List<ProductDto> getAllProducts() {
        return this.productInternalService.getAll().stream().map(product -> ProductDto.of(product)).toList();
    }

    public ProductDto createProduct(CreateProductRequest createProductRequest) {
        return ProductDto.of( this.productInternalService.create(createProductRequest) );
    }

    public ProductDto updateProduct(Long id, UpdateProductRequest updateProductRequest) {
        return ProductDto.of( this.productInternalService.update(id, updateProductRequest) );
    }
}

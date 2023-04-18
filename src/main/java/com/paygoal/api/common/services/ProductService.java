package com.paygoal.api.common.services;

import com.paygoal.api.common.dtos.Product.CreateProductRequest;
import com.paygoal.api.common.dtos.Product.ProductDto;
import com.paygoal.api.common.dtos.Product.UpdateProductRequest;
import com.paygoal.api.common.exceptions.ProductNotFoundException;
import com.paygoal.api.common.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void deleteProduct(Long id) {
        this.productInternalService.delete(id);
        return;
    }

    public ProductDto getById(Long id) {
        Optional<Product> maybeProduct = this.productInternalService.getById(id);
        if (maybeProduct.isPresent()) {
            return ProductDto.of(maybeProduct.get());
        } else {
            throw new ProductNotFoundException();
        }
    }

    public ProductDto getByName(String name) {
        Optional<Product> maybeProduct = this.productInternalService.getByName(name);
        if (maybeProduct.isPresent()) {
            return ProductDto.of(maybeProduct.get());
        } else {
            throw new ProductNotFoundException();
        }
    }
}

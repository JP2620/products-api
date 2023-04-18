package com.paygoal.api.common.services;

import com.paygoal.api.common.dtos.Product.CreateProductRequest;
import com.paygoal.api.common.model.Product;
import com.paygoal.api.common.repositories.ProductRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInternalService {
    private final ProductRepository productRepository;

    public ProductInternalService(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    public Product create(CreateProductRequest createProductRequest) {
        Product newProduct = new Product();
        newProduct.setPrice(createProductRequest.getPrice());
        newProduct.setQuantity(createProductRequest.getQuantity());
        newProduct.setDescription(createProductRequest.getDescription());
        newProduct.setName(createProductRequest.getName());
        return this.productRepository.save(newProduct);
    }
}

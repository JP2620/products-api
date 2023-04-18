package com.paygoal.api.common.services;

import com.paygoal.api.common.dtos.Product.CreateProductRequest;
import com.paygoal.api.common.dtos.Product.ProductDto;
import com.paygoal.api.common.dtos.Product.UpdateProductRequest;
import com.paygoal.api.common.exceptions.ProductNotFoundException;
import com.paygoal.api.common.model.Product;
import com.paygoal.api.common.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Product update(Long id, UpdateProductRequest updateProductRequest) {
        Optional<Product> maybeProduct = this.productRepository.findById(id);
        if (maybeProduct.isPresent()) {
            Product product = maybeProduct.get();
            product.setName(updateProductRequest.getName());
            product.setPrice(updateProductRequest.getPrice());
            product.setQuantity(updateProductRequest.getQuantity());
            product.setDescription(updateProductRequest.getDescription());
            return this.productRepository.save(product);
        } else {
           throw new ProductNotFoundException();
        }
    }

    public void delete(Long id) {
        this.productRepository.deleteById(id);
    }

    public Optional<Product> getById(Long id) {
        return this.productRepository.findById(id);
    }

    public Optional<Product> getByName(String name) {
        return this.productRepository.findByName(name);
    }

    public Page<Product> findAll(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }
}

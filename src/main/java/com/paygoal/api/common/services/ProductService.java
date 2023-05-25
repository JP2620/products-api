package com.paygoal.api.common.services;

import com.paygoal.api.common.dtos.Product.CreateProductRequest;
import com.paygoal.api.common.dtos.Product.ProductDto;
import com.paygoal.api.common.dtos.Product.UpdateProductRequest;
import com.paygoal.api.common.exceptions.ProductAlreadyExistsException;
import com.paygoal.api.common.exceptions.ProductNotFoundException;
import com.paygoal.api.common.model.Product;
import com.paygoal.api.common.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAll() {
        return this.productRepository.findAll().stream().map(product -> ProductDto.of(product)).toList();
    }

    public ProductDto create(CreateProductRequest createProductRequest) {
        Optional<Product> maybeProduct = this.productRepository.findByName(createProductRequest.getName());
        if (maybeProduct.isPresent()) {
            throw new ProductAlreadyExistsException();
        }

        Product newProduct = new Product();
        newProduct.setPrice(createProductRequest.getPrice());
        newProduct.setQuantity(createProductRequest.getQuantity());
        newProduct.setDescription(createProductRequest.getDescription());
        newProduct.setName(createProductRequest.getName());
        return ProductDto.of(this.productRepository.save(newProduct));
    }

    public ProductDto update(Long id, UpdateProductRequest updateProductRequest) {
        Optional<Product> maybeProduct = this.productRepository.findById(id);
        if (maybeProduct.isPresent()) {
            Product product = maybeProduct.get();
            if (this.getByName(updateProductRequest.getName()) != null) {
                throw new ProductAlreadyExistsException();
            }
            product.setName(updateProductRequest.getName());
            product.setPrice(updateProductRequest.getPrice());
            product.setQuantity(updateProductRequest.getQuantity());
            product.setDescription(updateProductRequest.getDescription());
            return ProductDto.of(this.productRepository.save(product));
        } else {
           throw new ProductNotFoundException();
        }
    }

    public void delete(Long id) {
        this.productRepository.deleteById(id);
    }

    public ProductDto getById(Long id) throws ProductNotFoundException {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return ProductDto.of(product.get());
        } else {
            throw new ProductNotFoundException();
        }
    }

    public ProductDto getByName(String name) throws ProductNotFoundException {
        Optional<Product> product = this.productRepository.findByName(name);
        if (product.isPresent()) {
            return ProductDto.of(product.get());
        } else {
            throw new ProductNotFoundException();
        }
    }

    public Page<ProductDto> findAll(Pageable pageable) {
        return this.productRepository.findAll(pageable).map(product -> ProductDto.of(product));
    }
}

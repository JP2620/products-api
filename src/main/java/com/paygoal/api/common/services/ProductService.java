package com.paygoal.api.common.services;

import com.paygoal.api.common.dtos.ProductDto;
import com.paygoal.api.common.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    public List<ProductDto> getAllProducts() {
        return List.of(
                Product
                        .builder()
                        .id(1L)
                        .description("idk")
                        .price(999L)
                        .quantity(5L)
                        .name("chocolate")
                        .build()
        )
                .stream()
                .map(p -> ProductDto.of(p))
                .toList();
    }
}

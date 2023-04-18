package com.paygoal.api.common.dtos.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateProductRequest {
    private String name;
    private String description;
    private Long price;
    private  Long quantity;
}

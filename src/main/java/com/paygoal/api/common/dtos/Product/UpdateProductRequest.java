package com.paygoal.api.common.dtos.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class UpdateProductRequest {
    private String name;
    private String description;
    private Long price;
    private  Long quantity;
}

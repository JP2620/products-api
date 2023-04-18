package com.paygoal.api.common.dtos.Product;

import com.paygoal.api.common.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Long price; // in cents
    private Long quantity;


    public static ProductDto of(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setDescription(product.getDescription());
        return dto;
    }

}

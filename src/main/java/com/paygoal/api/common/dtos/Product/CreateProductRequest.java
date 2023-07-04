package com.paygoal.api.common.dtos.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name should not be blank")
    private String name;
    @NotNull(message = "Description is required")
    @NotBlank(message = "Description should not be blank")
    private String description;
    @NotNull(message = "Price is required")
    private Long price;
    @NotNull(message = "Quantity is required")
    private  Long quantity;
}

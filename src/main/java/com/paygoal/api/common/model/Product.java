package com.paygoal.api.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
public class Product {
    @Id
    private Long id;
    private String name;
    private String description;
    private Long price; // in cents
    private Long quantity;
}

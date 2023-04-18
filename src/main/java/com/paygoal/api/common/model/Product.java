package com.paygoal.api.common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
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

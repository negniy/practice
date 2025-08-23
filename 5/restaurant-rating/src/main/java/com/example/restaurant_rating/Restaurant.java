package com.example.restaurant_rating;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Restaurant {
    private Long id;
    private String name;
    private String description;
    private CuisineType cuisineType;
    private double averageCheck;
    private BigDecimal userRating;

    public Restaurant(Long id, String name, CuisineType cuisineType, double averageCheck) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.averageCheck = averageCheck;
        this.userRating = BigDecimal.ZERO;
    }
}
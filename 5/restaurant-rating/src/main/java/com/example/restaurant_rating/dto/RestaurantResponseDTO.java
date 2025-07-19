package com.example.restaurant_rating.dto;

import com.example.restaurant_rating.CuisineType;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class RestaurantResponseDTO {
    Long id;
    String name;
    String description;
    CuisineType cuisineType;
    double averageCheck;
    BigDecimal userRating;
}
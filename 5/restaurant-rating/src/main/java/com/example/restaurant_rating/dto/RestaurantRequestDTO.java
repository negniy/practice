package com.example.restaurant_rating.dto;

import com.example.restaurant_rating.CuisineType;
import lombok.Value;

@Value
public class RestaurantRequestDTO {
    String name;
    String description;
    CuisineType cuisineType;
    double averageCheck;
}
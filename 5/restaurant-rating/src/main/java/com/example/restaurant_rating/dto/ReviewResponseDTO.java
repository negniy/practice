package com.example.restaurant_rating.dto;

import lombok.Value;

@Value
public class ReviewResponseDTO {
    Long visitorId;
    Long restaurantId;
    int score;
    String review;
}
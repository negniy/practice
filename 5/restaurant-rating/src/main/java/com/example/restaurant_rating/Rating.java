package com.example.restaurant_rating;

import lombok.Data;

@Data
public class Rating {
    private Long visitorId;
    private Long restaurantId;
    private int score;
    private String review;

    public Rating(Long visitorId, Long restaurantId, int score, String review) {
        this.visitorId = visitorId;
        this.restaurantId = restaurantId;
        this.score = score;
        this.review = review;
    }
}
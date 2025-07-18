package com.example.restaurant_rating;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RatingRepository {
    private final List<Rating> ratings = new ArrayList<>();

    public void save(Rating rating) {
        ratings.add(rating);
    }

    public void remove(Rating rating) {
        ratings.remove(rating);
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratings);
    }

    public Rating findById(Long visitorId, Long restaurantId) {
        return ratings.stream()
                .filter(r -> r.getVisitorId().equals(visitorId) && r.getRestaurantId().equals(restaurantId))
                .findFirst()
                .orElse(null);
    }
}
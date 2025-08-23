package com.example.restaurant_rating;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;

    public RatingService(RatingRepository ratingRepository, RestaurantRepository restaurantRepository) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void save(Rating rating) {
        ratingRepository.save(rating);
        updateRestaurantRating(rating.getRestaurantId());
    }

    public void remove(Rating rating) {
        ratingRepository.remove(rating);
        updateRestaurantRating(rating.getRestaurantId());
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    private void updateRestaurantRating(Long restaurantId) {
        List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();
        if (!ratings.isEmpty()) {
            BigDecimal average = BigDecimal.valueOf(ratings.stream()
                    .mapToInt(Rating::getScore)
                    .average()
                    .orElse(0.0));
            Restaurant restaurant = restaurantRepository.findAll().stream()
                    .filter(r -> r.getId().equals(restaurantId))
                    .findFirst()
                    .orElse(null);
            if (restaurant != null) {
                restaurant.setUserRating(average);
            }
        }
    }
}
package com.example.restaurant_rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByVisitorId(Long visitorId);
    List<Rating> findByRestaurantId(Long restaurantId);
    Rating findByVisitorIdAndRestaurantId(Long visitorId, Long restaurantId);
}
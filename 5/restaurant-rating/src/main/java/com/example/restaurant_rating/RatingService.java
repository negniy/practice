package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.ReviewRequestDTO;
import com.example.restaurant_rating.dto.ReviewResponseDTO;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;

    public RatingService(RatingRepository ratingRepository, RestaurantRepository restaurantRepository) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        Rating rating = new Rating(dto.getVisitorId(), dto.getRestaurantId(), dto.getScore(), dto.getReview());
        ratingRepository.save(rating);
        updateRestaurantRating(dto.getRestaurantId());
        return toResponseDTO(rating);
    }

    public void remove(Long visitorId, Long restaurantId) {
        Rating rating = ratingRepository.findById(visitorId, restaurantId);
        if (rating != null) {
            ratingRepository.remove(rating);
            updateRestaurantRating(restaurantId);
        }
    }

    public List<ReviewResponseDTO> findAll() {
        return ratingRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
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

    public ReviewResponseDTO findById(Long visitorId, Long restaurantId) {
        Rating r = ratingRepository.findById(visitorId, restaurantId);
        if (r == null) {
            throw new EntityNotFoundException("Review not found for user=" + visitorId + " restaurant=" + restaurantId);
        }
        return toResponseDTO(r);
    }

    public ReviewResponseDTO update(Long visitorId, Long restaurantId, ReviewRequestDTO dto) {
        Rating r = ratingRepository.findById(visitorId, restaurantId);
        if (r == null) {
            throw new EntityNotFoundException("Review not found for user=" + visitorId + " restaurant=" + restaurantId);
        }
        r.setScore(dto.getScore());
        r.setReview(dto.getReview());
        updateRestaurantRating(restaurantId);
        return toResponseDTO(r);
    }

    private ReviewResponseDTO toResponseDTO(Rating rating) {
        return new ReviewResponseDTO(rating.getVisitorId(), rating.getRestaurantId(), rating.getScore(), rating.getReview());
    }
}
package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.ReviewRequestDTO;
import com.example.restaurant_rating.dto.ReviewResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final VisitorRepository visitorRepository;
    private final RestaurantRepository restaurantRepository;

    public RatingService(RatingRepository ratingRepository,
                         VisitorRepository visitorRepository,
                         RestaurantRepository restaurantRepository) {
        this.ratingRepository = ratingRepository;
        this.visitorRepository = visitorRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        var visitor = visitorRepository.findById(dto.getVisitorId())
                .orElseThrow(() -> new EntityNotFoundException("Visitor not found: " + dto.getVisitorId()));
        var restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found: " + dto.getRestaurantId()));
        var rating = new Rating(visitor, restaurant, dto.getScore(), dto.getReview());
        rating = ratingRepository.save(rating);
        updateRestaurantRating(restaurant.getId());
        return toResponseDTO(rating);
    }

    public ReviewResponseDTO findById(Long visitorId, Long restaurantId) {
        Rating rating = ratingRepository.findByVisitorIdAndRestaurantId(visitorId, restaurantId);
        if (rating == null) {
            throw new EntityNotFoundException("Review not found for visitor=" + visitorId +
                                              " restaurant=" + restaurantId);
        }
        return toResponseDTO(rating);
    }

    public void deleteById(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    public List<ReviewResponseDTO> findAll() {
        return ratingRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO findByVisitorAndRestaurant(Long visitorId, Long restaurantId) {
        var rating = ratingRepository.findByVisitorIdAndRestaurantId(visitorId, restaurantId);
        if (rating == null) {
            throw new EntityNotFoundException("Review not found for visitor=" + visitorId +
                                              " restaurant=" + restaurantId);
        }
        return toResponseDTO(rating);
    }

    public ReviewResponseDTO update(Long visitorId, Long restaurantId, ReviewRequestDTO dto) {
        var rating = ratingRepository.findByVisitorIdAndRestaurantId(visitorId, restaurantId);
        if (rating == null) {
            throw new EntityNotFoundException("Review not found for visitor=" + visitorId +
                                              " restaurant=" + restaurantId);
        }
        rating.setScore(dto.getScore());
        rating.setReview(dto.getReview());
        rating = ratingRepository.save(rating);
        updateRestaurantRating(restaurantId);
        return toResponseDTO(rating);
    }

    private void updateRestaurantRating(Long restaurantId) {
        List<Rating> ratings = ratingRepository.findByRestaurantId(restaurantId);
        if (!ratings.isEmpty()) {
            double avg = ratings.stream()
                    .mapToInt(Rating::getScore)
                    .average()
                    .orElse(0.0);
            BigDecimal average = BigDecimal.valueOf(avg);
            var restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new EntityNotFoundException("Restaurant not found: " + restaurantId));
            restaurant.setUserRating(average);
            restaurantRepository.save(restaurant);
        }
    }

    private ReviewResponseDTO toResponseDTO(Rating rating) {
        return new ReviewResponseDTO(
                rating.getVisitor().getId(),
                rating.getRestaurant().getId(),
                rating.getScore(),
                rating.getReview()
        );
    }

    public boolean remove(Long visitorId, Long restaurantId) {
        Rating rating = ratingRepository.findByVisitorIdAndRestaurantId(visitorId, restaurantId);
        if (rating != null) {
            ratingRepository.delete(rating);
            updateRestaurantRating(restaurantId);
            return true;
        }
        return false;
    }
}
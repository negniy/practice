package com.example.restaurant_rating.controllers;

import com.example.restaurant_rating.dto.ReviewRequestDTO;
import com.example.restaurant_rating.dto.ReviewResponseDTO;
import com.example.restaurant_rating.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final RatingService ratingService;

    public ReviewController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(ratingService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(ratingService.findAll());
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<ReviewResponseDTO> getById(
            @PathVariable Long visitorId,
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(ratingService.findById(visitorId, restaurantId));
    }

    @PutMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<ReviewResponseDTO> update(
            @PathVariable Long visitorId,
            @PathVariable Long restaurantId,
            @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(ratingService.update(visitorId, restaurantId, dto));
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long visitorId, @PathVariable Long restaurantId) {
        ratingService.remove(visitorId, restaurantId);
        return ResponseEntity.ok().build();
    }
}
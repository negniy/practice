package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.UserRequestDTO;
import com.example.restaurant_rating.dto.RestaurantRequestDTO;
import com.example.restaurant_rating.dto.ReviewRequestDTO;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantRatingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantRatingApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(VisitorService visitorService, RestaurantService restaurantService, RatingService ratingService) {
        return args -> {
            visitorService.save(new UserRequestDTO(null, 25, "Male"));
            visitorService.save(new UserRequestDTO(null, 30, "Female"));
            restaurantService.save(new RestaurantRequestDTO("Italian Bistro", null, CuisineType.ITALIAN, 50.0));
            restaurantService.save(new RestaurantRequestDTO("China Wok", null, CuisineType.CHINESE, 30.0));
            ratingService.save(new ReviewRequestDTO(1L, 1L, 4, "Good food!"));
            ratingService.save(new ReviewRequestDTO(2L, 1L, 5, "Excellent service"));

            System.out.println("Initial data loaded. Use API to interact.");
        };
    }
}
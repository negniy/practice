package com.example.restaurant_rating;

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
            // Инициализация посетителей
            Visitor v1 = new Visitor(1L, 25, "Male");
            Visitor v2 = new Visitor(2L, 30, "Female");
            visitorService.save(v1);
            visitorService.save(v2);

            // Инициализация ресторанов
            Restaurant r1 = new Restaurant(1L, "Italian Bistro", CuisineType.ITALIAN, 50.0);
            Restaurant r2 = new Restaurant(2L, "China Wok", CuisineType.CHINESE, 30.0);
            restaurantService.save(r1);
            restaurantService.save(r2);

            // Инициализация оценок
            Rating rt1 = new Rating(1L, 1L, 4, "Good food!");
            Rating rt2 = new Rating(2L, 1L, 5, "Excellent service");
            ratingService.save(rt1);
            ratingService.save(rt2);

            // Вывод для проверки
            System.out.println("Visitors: " + visitorService.findAll());
            System.out.println("Restaurants: " + restaurantService.findAll());
            System.out.println("Ratings: " + ratingService.findAll());

			// Удаление рейтинга и проверка обновления
            ratingService.remove(rt1);
            System.out.println("Restaurants after removal: " + restaurantService.findAll());
        };
    }
}
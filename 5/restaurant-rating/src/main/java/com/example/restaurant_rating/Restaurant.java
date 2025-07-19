package com.example.restaurant_rating;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;

    private Double averageCheck;

    private BigDecimal userRating;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    public Restaurant(String name, String description, CuisineType cuisineType, Double averageCheck) {
        this.name = name;
        this.description = description;
        this.cuisineType = cuisineType;
        this.averageCheck = averageCheck;
        this.userRating = BigDecimal.ZERO;
    }
}
package com.example.restaurant_rating;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ratings",
       uniqueConstraints = @UniqueConstraint(columnNames = {"visitor_id","restaurant_id"}))
@Data
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitor_id", nullable = false)
    private Visitor visitor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    private Integer score;

    @Column(length = 1000)
    private String review;

    public Rating(Visitor visitor, Restaurant restaurant, Integer score, String review) {
        this.visitor = visitor;
        this.restaurant = restaurant;
        this.score = score;
        this.review = review;
    }
}
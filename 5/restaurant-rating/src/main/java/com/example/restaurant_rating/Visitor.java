package com.example.restaurant_rating;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "visitors")
@Data
@NoArgsConstructor
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String gender;

    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    public Visitor(String name, Integer age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}

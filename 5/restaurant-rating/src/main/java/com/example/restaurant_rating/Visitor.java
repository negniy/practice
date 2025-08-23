package com.example.restaurant_rating;

import lombok.Data;

@Data
public class Visitor {
    private Long id;
    private String name;
    private int age;
    private String gender;

    public Visitor(Long id, int age, String gender) {
        this.id = id;
        this.age = age;
        this.gender = gender;
    }
}
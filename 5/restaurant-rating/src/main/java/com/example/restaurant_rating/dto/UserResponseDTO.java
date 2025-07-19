package com.example.restaurant_rating.dto;

import lombok.Value;

@Value
public class UserResponseDTO {
    Long id;
    String name;
    int age;
    String gender;
}
package com.example.restaurant_rating.controllers;

import com.example.restaurant_rating.dto.RestaurantRequestDTO;
import com.example.restaurant_rating.dto.RestaurantResponseDTO;
import com.example.restaurant_rating.RestaurantService;
import com.example.restaurant_rating.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@RequestBody RestaurantRequestDTO dto) {
        return ResponseEntity.ok(restaurantService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> update(
            @PathVariable Long id,
            @RequestBody RestaurantRequestDTO dto) {
        return ResponseEntity.ok(restaurantService.update(id, dto));
    }

    @DeleteMapping("/{id}")
public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
    boolean removed = restaurantService.removeById(id);
    return removed 
        ? ResponseEntity.ok().build() 
        : ResponseEntity.notFound().build();
}
}
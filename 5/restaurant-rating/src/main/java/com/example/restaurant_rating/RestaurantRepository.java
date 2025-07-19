package com.example.restaurant_rating;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private long nextId = 1;

    public void save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            // присваиваем новый id
            restaurant.setId(nextId++);
        }
        restaurants.add(restaurant);
    }

    public void remove(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }

    public Restaurant findById(Long id) {
        return restaurants.stream()
                          .filter(r -> id.equals(r.getId()))
                          .findFirst()
                          .orElse(null);
    }
}
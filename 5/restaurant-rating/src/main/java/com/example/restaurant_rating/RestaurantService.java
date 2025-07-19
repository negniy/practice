package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.RestaurantRequestDTO;
import com.example.restaurant_rating.dto.RestaurantResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantResponseDTO save(RestaurantRequestDTO dto) {
        Restaurant restaurant = new Restaurant(
                dto.getName(),
                dto.getDescription(),
                dto.getCuisineType(),
                dto.getAverageCheck()
        );
        restaurant = restaurantRepository.save(restaurant);
        return toResponseDTO(restaurant);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO findById(Long id) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found: " + id));
        return toResponseDTO(r);
    }

    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO dto) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found: " + id));
        r.setName(dto.getName());
        r.setDescription(dto.getDescription());
        r.setCuisineType(dto.getCuisineType());
        r.setAverageCheck(dto.getAverageCheck());
        r = restaurantRepository.save(r);
        return toResponseDTO(r);
    }

    public boolean removeById(Long id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }

    private RestaurantResponseDTO toResponseDTO(Restaurant restaurant) {
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                restaurant.getAverageCheck(),
                restaurant.getUserRating()
        );
    }
}
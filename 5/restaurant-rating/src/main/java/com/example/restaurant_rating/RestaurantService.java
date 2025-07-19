package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.RestaurantRequestDTO;
import com.example.restaurant_rating.dto.RestaurantResponseDTO;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantResponseDTO save(RestaurantRequestDTO dto) {
        Restaurant restaurant = new Restaurant(null, dto.getName(), dto.getCuisineType(), dto.getAverageCheck());
        restaurant.setDescription(dto.getDescription());
        restaurantRepository.save(restaurant);
        return toResponseDTO(restaurant);
    }

    public boolean removeById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        if (restaurant != null) {
            restaurantRepository.remove(restaurant);
            return true;
        }
        return false;
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO findById(Long id) {
        Restaurant r = restaurantRepository.findById(id);
        if (r == null) {
            throw new EntityNotFoundException("Restaurant not found: " + id);
        }
        return toResponseDTO(r);
    }

    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO dto) {
        Restaurant r = restaurantRepository.findById(id);
        if (r == null) {
            throw new EntityNotFoundException("Restaurant not found: " + id);
        }
        r.setName(dto.getName());
        r.setDescription(dto.getDescription());
        r.setCuisineType(dto.getCuisineType());
        r.setAverageCheck(dto.getAverageCheck());
        return toResponseDTO(r);
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

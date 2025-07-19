package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.ReviewRequestDTO;
import com.example.restaurant_rating.dto.ReviewResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RatingService ratingService;

    private Visitor visitor;
    private Restaurant restaurant;
    private Rating rating;
    private ReviewRequestDTO dto;

    @BeforeEach
    void setUp() {
        visitor = new Visitor("Alice", 30, "Female");
        visitor.setId(1L);
        restaurant = new Restaurant("Resto", "Nice", CuisineType.ITALIAN, 50.0);
        restaurant.setId(2L);
        rating = new Rating(visitor, restaurant, 5, "Excellent");
        rating.setId(10L);
        dto = new ReviewRequestDTO(1L, 2L, 5, "Excellent");
    }

    @Test
    void testSave_WhenValid() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingRepository.findByRestaurantId(2L)).thenReturn(Collections.singletonList(rating));
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        ReviewResponseDTO response = ratingService.save(dto);

        assertNotNull(response);
        assertEquals(1L, response.getVisitorId());
        assertEquals(2L, response.getRestaurantId());
        assertEquals(5, response.getScore());
        assertEquals("Excellent", response.getReview());

        verify(visitorRepository).findById(1L);
        verify(restaurantRepository, times(2)).findById(2L);
        verify(ratingRepository).save(any(Rating.class));
        verify(ratingRepository).findByRestaurantId(2L);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testFindById_WhenFound() {
        when(ratingRepository.findByVisitorIdAndRestaurantId(1L, 2L)).thenReturn(rating);

        ReviewResponseDTO response = ratingService.findById(1L, 2L);

        assertNotNull(response);
        assertEquals(5, response.getScore());
        assertEquals("Excellent", response.getReview());
    }

    @Test
    void testFindById_WhenNotFound() {
        when(ratingRepository.findByVisitorIdAndRestaurantId(1L, 2L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> ratingService.findById(1L, 2L));
    }

    @Test
    void testFindAll() {
        Rating r2 = new Rating(visitor, restaurant, 3, "Good");
        r2.setId(11L);
        when(ratingRepository.findAll()).thenReturn(Arrays.asList(rating, r2));

        List<ReviewResponseDTO> list = ratingService.findAll();

        assertEquals(2, list.size());
        assertEquals(5, list.get(0).getScore());
        assertEquals(3, list.get(1).getScore());
    }

    @Test
    void testFindByVisitorAndRestaurant_WhenFound() {
        when(ratingRepository.findByVisitorIdAndRestaurantId(1L, 2L)).thenReturn(rating);

        ReviewResponseDTO response = ratingService.findByVisitorAndRestaurant(1L, 2L);

        assertNotNull(response);
        assertEquals(1L, response.getVisitorId());
    }

    @Test
    void testFindByVisitorAndRestaurant_WhenNotFound() {
        when(ratingRepository.findByVisitorIdAndRestaurantId(1L, 2L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> ratingService.findByVisitorAndRestaurant(1L, 2L));
    }

    @Test
    void testUpdate_WhenFound() {
        ReviewRequestDTO updateDto = new ReviewRequestDTO(1L, 2L, 4, "Good");
        when(ratingRepository.findByVisitorIdAndRestaurantId(1L, 2L)).thenReturn(rating);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingRepository.findByRestaurantId(2L)).thenReturn(Collections.singletonList(rating));
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        ReviewResponseDTO response = ratingService.update(1L, 2L, updateDto);

        assertNotNull(response);
        assertEquals(4, response.getScore());
        assertEquals("Good", response.getReview());
        verify(ratingRepository).save(any(Rating.class));
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testUpdate_WhenNotFound() {
        when(ratingRepository.findByVisitorIdAndRestaurantId(1L, 2L)).thenReturn(null);
        ReviewRequestDTO updateDto = new ReviewRequestDTO(1L, 2L, 4, "Good");

        assertThrows(EntityNotFoundException.class, () -> ratingService.update(1L, 2L, updateDto));
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void testRemove_WhenExists() {
        when(ratingRepository.findByVisitorIdAndRestaurantId(1L, 2L)).thenReturn(rating);
        when(ratingRepository.findByRestaurantId(2L)).thenReturn(Collections.singletonList(rating));
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        boolean result = ratingService.remove(1L, 2L);

        assertTrue(result);
        verify(ratingRepository).delete(rating);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testRemove_WhenNotExists() {
        when(ratingRepository.findByVisitorIdAndRestaurantId(1L, 2L)).thenReturn(null);

        boolean result = ratingService.remove(1L, 2L);

        assertFalse(result);
        verify(ratingRepository, never()).delete(any());
    }

    @Test
    void testDeleteById() {
        ratingService.deleteById(10L);

        verify(ratingRepository).deleteById(10L);
    }
}

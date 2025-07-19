package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.RestaurantRequestDTO;
import com.example.restaurant_rating.dto.RestaurantResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private RestaurantRequestDTO dto;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        dto = new RestaurantRequestDTO(
                "Test Restaurant",
                "A cozy place",
                CuisineType.ITALIAN,
                100.0
        );
        restaurant = new Restaurant(
                dto.getName(),
                dto.getDescription(),
                dto.getCuisineType(),
                dto.getAverageCheck()
        );
        restaurant.setId(1L);
        restaurant.setUserRating(BigDecimal.ZERO);
    }

    @Test
    void testSave() {
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> {
            Restaurant r = invocation.getArgument(0);
            r.setId(1L);
            r.setUserRating(BigDecimal.ZERO);
            return r;
        });

        RestaurantResponseDTO response = restaurantService.save(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Restaurant", response.getName());
        assertEquals("A cozy place", response.getDescription());
        assertEquals(CuisineType.ITALIAN, response.getCuisineType());
        assertEquals(100.0, response.getAverageCheck());
        assertEquals(BigDecimal.ZERO, response.getUserRating());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testFindAll() {
        Restaurant r1 = new Restaurant("R1", "Desc1", CuisineType.CHINESE, 50.0);
        r1.setId(2L);
        r1.setUserRating(BigDecimal.valueOf(4.5));
        Restaurant r2 = new Restaurant("R2", "Desc2", CuisineType.EUROPEAN, 75.0);
        r2.setId(3L);
        r2.setUserRating(BigDecimal.valueOf(3.8));
        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<RestaurantResponseDTO> list = restaurantService.findAll();

        assertEquals(2, list.size());
        assertEquals(2L, list.get(0).getId());
        assertEquals("R1", list.get(0).getName());
        assertEquals(BigDecimal.valueOf(4.5), list.get(0).getUserRating());
        assertEquals(3L, list.get(1).getId());
        assertEquals("R2", list.get(1).getName());
        assertEquals(BigDecimal.valueOf(3.8), list.get(1).getUserRating());
    }

    @Test
    void testFindById_WhenFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantResponseDTO response = restaurantService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Restaurant", response.getName());
    }

    @Test
    void testFindById_WhenNotFound() {
        when(restaurantRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restaurantService.findById(5L));
    }

    @Test
    void testUpdate_WhenFound() {
        RestaurantRequestDTO updateDto = new RestaurantRequestDTO(
                "Updated Name",
                "Updated Desc",
                CuisineType.CHINESE,
                120.0
        );
        Restaurant updated = new Restaurant(
                updateDto.getName(),
                updateDto.getDescription(),
                updateDto.getCuisineType(),
                updateDto.getAverageCheck()
        );
        updated.setId(1L);
        updated.setUserRating(BigDecimal.valueOf(1.2));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updated);

        RestaurantResponseDTO response = restaurantService.update(1L, updateDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Updated Name", response.getName());
        assertEquals(120.0, response.getAverageCheck());
        assertEquals(CuisineType.CHINESE, response.getCuisineType());
        assertEquals(BigDecimal.valueOf(1.2), response.getUserRating());
        verify(restaurantRepository).findById(1L);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testUpdate_WhenNotFound() {
        when(restaurantRepository.findById(9L)).thenReturn(Optional.empty());

        RestaurantRequestDTO updateDto = new RestaurantRequestDTO("X", null, CuisineType.EUROPEAN, 10.0);
        assertThrows(EntityNotFoundException.class, () -> restaurantService.update(9L, updateDto));
        verify(restaurantRepository).findById(9L);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testRemoveById_WhenExists() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);

        boolean res = restaurantService.removeById(1L);

        assertTrue(res);
        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void testRemoveById_WhenNotExists() {
        when(restaurantRepository.existsById(2L)).thenReturn(false);

        boolean res = restaurantService.removeById(2L);

        assertFalse(res);
        verify(restaurantRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteById() {
        restaurantService.deleteById(10L);
        verify(restaurantRepository).deleteById(10L);
    }
}

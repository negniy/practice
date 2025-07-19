package com.example.restaurant_rating.controllers;

import com.example.restaurant_rating.CuisineType;
import com.example.restaurant_rating.RatingService;
import com.example.restaurant_rating.dto.RestaurantRequestDTO;
import com.example.restaurant_rating.dto.RestaurantResponseDTO;
import com.example.restaurant_rating.RestaurantService;
import com.example.restaurant_rating.VisitorService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private VisitorService visitorService;
    @MockBean private RestaurantService restaurantService;
    @MockBean private RatingService ratingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/restaurants - success")
    void testGetAllRestaurants() throws Exception {
        List<RestaurantResponseDTO> list = Arrays.asList(
            new RestaurantResponseDTO(1L, "La Piazza", "Cozy Italian", CuisineType.ITALIAN, 55.0, BigDecimal.valueOf(4.7)),
            new RestaurantResponseDTO(2L, "Dragon Express", "Fast Chinese", CuisineType.CHINESE, 25.0, BigDecimal.valueOf(4.1))
        );
        Mockito.when(restaurantService.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/restaurants"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].cuisineType").value("ITALIAN"))
               .andExpect(jsonPath("$[1].cuisineType").value("CHINESE"));
    }

    @Test
    @DisplayName("GET /api/restaurants/{id} - found")
    void testGetByIdFound() throws Exception {
        RestaurantResponseDTO dto =
            new RestaurantResponseDTO(1L, "Europa Deli", "Pan-European menu", CuisineType.EUROPEAN, 40.0, BigDecimal.valueOf(4.3));
        Mockito.when(restaurantService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/restaurants/{id}", 1L))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.cuisineType").value("EUROPEAN"))
               .andExpect(jsonPath("$.averageCheck").value(40.0));
    }

    @Test
    @DisplayName("GET /api/restaurants/{id} - not found")
    void testGetByIdNotFound() throws Exception {
        Mockito.when(restaurantService.findById(99L))
               .thenThrow(new EntityNotFoundException("Restaurant not found"));

        mockMvc.perform(get("/api/restaurants/{id}", 99L))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Restaurant not found"));
    }

    @Test
    @DisplayName("POST /api/restaurants - success")
    void testCreateRestaurant() throws Exception {
        RestaurantRequestDTO req = new RestaurantRequestDTO("New Italiano", "Garden patio", CuisineType.ITALIAN, 60.0);
        RestaurantResponseDTO resp =
            new RestaurantResponseDTO(5L, "New Italiano", "Garden patio", CuisineType.ITALIAN, 60.0, BigDecimal.ZERO);
        Mockito.when(restaurantService.save(any(RestaurantRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(5L))
               .andExpect(jsonPath("$.cuisineType").value("ITALIAN"));
    }

    @Test
    @DisplayName("PUT /api/restaurants/{id} - success")
    void testUpdateRestaurant() throws Exception {
        RestaurantRequestDTO req = new RestaurantRequestDTO("China Town", "Authentic Chinese", CuisineType.CHINESE, 35.0);
        RestaurantResponseDTO resp =
            new RestaurantResponseDTO(1L, "China Town", "Authentic Chinese", CuisineType.CHINESE, 35.0, BigDecimal.valueOf(4.5));
        Mockito.when(restaurantService.update(eq(1L), any(RestaurantRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(put("/api/restaurants/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.cuisineType").value("CHINESE"))
               .andExpect(jsonPath("$.userRating").value(4.5));
    }

    @Test
    @DisplayName("DELETE /api/restaurants/{id} - exists")
    void testDeleteWhenExists() throws Exception {
        Mockito.when(restaurantService.removeById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/restaurants/{id}", 1L))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/restaurants/{id} - not exists")
    void testDeleteWhenNotExists() throws Exception {
        Mockito.when(restaurantService.removeById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/restaurants/{id}", 99L))
               .andExpect(status().isNotFound());
    }
}

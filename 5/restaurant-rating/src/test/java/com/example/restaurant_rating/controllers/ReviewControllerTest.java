package com.example.restaurant_rating.controllers;

import com.example.restaurant_rating.controllers.ReviewController;
import com.example.restaurant_rating.dto.ReviewRequestDTO;
import com.example.restaurant_rating.dto.ReviewResponseDTO;
import com.example.restaurant_rating.RatingService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private VisitorService visitorService;
    @MockBean private RestaurantService restaurantService;
    @MockBean private RatingService ratingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/reviews — should return list of reviews")
    void testGetAllReviews() throws Exception {
        List<ReviewResponseDTO> reviews = Arrays.asList(
            new ReviewResponseDTO(10L, 20L, 5, "Awesome!"),
            new ReviewResponseDTO(11L, 21L, 4, "Good")
        );
        Mockito.when(ratingService.findAll()).thenReturn(reviews);

        mockMvc.perform(get("/api/reviews"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].visitorId").value(10L))
               .andExpect(jsonPath("$[0].score").value(5))
               .andExpect(jsonPath("$[1].review").value("Good"));
    }

    @Test
    @DisplayName("GET /api/reviews/{visitorId}/{restaurantId} — when found")
    void testGetByIdFound() throws Exception {
        ReviewResponseDTO dto = new ReviewResponseDTO(10L, 20L, 5, "Excellent");
        Mockito.when(ratingService.findById(10L, 20L)).thenReturn(dto);

        mockMvc.perform(get("/api/reviews/{visitorId}/{restaurantId}", 10L, 20L))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.visitorId").value(10L))
               .andExpect(jsonPath("$.score").value(5))
               .andExpect(jsonPath("$.review").value("Excellent"));
    }

    @Test
    @DisplayName("GET /api/reviews/{visitorId}/{restaurantId} — when not found")
    void testGetByIdNotFound() throws Exception {
        Mockito.when(ratingService.findById(99L, 88L))
               .thenThrow(new EntityNotFoundException("Not exists"));

        mockMvc.perform(get("/api/reviews/{visitorId}/{restaurantId}", 99L, 88L))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/reviews — should create review")
    void testCreateReview() throws Exception {
        ReviewRequestDTO req = new ReviewRequestDTO(10L, 20L, 5, "Nice");
        ReviewResponseDTO resp = new ReviewResponseDTO(10L, 20L, 5, "Nice");
        Mockito.when(ratingService.save(any(ReviewRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.visitorId").value(10L))
               .andExpect(jsonPath("$.review").value("Nice"));
    }

    @Test
    @DisplayName("PUT /api/reviews/{visitorId}/{restaurantId} — should update review")
    void testUpdateReview() throws Exception {
        ReviewRequestDTO req = new ReviewRequestDTO(10L, 20L, 4, "Updated");
        ReviewResponseDTO resp = new ReviewResponseDTO(10L, 20L, 4, "Updated");
        Mockito.when(ratingService.update(eq(10L), eq(20L), any(ReviewRequestDTO.class)))
               .thenReturn(resp);

        mockMvc.perform(put("/api/reviews/{visitorId}/{restaurantId}", 10L, 20L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.score").value(4))
               .andExpect(jsonPath("$.review").value("Updated"));
    }

    @Test
    @DisplayName("DELETE /api/reviews/{visitorId}/{restaurantId} — should delete review")
    void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/api/reviews/{visitorId}/{restaurantId}", 10L, 20L))
               .andExpect(status().isOk());
    }
}

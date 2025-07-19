package com.example.restaurant_rating.controllers;

import com.example.restaurant_rating.dto.UserRequestDTO;
import com.example.restaurant_rating.dto.UserResponseDTO;
import com.example.restaurant_rating.RatingService;
import com.example.restaurant_rating.RestaurantService;
import com.example.restaurant_rating.VisitorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean VisitorService visitorService;
    @MockBean RestaurantService restaurantService;
    @MockBean RatingService ratingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/users - success")
    void testGetAllUsers() throws Exception {
        List<UserResponseDTO> list = Arrays.asList(
            new UserResponseDTO(1L, "Alice", 30, "Female"),
            new UserResponseDTO(2L, "Bob", 25, "Male")
        );
        Mockito.when(visitorService.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - found")
    void testGetUserByIdFound() throws Exception {
        UserResponseDTO dto = new UserResponseDTO(1L, "Alice", 30, "Female");
        Mockito.when(visitorService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - not found")
    void testGetUserByIdNotFound() throws Exception {
        Mockito.when(visitorService.findById(99L))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
@DisplayName("POST /api/users - success")
void testCreateUser() throws Exception {
    UserRequestDTO request  = new UserRequestDTO("Charlie", 40, "Male");
    UserResponseDTO response = new UserResponseDTO(3L, "Charlie", 40, "Male");
    Mockito.when(visitorService.save(any(UserRequestDTO.class))).thenReturn(response);

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isOk()) // ← меняем с isCreated() на isOk()
           .andExpect(jsonPath("$.id").value(3L))
           .andExpect(jsonPath("$.name").value("Charlie"));
}

    @Test
    @DisplayName("PUT /api/users/{id} - success")
    void testUpdateUser() throws Exception {
        UserRequestDTO request = new UserRequestDTO("AliceUpdated", 31, "Female");
        UserResponseDTO response = new UserResponseDTO(1L, "AliceUpdated", 31, "Female");
        Mockito.when(visitorService.update(eq(1L), any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("AliceUpdated"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - success")
    void testDeleteUserWhenExists() throws Exception {
        Mockito.when(visitorService.removeById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - not found")
    void testDeleteUserWhenNotExists() throws Exception {
        Mockito.when(visitorService.removeById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}

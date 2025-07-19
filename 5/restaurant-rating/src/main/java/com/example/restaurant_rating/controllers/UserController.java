package com.example.restaurant_rating.controllers;

import com.example.restaurant_rating.dto.UserRequestDTO;
import com.example.restaurant_rating.dto.UserResponseDTO;
import com.example.restaurant_rating.Visitor;
import com.example.restaurant_rating.VisitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final VisitorService visitorService;

    public UserController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(visitorService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(visitorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(visitorService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(visitorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = visitorService.removeById(id);
        return deleted
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }
}
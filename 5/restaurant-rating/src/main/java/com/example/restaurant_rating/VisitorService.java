package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.UserRequestDTO;
import com.example.restaurant_rating.dto.UserResponseDTO;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public UserResponseDTO save(UserRequestDTO dto) {
        Visitor visitor = new Visitor(null, dto.getAge(), dto.getGender());
        visitor.setName(dto.getName());
        visitorRepository.save(visitor);
        return toResponseDTO(visitor);
    }

    public void remove(Visitor visitor) {
        visitorRepository.remove(visitor);
    }

    public List<UserResponseDTO> findAll() {
        return visitorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findById(Long id) {
        Visitor v = visitorRepository.findById(id);
        if (v == null) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        return toResponseDTO(v);
    }

    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        Visitor v = visitorRepository.findById(id);
        if (v == null) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        v.setName(dto.getName());
        v.setAge(dto.getAge());
        v.setGender(dto.getGender());
        return toResponseDTO(v);
    }

    private UserResponseDTO toResponseDTO(Visitor visitor) {
        return new UserResponseDTO(visitor.getId(), visitor.getName(), visitor.getAge(), visitor.getGender());
    }

    public boolean removeById(Long id) {
        Visitor visitor = visitorRepository.findById(id);
        if (visitor != null) {
            visitorRepository.remove(visitor);
            return true;
        }
        return false;
    }
}
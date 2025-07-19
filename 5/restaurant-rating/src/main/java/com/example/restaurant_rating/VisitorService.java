package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.UserRequestDTO;
import com.example.restaurant_rating.dto.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public UserResponseDTO save(UserRequestDTO dto) {
        Visitor v = new Visitor(dto.getName(), dto.getAge(), dto.getGender());
        v = visitorRepository.save(v);
        return toDTO(v);
    }

    public boolean removeById(Long id) {
        if (visitorRepository.existsById(id)) {
            visitorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserResponseDTO> findAll() {
        return visitorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findById(Long id) {
        Visitor v = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return toDTO(v);
    }

    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        Visitor v = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        v.setName(dto.getName());
        v.setAge(dto.getAge());
        v.setGender(dto.getGender());
        v = visitorRepository.save(v);
        return toDTO(v);
    }

    public void deleteById(Long id) {
        visitorRepository.deleteById(id);
    }

    private UserResponseDTO toDTO(Visitor v) {
        return new UserResponseDTO(v.getId(), v.getName(), v.getAge(), v.getGender());
    }
}
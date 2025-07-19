package com.example.restaurant_rating;

import com.example.restaurant_rating.dto.UserRequestDTO;
import com.example.restaurant_rating.dto.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @InjectMocks
    private VisitorService visitorService;

    private UserRequestDTO dto;
    private Visitor visitor;

    @BeforeEach
    void setUp() {
        dto = new UserRequestDTO("Alice", 30, "Female");
        visitor = new Visitor("Alice", 30, "Female");
        visitor.setId(1L);
    }

    @Test
    void testSave() {
        when(visitorRepository.save(any(Visitor.class))).thenAnswer(invocation -> {
            Visitor v = invocation.getArgument(0);
            v.setId(1L);
            return v;
        });

        UserResponseDTO response = visitorService.save(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Alice", response.getName());
        assertEquals(30, response.getAge());
        assertEquals("Female", response.getGender());
        verify(visitorRepository, times(1)).save(any(Visitor.class));
    }

    @Test
    void testRemoveById_WhenExists() {
        when(visitorRepository.existsById(1L)).thenReturn(true);

        boolean result = visitorService.removeById(1L);

        assertTrue(result);
        verify(visitorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRemoveById_WhenNotExists() {
        when(visitorRepository.existsById(2L)).thenReturn(false);

        boolean result = visitorService.removeById(2L);

        assertFalse(result);
        verify(visitorRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindAll() {
        Visitor v1 = new Visitor("Bob", 25, "Male");
        v1.setId(2L);
        Visitor v2 = new Visitor("Carol", 28, "Female");
        v2.setId(3L);
        when(visitorRepository.findAll()).thenReturn(Arrays.asList(v1, v2));

        List<UserResponseDTO> list = visitorService.findAll();

        assertEquals(2, list.size());
        assertEquals(2L, list.get(0).getId());
        assertEquals("Bob", list.get(0).getName());
        assertEquals(3L, list.get(1).getId());
        assertEquals("Carol", list.get(1).getName());
    }

    @Test
    void testFindById_WhenFound() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));

        UserResponseDTO response = visitorService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Alice", response.getName());
    }

    @Test
    void testFindById_WhenNotFound() {
        when(visitorRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> visitorService.findById(4L));
    }

    @Test
    void testUpdate_WhenFound() {
        UserRequestDTO updateDto = new UserRequestDTO("AliceUpdated", 31, "Female");
        Visitor updatedVisitor = new Visitor("AliceUpdated", 31, "Female");
        updatedVisitor.setId(1L);

        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(visitorRepository.save(any(Visitor.class))).thenReturn(updatedVisitor);

        UserResponseDTO response = visitorService.update(1L, updateDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("AliceUpdated", response.getName());
        assertEquals(31, response.getAge());
        verify(visitorRepository, times(1)).findById(1L);
        verify(visitorRepository, times(1)).save(any(Visitor.class));
    }

    @Test
    void testUpdate_WhenNotFound() {
        when(visitorRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> visitorService.update(5L, dto));
        verify(visitorRepository, times(1)).findById(5L);
        verify(visitorRepository, never()).save(any(Visitor.class));
    }

    @Test
    void testDeleteById() {
        visitorService.deleteById(10L);

        verify(visitorRepository, times(1)).deleteById(10L);
    }
}

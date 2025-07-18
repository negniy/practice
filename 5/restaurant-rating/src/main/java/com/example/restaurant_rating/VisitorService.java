package com.example.restaurant_rating;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public void save(Visitor visitor) {
        visitorRepository.save(visitor);
    }

    public void remove(Visitor visitor) {
        visitorRepository.remove(visitor);
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }
}
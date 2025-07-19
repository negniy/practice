package com.example.restaurant_rating;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors = new ArrayList<>();
    private long nextId = 1;

    public void save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(nextId++);
        }
        visitors.add(visitor);
    }

    public void remove(Visitor visitor) {
        visitors.remove(visitor);
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors);
    }

    public Visitor findById(Long id) {
        return visitors.stream()
                       .filter(v -> id.equals(v.getId()))
                       .findFirst()
                       .orElse(null);
    }
}

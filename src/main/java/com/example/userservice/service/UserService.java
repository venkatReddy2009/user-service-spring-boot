package com.example.userservice.service;

import com.example.userservice.exception.NotFoundException;
import com.example.userservice.model.User;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(0);

    public List<User> getAll() {
        return new ArrayList<>(store.values());
    }

    public User getById(Long id) {
        User u = store.get(id);
        if (u == null) throw new NotFoundException("User " + id + " not found");
        return u;
    }

    public User create(User user) {
        long id = idSeq.incrementAndGet();
        user.setId(id);
        store.put(id, user);
        System.out.println(user +" "+ store.get(id));
        return user;
    }

    public User update(Long id, User patch) {
        return store.compute(id, (k, existing) -> {
            if (existing == null) throw new NotFoundException("User " + id + " not found");
            if (patch.getName() != null) existing.setName(patch.getName());
            if (patch.getEmail() != null) existing.setEmail(patch.getEmail());
            return existing;
        });
    }

    public void delete(Long id) {
        if (store.remove(id) == null) throw new NotFoundException("User " + id + " not found");
    }
}
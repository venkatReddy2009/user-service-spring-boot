package com.example.userservice.service;

import com.example.userservice.exception.NotFoundException;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) { this.repo = repo; }

    public List<User> getAll() {
        return repo.findAll();
    }

    public User getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User " + id + " not found"));
    }
    @Transactional
    public User create(User user) {
        repo.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exists");
        });
        return repo.save(user);
    }
    @Transactional
    public User update(Long id, User patch) {
        User existing = getById(id);
        if (patch.getName() != null)  existing.setName(patch.getName());
        if (patch.getEmail() != null) existing.setEmail(patch.getEmail());
        return repo.save(existing);
    }
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("User " + id + " not found");
        }
        repo.deleteById(id);
    }
}
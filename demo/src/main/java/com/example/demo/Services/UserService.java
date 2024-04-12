package com.example.demo.Services;

import org.springframework.stereotype.Service;

import com.example.demo.Models.User;
import com.example.demo.Repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void SaveUser(User user) {
        userRepository.save(user);
    }

    public User GetUser(String name) {
        User user = userRepository.findByName(name);
        System.out.println("Found User From Mongo"+user);
        return user;
    }
}

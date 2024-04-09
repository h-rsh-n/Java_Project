package com.example.demo.UserHandler;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Repository userRepository;

    public UserService(Repository userRepository) {
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

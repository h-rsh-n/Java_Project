package com.example.demo.UserHandler;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface Repository extends MongoRepository<User, String> {
    User findByName(String name);
}

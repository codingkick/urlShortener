package com.example.urlShortener.controller;

import com.example.urlShortener.model.User;
import com.example.urlShortener.repoInterface.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/user/{id}")
    public User getUserInfo(@PathVariable String id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        return "Hello "+name+" , how are you?";
    }
}
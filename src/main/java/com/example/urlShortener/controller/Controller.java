package com.example.urlShortener.controller;

import com.example.urlShortener.model.User;
import com.example.urlShortener.repoInterface.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
public class Controller {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/user/{id}")
    public User getUserInfo(@PathVariable String id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("/username/{userName}")
    public User getUserInfoByUsername(@PathVariable String userName) {
        User user1 =  userRepository.findByUserName(userName);
        if(user1 == null)
        {
            User defaultUser = new User();
            defaultUser.setUserName(userName+" not found");
            return defaultUser;
        }
        else
            return user1;
    }

    @PostMapping("/user")
    public String createUser(@RequestBody User user) {
        User findUser = userRepository.findByUserName(user.getUserName());
        System.out.println(findUser);
        if(findUser!=null)
        {
            return "User is already there";
        }
        else {
            User createdUser = userRepository.save(user);
            return "user created with id : "+ createdUser.getId();
        }
    }

    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        return "Hello "+name+" , how are you?";
    }
}
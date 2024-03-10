package com.example.urlShortener.controller;

import com.example.urlShortener.model.Link;
import com.example.urlShortener.model.User;
import com.example.urlShortener.repoInterface.LinkRepository;
import com.example.urlShortener.repoInterface.UserRepository;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;

@RestController
public class Controller {
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;

    @Autowired
    Controller(UserRepository userRepository, LinkRepository linkRepository) {
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
    }

    @PostMapping("/createUser")
    public String createUser(@RequestBody User user) {
        String userName = user.getUserName();

        User u1 = userRepository.findByUserName(userName);
        if(u1 == null) {
            String originalPassword = user.getPassword();
            String hashedPassword = Hashing.sha256().hashString(originalPassword, StandardCharsets.UTF_8).toString();
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return "User is created";
        }
        else {
            return "userName already in use";
        }
    }

    @GetMapping("/shortenUrl")
    public String shortenUrl(@RequestParam String originalUrl,
                             @RequestHeader (value = "userName") String userName,
                             @RequestHeader (value = "password") String password) {
        User userInDb = userRepository.findByUserName(userName);
        if(userInDb != null) {
            String hashedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            if(hashedPassword.equals(userInDb.getPassword()))
            {
                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String currentTimeAsString = currentTime.format(formatter);
                String urlSha256 = Hashing.sha256().hashString(originalUrl + userName + currentTimeAsString, StandardCharsets.UTF_8).toString();
                // Use Base64 encoding to create a shorter representation
                String base64EncodedUrl = Base64.getUrlEncoder().withoutPadding().encodeToString(urlSha256.getBytes());
                String smallUrl;
                smallUrl = base64EncodedUrl.substring(0,7);
                Link link1 = new Link();
                link1.setSmallUrl(smallUrl);
                link1.setOriginalUrl(originalUrl);
                linkRepository.save(link1);
                return smallUrl;
            }
            else
                return "Wrong password";
        }
        else
            return "No such user";
    }
















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

    @GetMapping("/header")
    public String printHeader(@RequestHeader(value = "Authorization") String authHeader) {
        System.out.println("header call");
        return authHeader;
    }

    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        return "Hello "+name+" , how are you?";
    }
}
package com.example.urlShortener.repoInterface;

import com.example.urlShortener.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LinkRepository extends MongoRepository<Link, String> {
}

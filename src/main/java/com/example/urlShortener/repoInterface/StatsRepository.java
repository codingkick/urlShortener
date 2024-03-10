package com.example.urlShortener.repoInterface;

import com.example.urlShortener.model.Stats;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatsRepository extends MongoRepository<Stats, String> {
    Stats findBySmallUrl(String smallUrl);
}

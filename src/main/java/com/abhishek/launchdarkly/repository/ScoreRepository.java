package com.abhishek.launchdarkly.repository;

import com.abhishek.launchdarkly.entity.Score;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends MongoRepository<Score, Long> {
}

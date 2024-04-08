package com.abhishek.launchdarkly.repository;

import com.abhishek.launchdarkly.entity.Exam;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends MongoRepository<Exam, Long> {
}

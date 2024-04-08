package com.abhishek.launchdarkly.service.impl;

import com.abhishek.launchdarkly.entity.Exam;
import com.abhishek.launchdarkly.entity.Score;
import com.abhishek.launchdarkly.entity.Student;
import com.abhishek.launchdarkly.repository.ExamRepository;
import com.abhishek.launchdarkly.repository.ScoreRepository;
import com.abhishek.launchdarkly.repository.StudentRepository;
import com.abhishek.launchdarkly.representation.ScoreRepresentation;
import com.abhishek.launchdarkly.service.DbService;
import com.abhishek.launchdarkly.utils.EntityUtility;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoDbService implements DbService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ScoreRepository scoreRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    public Score save(ScoreRepresentation scoreRepresentation) {

        // upsert exam
        Optional<Exam> exam = examRepository.findById(scoreRepresentation.getExam());

        log.info("Saving exam with id: {}", scoreRepresentation.getExam());
        if (exam.isPresent()) {
            Exam e = exam.get();
            double totalScore = 
                (e.getAverageScore() * e.getNumberOfStudents()) + scoreRepresentation.getScore();
            long numberOfStudents = e.getNumberOfStudents() + 1L;
            e.setAverageScore(totalScore / numberOfStudents);
            e.setNumberOfStudents(numberOfStudents);
            examRepository.save(e);
        } else {
            examRepository.save(EntityUtility.examFromRepresentation(scoreRepresentation));
        }
        log.info("Exam saved...");

        // upsert student
        Optional<Student> student = studentRepository.findById(scoreRepresentation.getStudentId());
        log.info("Saving student with id: {}", scoreRepresentation.getStudentId());
        if (student.isPresent()) {
            Student s = student.get();
            double totalScore =
                (s.getAverageScore() * s.getNumberOfExams()) + scoreRepresentation.getScore();
            long numberOfExams = s.getNumberOfExams() + 1L;
            s.setAverageScore(totalScore / numberOfExams);
            s.setNumberOfExams(numberOfExams);
            studentRepository.save(s);
        } else {
            studentRepository.save(EntityUtility.studentFromRepresentation(scoreRepresentation));
        }
        log.info("Student saved...");

        // insert score
        Score score = EntityUtility.scoreFromRepresentation(scoreRepresentation);
        log.info("Inserting score record {}", score);
        scoreRepository.save(score);
        log.info("Score saved...");

        return score;
    }
}


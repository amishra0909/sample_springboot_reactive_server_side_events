package com.abhishek.launchdarkly.utils;

import com.abhishek.launchdarkly.entity.Exam;
import com.abhishek.launchdarkly.entity.Score;
import com.abhishek.launchdarkly.entity.Student;
import com.abhishek.launchdarkly.representation.ScoreRepresentation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityUtility {

    public static Score scoreFromRepresentation(ScoreRepresentation representation) {
        log.info("Creating score from representation {}", representation);
        Score score = new Score();
        score.setExamId(representation.getExam());
        score.setStudentId(representation.getStudentId());
        score.setScore(representation.getScore());
        return score;
    }

    public static Exam examFromRepresentation(ScoreRepresentation representation) {
        log.info("Creating exam from representation {}", representation);
        Exam exam = new Exam();
        exam.setId(representation.getExam());
        exam.setAverageScore(representation.getScore());
        exam.setNumberOfStudents(1);
        return exam;
    }

    public static Student studentFromRepresentation(ScoreRepresentation representation) {
        log.info("Creating student from representation {}", representation);
        Student student = new Student();
        student.setId(representation.getStudentId());
        student.setAverageScore(representation.getScore());
        student.setNumberOfExams(1);
        return student;
    }
}

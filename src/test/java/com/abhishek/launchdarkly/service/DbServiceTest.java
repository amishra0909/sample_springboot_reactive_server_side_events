package com.abhishek.launchdarkly.service;

import com.abhishek.launchdarkly.config.TestApplicationConfig;
import com.abhishek.launchdarkly.entity.Exam;
import com.abhishek.launchdarkly.entity.Score;
import com.abhishek.launchdarkly.entity.Student;
import com.abhishek.launchdarkly.repository.ExamRepository;
import com.abhishek.launchdarkly.repository.ScoreRepository;
import com.abhishek.launchdarkly.repository.StudentRepository;
import com.abhishek.launchdarkly.representation.ScoreRepresentation;
import com.abhishek.launchdarkly.service.impl.MongoDbService;
import com.abhishek.launchdarkly.utils.EntityUtility;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(classes = {TestApplicationConfig.class, MongoDbService.class})
@RequiredArgsConstructor
public class DbServiceTest {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DbService dbService;

    @Test
    @DisplayName("Storing new documents when neither exam nor student present")
    public void shouldSaveNewDocumentsWhenExamAndStudentAreNotPresent(TestInfo testInfo) {
        log.info("Running test: {}", testInfo.getDisplayName());

        ScoreRepresentation score = new ScoreRepresentation();
        score.setExam(1L);
        score.setStudentId("studentId1");
        score.setScore(0.8F);

        Mockito.when(examRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(studentRepository.findById("studentId1")).thenReturn(Optional.empty());
       
        dbService.save(score);

        Mockito.verify(examRepository, Mockito.times(1)).save(EntityUtility.examFromRepresentation(score));
        Mockito.verify(studentRepository, Mockito.times(1)).save(EntityUtility.studentFromRepresentation(score));
        Mockito.verify(scoreRepository, Mockito.times(1)).save(EntityUtility.scoreFromRepresentation(score));
    }

    @Test
    @DisplayName("Storing same documents when both exam and student are present")
    public void shouldSaveExistingDocumentsWhenExamAndStudentArePresent(TestInfo testInfo) {
        log.info("Running test: {}", testInfo.getDisplayName());

        ScoreRepresentation score = new ScoreRepresentation();
        score.setExam(2L);
        score.setStudentId("studentId2");
        score.setScore(0.8F);

        Exam exam = new Exam();
        exam.setId(2L);
        exam.setAverageScore(0.6F);
        exam.setNumberOfStudents(1L);

        Student student = new Student();
        student.setId("studentId2");
        student.setAverageScore(0.6F);
        student.setNumberOfExams(1L);

        Mockito.when(examRepository.findById(2L)).thenReturn(Optional.of(exam));
        Mockito.when(studentRepository.findById("studentId2")).thenReturn(Optional.of(student));
       
        dbService.save(score);

        double examAverageScore = ((exam.getAverageScore() * exam.getNumberOfStudents()) + score.getScore()) / (exam.getNumberOfStudents() + 1L);
        double studentAverageScore = ((student.getAverageScore() * student.getNumberOfExams()) + score.getScore()) / (student.getNumberOfExams() + 1L);

        exam.setAverageScore(examAverageScore);
        exam.setNumberOfStudents(exam.getNumberOfStudents() + 1L);

        student.setAverageScore(studentAverageScore);
        student.setNumberOfExams(student.getNumberOfExams() + 1L);

        Mockito.verify(examRepository, Mockito.times(1)).save(exam);
        Mockito.verify(studentRepository, Mockito.times(1)).save(student);
        Mockito.verify(scoreRepository, Mockito.times(1)).save(EntityUtility.scoreFromRepresentation(score));
   }

    @Test
    @DisplayName("Storing new student when exam already present")
    public void shouldSaveNewStudentWhenExamIsPresent(TestInfo testInfo) {
        log.info("Running test: {}", testInfo.getDisplayName());

        ScoreRepresentation score = new ScoreRepresentation();
        score.setExam(3L);
        score.setStudentId("studentId3");
        score.setScore(0.8F);

        Exam exam = new Exam();
        exam.setId(3L);
        exam.setAverageScore(0.6F);
        exam.setNumberOfStudents(1L);

        Mockito.when(examRepository.findById(3L)).thenReturn(Optional.of(exam));
        Mockito.when(studentRepository.findById("studentId3")).thenReturn(Optional.empty());
       
        dbService.save(score);

        double examAverageScore = ((exam.getAverageScore() * exam.getNumberOfStudents()) + score.getScore()) / (exam.getNumberOfStudents() + 1L);

        exam.setAverageScore(examAverageScore);
        exam.setNumberOfStudents(exam.getNumberOfStudents() + 1L);

        Mockito.verify(examRepository, Mockito.times(1)).save(exam);
        Mockito.verify(studentRepository, Mockito.times(1)).save(EntityUtility.studentFromRepresentation(score));
        Mockito.verify(scoreRepository, Mockito.times(1)).save(EntityUtility.scoreFromRepresentation(score));

    }

    @Test
    @DisplayName("Storing new documents when neither exam nor student present")
    public void shouldSaveNewExamWhenStudentIsPresent(TestInfo testInfo) {
        log.info("Running test: {}", testInfo.getDisplayName());
 
        ScoreRepresentation score = new ScoreRepresentation();
        score.setExam(4L);
        score.setStudentId("studentId4");
        score.setScore(0.8F);

        Student student = new Student();
        student.setId("studentId4");
        student.setAverageScore(0.6F);
        student.setNumberOfExams(1L);

        Mockito.when(examRepository.findById(4L)).thenReturn(Optional.empty());
        Mockito.when(studentRepository.findById("studentId4")).thenReturn(Optional.of(student));
       
        dbService.save(score);

        double studentAverageScore = ((student.getAverageScore() * student.getNumberOfExams()) + score.getScore()) / (student.getNumberOfExams() + 1L);

        student.setAverageScore(studentAverageScore);
        student.setNumberOfExams(student.getNumberOfExams() + 1L);

        Mockito.verify(examRepository, Mockito.times(1)).save(EntityUtility.examFromRepresentation(score));
        Mockito.verify(studentRepository, Mockito.times(1)).save(student);
        Mockito.verify(scoreRepository, Mockito.times(1)).save(EntityUtility.scoreFromRepresentation(score));

    }

}


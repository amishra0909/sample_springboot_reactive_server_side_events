package com.abhishek.launchdarkly.service;

import com.abhishek.launchdarkly.representation.ScoreRepresentation;
import com.abhishek.launchdarkly.service.impl.InputValidator;
import org.junit.jupiter.api.Test;

public class InputValidatorTest {

    private Validator inputValidator = new InputValidator();

    @Test
    public void testValidStudentId_returnsTrue() {
        ScoreRepresentation rep = new ScoreRepresentation();
        rep.setScore(0.111f);
        rep.setExam(10L);
        rep.setStudentId("ccc111");

        if(!inputValidator.validate(rep)) {
            System.out.println("failed");
        }
    }

    @Test
    public void testInvalidStudentId_returnsFrue() {
        ScoreRepresentation rep = new ScoreRepresentation();
        rep.setScore(0.111f);
        rep.setExam(10L);
        rep.setStudentId("ccc111&-");

        if (inputValidator.validate(rep)) {
            System.out.println("failed");
        }
    }
}




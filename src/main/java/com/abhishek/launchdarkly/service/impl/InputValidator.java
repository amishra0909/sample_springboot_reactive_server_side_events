package com.abhishek.launchdarkly.service.impl;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.abhishek.launchdarkly.service.Validator;
import com.abhishek.launchdarkly.representation.ScoreRepresentation;

public class InputValidator implements Validator {

    private static Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");

    @Override
    public boolean validate(ScoreRepresentation representation) {

        return representation.getScore() >= 0.0 && representation.getScore() <= 1.0
            && representation.getExam() > 0L
            && InputValidator.pattern.matcher(representation.getStudentId()).matches();
    }

}


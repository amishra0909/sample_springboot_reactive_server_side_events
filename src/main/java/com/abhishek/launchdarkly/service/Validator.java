package com.abhishek.launchdarkly.service;

import com.abhishek.launchdarkly.representation.ScoreRepresentation;

public interface Validator {

    boolean validate(ScoreRepresentation representation);
}

package com.abhishek.launchdarkly.service;

import com.abhishek.launchdarkly.entity.Score;
import com.abhishek.launchdarkly.representation.ScoreRepresentation;

public interface DbService {

    Score save(ScoreRepresentation representation);
}

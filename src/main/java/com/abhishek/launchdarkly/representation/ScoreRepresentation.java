package com.abhishek.launchdarkly.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreRepresentation {

    @NonNull
    private long exam;

    @NonNull
    private String studentId;

    @NonNull
    private float score;
}


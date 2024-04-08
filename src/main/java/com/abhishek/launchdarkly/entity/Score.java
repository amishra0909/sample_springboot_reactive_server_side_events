package com.abhishek.launchdarkly.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "scores")
public class Score {

    @Indexed
    @NonNull
    private long examId;

    @Indexed
    @NonNull
    private String studentId;

    @NonNull
    private float score;
}

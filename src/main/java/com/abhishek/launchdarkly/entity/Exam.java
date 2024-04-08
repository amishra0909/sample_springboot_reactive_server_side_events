package com.abhishek.launchdarkly.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "exams")
public class Exam {

    @Id
    @NonNull
    private long id;

    @NonNull
    private double averageScore;

    @NonNull
    private long numberOfStudents;
}


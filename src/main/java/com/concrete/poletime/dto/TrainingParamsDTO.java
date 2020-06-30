package com.concrete.poletime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingParamsDTO {
    private String trainingFrom;
    private String trainingTo;
    private String hall;
    private int personLimit;
    private String type;
    private String level;
}

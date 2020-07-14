package com.concrete.poletime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingParamsDTO {
    @NotNull @NotEmpty
    private String trainingFrom;
    @NotNull @NotEmpty
    private String trainingTo;
    @NotNull @NotEmpty
    private String hall;
    @NotNull
    private int personLimit;
    @NotNull @NotEmpty
    private String type;
    @NotNull @NotEmpty
    private String level;
}

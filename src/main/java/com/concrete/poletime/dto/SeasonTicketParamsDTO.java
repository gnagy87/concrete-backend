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
public class SeasonTicketParamsDTO {
    @NotNull @NotEmpty
    private String email;
    @NotNull @NotEmpty
    private String validFrom;
    @NotNull
    private int amount;
}

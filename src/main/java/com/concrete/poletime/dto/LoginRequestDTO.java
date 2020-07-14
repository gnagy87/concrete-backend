package com.concrete.poletime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDTO {
    @NotNull @NotEmpty
    private String email;
    @NotNull @NotEmpty
    private String password;
}

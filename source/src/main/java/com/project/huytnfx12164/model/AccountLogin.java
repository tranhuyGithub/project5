package com.project.huytnfx12164.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class AccountLogin {
    @Size(min = 5, max = 250)
    @NotEmpty(message = "Username can't be empty!")
    private String email;

    @Size(min = 6, max = 250)
    @NotEmpty(message = "Password can't be empty!")
    private String password;


}
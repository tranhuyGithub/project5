package com.project.huytnfx12164.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class Password {

    @NotEmpty(message = "Current Password can't be empty!")
    private String current;

    @NotEmpty(message = "Password can't be empty!")
    private String newPassword;

    @NotEmpty(message = "Password can't be empty!")
    private String confirm;

}

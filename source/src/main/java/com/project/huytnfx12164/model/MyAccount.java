package com.project.huytnfx12164.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class MyAccount {

    @NotEmpty(message = "Username can't be empty!")
    private String userName;

    @NotEmpty(message = "Phone number can't be empty!")
    @Size(max = 11)
    private String phone;
}

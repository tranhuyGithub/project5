package com.project.huytnfx12164.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class ProcessMoney {

    private int target;
    private int process;

}

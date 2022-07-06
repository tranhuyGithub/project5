package com.project.huytnfx12164.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CHARITY")
public class Charity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID"

    )
    private int id;

    @NotEmpty(message = "Field can't be empty!")
    @Column(name = "NAME")
    private String name;

    @NotEmpty(message = "Field can't be empty!")
    @Column(name = "CONTENT")
    private String content;

    @NotEmpty(message = "Field can't be empty!")
    @Column(name = "DATE_START")
    private String dateStart;

    @NotEmpty(message = "Field can't be empty!")
    @Column(name = "DATE_END")
    private String dateEnd;

    @NotNull(message = "Field can't be empty!")
    @Column(name = "MONEY")
    private int money;

    @NotEmpty(message = "Field can't be empty!")
    @Column(name = "PAY_METHOD")
    private String payMethod;

    @Column(name = "IMG")
    private String img;

    @Column(name = "STATUS")
    private int status;

    @OneToMany(mappedBy = "charity")
    private Set<EachDonation> eachDonationSet ;


}

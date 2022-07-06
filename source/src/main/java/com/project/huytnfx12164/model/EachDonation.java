package com.project.huytnfx12164.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EACHDONATION")
public class EachDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "CHARITY_ID")
    private int charityId;


    @Column(name = "ACCOUNT_ID")
    private int accountId;


    @Column(name = "USERNAME")
    private String username;


    @NotNull(message = "Field can't be empty!")
    @Column(name = "MONEY")
    private int money;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "PAY_METHOD")
    private String payMethod;

    @Column(name = "STATUS")
    private int status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName="ID", insertable=false, updatable=false)
    private Account account;

    @ManyToOne(optional=false)
    @JoinColumn(name = "CHARITY_ID" , referencedColumnName="ID", insertable=false, updatable=false)
    private Charity charity;


}

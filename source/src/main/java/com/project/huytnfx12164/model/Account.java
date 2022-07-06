package com.project.huytnfx12164.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.thymeleaf.standard.expression.Each;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID",  nullable=false)
    private int id;

    @NotNull(message = "Email can't be empty!")
    @NotBlank
    @Size(min = 5, max = 250)
    @Column(name= "EMAIL")
    private String email;

    @NotEmpty(message = "Username can't be empty!")
    @Size(min = 6, max = 250)
    @Column(name= "PASSWORD")
    private String password;

    @NotEmpty(message = "Username can't be empty!")
    @Column(name= "USERNAME")
    private String userName;

    @NotEmpty(message = "Number phone can't be empty!")
    @Size(min =  10 , max = 11)
    @Column(name= "PHONE")
    private String phone;


    @Column(name= "ROLE")
    private int role;


    @Column(name= "STATUS")
    private int status;


    @Column(name= "DATE")
    private LocalDate dateCreate;



    @Column(name = "TOKEN")
    private String token;


    @OneToMany( mappedBy = "account")
    private Set<EachDonation> eachDonationSet ;

}

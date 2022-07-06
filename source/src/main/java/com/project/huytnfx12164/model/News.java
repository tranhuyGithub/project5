package com.project.huytnfx12164.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Blob;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NEWS")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @NotEmpty(message = "Field can't be empty!")
    @Column(name = "TITLE")
    private String title;

    @NotEmpty(message = "Field can't be empty!")
    @Column(name = "CONTENT")
    private String content;


    @Column(name = "DATE")
    private LocalDate newsDate;

    @Column(name = "COUNTVIEW")
    private int countView;


    @Column(name = "IMG")
    private String img;


    @Column(name = "STATUS")
    private int status;


}

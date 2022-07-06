package com.project.huytnfx12164.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsForm {



    private int id;


    private String title;


    private String content;


    private LocalDate newsDate;


    private int countView;


    private MultipartFile img;



    private int status;


}

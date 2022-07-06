package com.project.huytnfx12164.controller;

import com.project.huytnfx12164.model.News;
import com.project.huytnfx12164.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class NewsController {
    @Autowired
    private NewsService serviceN;


    @RequestMapping("/blogPage")
    public String blog(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                       @RequestParam("page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        int pageSize = 6;

        Page<News> resultPage = serviceN.pageBlogs( currentPage , pageSize);
        List<News> news = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/blogPage";
        model.addAttribute("baseUrl", baseUrl);


        return "landingPage/blogs";}
    @RequestMapping("/blogSearch")
    public String searchBlog(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                             @RequestParam("page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        int pageSize = 6;
        if(keyword.equals("")) {return "redirect:/blogPage" ;}
        Page<News> resultPage = serviceN.searchPaginated( keyword , currentPage , pageSize);
        List<News> news = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/searchBlog";
        model.addAttribute("baseUrl", baseUrl);
        return "landingPage/blogs";
    }
    @RequestMapping("/newsDetail")
    public String blogSingle (@RequestParam("id") int id, Model model, HttpServletResponse response) throws IOException {
        News news = serviceN.get(id);
        model.addAttribute("news", news);
        return "landingPage/blog-detail";}
}

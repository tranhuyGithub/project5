package com.project.huytnfx12164.controller;

import com.project.huytnfx12164.model.*;
import com.project.huytnfx12164.service.AccountService;
import com.project.huytnfx12164.service.CharityService;
import com.project.huytnfx12164.service.DonationService;
import com.project.huytnfx12164.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@Controller
public class HomeController {
    @Autowired
    private CharityService service;
    @Autowired
    private HttpServletRequest http;

    @Autowired
    private NewsService serviceN;
    @Autowired
    private AccountService serviceA;


    @Autowired
    private CharityService serviceC;

    @Autowired
    private DonationService serviceD;
    // Display landingPage
    @GetMapping("/")
    public String charityList(Model model){

        List<Charity> charityList = service.listAll();

        model.addAttribute("charityList",charityList );
        return "landingPage/index";
    }

    @GetMapping("/charityDetail")
    public  String chartityDetail(@RequestParam("id") Integer id, Model model, @RequestParam("page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        int pageSize = 5;
        Page<AccountRanking> resultPage = serviceA.rankingDonation(id, currentPage , pageSize);
        List<AccountRanking> accounts = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        String baseUrl = "/manageAccount";
        model.addAttribute("baseUrl", baseUrl);

        int process = service.getProcessMoney(id);
        //
        model.addAttribute("process", process);

        // Get random news
        List<News> newsList = serviceN.listAll();
        int random = (int) (Math.random() * (newsList.size()-1));
        News news = newsList.get(random);
        model.addAttribute("news", news);
        //


        http.getSession().setAttribute("charity_id", id);
        Charity charity = service.get(id);
        model.addAttribute("charity", charity);
        return "landingPage/charity-detail";
    }


    @RequestMapping("/aboutPage")
    public String about(){return "landingPage/about";}

    @RequestMapping("/howItWork")
    public String howItWork(){return "landingPage/how-it-works";}

    @RequestMapping("/donatePage")
    public String donatePage(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                             @RequestParam("page") Optional<Integer> page){

        int currentPage = page.orElse(1);
        int pageSize = 6;

        Page<Charity> resultPage = serviceC.getAll( currentPage , pageSize);
        List<Charity> charityList = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/donatePage";
        model.addAttribute("baseUrl", baseUrl);
        return "landingPage/charities";}

    @RequestMapping("/homeSearch")
    public String finddonatePage(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                             @RequestParam("page") Optional<Integer> page){

        int currentPage = page.orElse(1);
        int pageSize = 6;

        Page<Charity> resultPage = serviceC.searchAll(keyword,  currentPage , pageSize);
        List<Charity> charityList = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/donatePage";
        model.addAttribute("baseUrl", baseUrl);
        return "landingPage/charities";}



}

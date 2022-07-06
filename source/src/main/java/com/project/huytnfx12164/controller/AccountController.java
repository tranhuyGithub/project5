package com.project.huytnfx12164.controller;

import com.project.huytnfx12164.model.*;
import com.project.huytnfx12164.service.AccountService;
import com.project.huytnfx12164.service.CharityService;
import com.project.huytnfx12164.service.DonationService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {
    @Autowired
    private AccountService serviceA;


    @Autowired
    private DonationService serviceD;
    @Autowired
    private CharityService serviceC;

    @Autowired
    private HttpServletRequest http;

    //  User Feature
    // Show profile
    @GetMapping("/profile")
    public String myAccount(
                            @RequestParam("page") Optional<Integer> page,  Model model){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        int currentPage = page.orElse(1);
        int pageSize = 5;
        int id = account.getId();
        Page<UserDonation> resultPage = serviceC.pageUser(id, currentPage , pageSize);
        List<UserDonation> accounts = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        String baseUrl = "/profile";
        model.addAttribute("baseUrl", baseUrl);


        return"user/profile";
    }

    // sh information
    @RequestMapping("/showInfo")
    public String infor(Model model, MyAccount myAccount ){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        myAccount.setPhone(account.getPhone());
        myAccount.setUserName(account.getUserName());
        model.addAttribute("myAccount", myAccount);
        return "user/changeInfo";
    }
    @PostMapping("/changeInfo")
    public String changeInfo(@Valid MyAccount myAccount, Errors errors, Model model , RedirectAttributes redirectAttributes){
        if(null != errors && errors.getErrorCount() > 0){
            model.addAttribute("error","Informatin update failed!!");
            return "user/changeInfo";
        } else{
            Account accountCr = (Account) http.getSession().getAttribute("accountCr");
            int id = accountCr.getId();
            Account account = serviceA.get(id);
            account.setUserName(myAccount.getUserName());
            account.setPhone(myAccount.getPhone());
            serviceA.save(account);
            http.getSession().setAttribute("accountCr", account);
            model.addAttribute("account", account);
            redirectAttributes.addFlashAttribute("success","Informatin update successfully!!");

            return"redirect:/profile";
        }
    }
    // Change password
    @GetMapping("/password")
    public String showPassword(Model model, Password password){
        return"user/changePassword";

    }
    @PostMapping("/changePassword")
    public String changePassWord(@Valid Password password, Errors errors, Model model, RedirectAttributes redirectAttributes){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(errors.hasErrors()){ model.addAttribute("error","\n" +
                "Change password failed!!");
            return"user/changePassword";}
        if(!BCrypt.checkpw(password.getCurrent(), account.getPassword())){
            model.addAttribute("error","\n" +
                    "Incorrect current password!!");
            return"user/changePassword";
        } else if (!password.getNewPassword().equals(password.getConfirm())){
            model.addAttribute("error","\n" +
                    "Incorrect confirm password!!");
            return"user/changePassword";
        } else if(password.getCurrent().equals(password.getNewPassword())){
            model.addAttribute("error","\n" +
                    "The new password cannot be the same current password!!");
            return"user/changePassword";
        }else{
            account.setPassword(BCrypt.hashpw(password.getNewPassword(), BCrypt.gensalt(12)));
            serviceA.save(account);
            http.getSession().setAttribute("accountCr", account);
            redirectAttributes.addFlashAttribute("success","\n" +
                    "Change password successfully!!");
            return "redirect:/profile";
        }

    }




}

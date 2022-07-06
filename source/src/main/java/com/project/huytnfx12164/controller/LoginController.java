package com.project.huytnfx12164.controller;


import com.project.huytnfx12164.model.*;
import com.project.huytnfx12164.service.AccountService;
import com.project.huytnfx12164.service.DonationService;
import com.project.huytnfx12164.service.EmailSenderService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;


@Controller
public class LoginController {
    @Autowired
    private AccountService service;
    @Autowired
    private DonationService serviceD;

    @Autowired
    private HttpServletRequest http;

    @Autowired
    private EmailSenderService emailSenderService;


// Feature Login
    // Display login form
    @GetMapping("/showLogin")
    public String showLogin(AccountLogin accountLogin, Model model) {
        return "login/login";
    }
    // Login
    @PostMapping("/login")
    public String dologin(@ModelAttribute("accountLogin") @Validated AccountLogin accountLogin, BindingResult result,Model model ) {
        if (result.hasErrors()){
            model.addAttribute("error", "Sign up failed!!");
            return "login/login";}
        Account account = service.getAccountByEmail(accountLogin.getEmail());
        if (account == null){
            model.addAttribute("error","Account does not exist!");
            return "login/login";}
        if (!BCrypt.checkpw(accountLogin.getPassword(), account.getPassword())){
            model.addAttribute("error","Password mismatch!");
            return "login/login";
        }
        if(account.getStatus() == 2 ){
            model.addAttribute("error", "Your account is disable, please check your email to active!");
            return "login/login";
        }

        http.getSession().setAttribute("accountCr", account);
        model.addAttribute("username", account.getUserName());
        String url = (String) http.getSession().getAttribute("url");
        if( url != null){return "redirect:"+url;}
        model.addAttribute("success", "You have signed in successfully!");

        return "redirect:/";
    }

// Feature Register and confirm new account
    // Display register form
    @GetMapping("/showRegister")
    public String showRegister(Account account, Model model) {
        return "login/register";
    }
    // Register
    @PostMapping("/register")
    public String addAccount(@Valid Account account , Errors errors, Model model,AccountLogin accountLogin){
        if (errors.hasErrors()){
            model.addAttribute("error","Create account failed!!");
            return "login/register";
        } else {
        Account existingAccount = service.getAccountByEmail(account.getEmail());

        if(existingAccount != null){
            model.addAttribute("error", "This email already exists!");
            return "login/register";

        } else {

            String url = UUID.randomUUID().toString();
            account.setToken(url);
            account.setRole(1);
            account.setStatus(2);
            ZoneId zonedId = ZoneId.of( "America/Montreal" );
            LocalDate todayD = LocalDate.now( zonedId );
           // String today = todayD.toString();
            account.setDateCreate(todayD);
            account.setPassword(BCrypt.hashpw(account.getPassword(),BCrypt.gensalt(12)));
            service.save(account);
          //  String email = account.getEmail();

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(account.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("huytnfx12164@funix.edu.vn");
            mailMessage.setText("To confirm your account, please click here: "+"http://localhost:8080/confirm-account?token="+url);
            emailSenderService.sendEmail(mailMessage);

            model.addAttribute("success","You have signed up successfully!");
        return "login/login";}}
    }
    // Confirm new account
    @RequestMapping(value = "/confirm-account" )
    public String confirmAccount(@RequestParam("token")String token,Model model, Account account,AccountLogin accountLogin ){
        if(token != null){

            account = service.getAccountByToken(token);
            account.setStatus(1);
            service.save(account);
            accountLogin.setEmail(account.getEmail());
            model.addAttribute("success", "AccountVerifited");
        } else {
            model.addAttribute("error", "The link is invalid or broken!");


        }
        return "login/login";
    }


// Feature Logout

    @GetMapping(value = "/logout")
    public String logout(Model model){
        http.getSession().invalidate();
        return "redirect:/";
    }


}

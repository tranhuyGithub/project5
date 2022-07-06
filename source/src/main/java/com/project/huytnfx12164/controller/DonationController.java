package com.project.huytnfx12164.controller;

import com.project.huytnfx12164.model.Account;

import com.project.huytnfx12164.model.AccountLogin;
import com.project.huytnfx12164.model.Charity;
import com.project.huytnfx12164.model.EachDonation;
import com.project.huytnfx12164.service.CharityService;
import com.project.huytnfx12164.service.DonationService;
import com.project.huytnfx12164.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
public class DonationController {

    @Autowired
    private  DonationService service;
    @Autowired
    private CharityService serviceC;

    @Autowired
    private HttpServletRequest http;

    @Autowired
    private EmailSenderService emailSenderService;





    @GetMapping("/donationRecord/{id}")
    public String donationRecord(@PathVariable("id") int id, Model model){
        List<EachDonation> donationList = service.findByCharityId(id);
        model.addAttribute("donationList",donationList);
        return "admin/manage_donation";
    }

    @GetMapping("/donationList/{id}")
    public String charityManage(Model model){
        List<EachDonation> donationList = service.listAll();
        model.addAttribute("donationList",donationList );
        return "admin/donationList";
    }


    @GetMapping("/donationForm")
    public String donationForm(@RequestParam("id") int id,  EachDonation eachDonation, Model model, AccountLogin accountLogin ){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null) {

            String url = http.getRequestURI()+"?id=" + id ;
            http.getSession().setAttribute("url", url);
            model.addAttribute("success", "Please login before donate!!!");
            return "login/login";
        }

        Charity charity = serviceC.get(id);
        model.addAttribute("charity", charity);
        model.addAttribute("username", account.getUserName());

        return ("form/donationForm");
    }


    @PostMapping("/donate")
    public String createDonation(@Valid EachDonation eachDonation, BindingResult result, @RequestParam("id" ) int id, Model model, RedirectAttributes redirect){
        Account account = (Account) http.getSession().getAttribute("accountCr");

        // validation input each donation
        if (result.hasErrors()){
            Charity charity = serviceC.get(id);
            model.addAttribute("charity", charity);
            model.addAttribute("username", account.getUserName());
            model.addAttribute("error","Donate failed!!");

        }else{
            // Add property
        eachDonation.setUsername(account.getUserName());
        eachDonation.setAccountId(account.getId());

        eachDonation.setCharityId(id);

        // Get date today
        ZoneId zonedId = ZoneId.of( "America/Montreal" );
        LocalDate today = LocalDate.now( zonedId );
        eachDonation.setDate(today);
        eachDonation.setStatus(1);
        service.save(eachDonation);
            // Send mail notification
            EachDonation tempDonation = service.getTemp(account.getId());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(account.getEmail());
            mailMessage.setSubject("Complete Donation!");
            mailMessage.setFrom("huytnfx12164@funix.edu.vn");
            mailMessage.setText("Thank for donated our charity. Transaction ID: "+ tempDonation.getId()+ ", Name public: " + account.getUserName()+", money : "+ tempDonation.getMoney() + " by " + tempDonation.getPayMethod()+"." );
            emailSenderService.sendEmail(mailMessage);

            Charity charity = serviceC.get(id);
            model.addAttribute("charity", charity);
            model.addAttribute("username", account.getUserName());
            model.addAttribute("tempDonation",tempDonation);
            model.addAttribute("success","Donate successfully!!");}
        return "form/donationForm";
    }
}

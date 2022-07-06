package com.project.huytnfx12164.controller;

import com.project.huytnfx12164.config.FileStorageProperties;
import com.project.huytnfx12164.model.*;
import com.project.huytnfx12164.service.*;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Controller
public class AdminController {
    @Autowired
    private CharityService serviceC;
    @Autowired
    private HttpServletRequest http;
    @Autowired
    private AccountService serviceA;
    @Autowired
    private NewsService serviceN;
    @Autowired
    private DonationService serviceD;



    // Display admin Dashboard
    @RequestMapping("/dashboard")
    public String dashboardAdmin(Model model){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}

        model.addAttribute("totalCharity",serviceC.total() );
        model.addAttribute("totalAccount",serviceA.total() );
        model.addAttribute("totalDonation",serviceD.total() );
        model.addAttribute("totalNews",serviceN.total() );
        model.addAttribute("charityListDB", serviceC.topList());
        model.addAttribute("accountListDB", serviceA.topList());
        model.addAttribute("newsList", serviceN.topList());
        return "admin/dashboard";}


    //Manage Charity
    // Display list charity AdminPage

    @GetMapping("/manageCharity")
    public String charityManage(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                                @RequestParam("page") Optional<Integer> page){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        int currentPage = page.orElse(1);
        int pageSize = 5;
        Page<CharityMN> resultPage = serviceC.findPaginated(currentPage , pageSize);
        List<CharityMN> charities = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        String baseUrl = "/manageCharity";
        model.addAttribute("baseUrl", baseUrl);
        return "admin/manage-charity";
    }
    // Search Charrity
    @RequestMapping("/searchCharity")
    public String CharitySearch(  Model model, @RequestParam(name = "keyword") String keyword,
                                        @RequestParam("page") Optional<Integer> page){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        int currentPage = page.orElse(1);
        int pageSize = 5;
        if(keyword.equals("")) {return "redirect:/manageCharity" ;}
        Page<CharityMN> resultPage = serviceC.searchPaginated( keyword , currentPage , pageSize);
        List<CharityMN> charities = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/searchCharity";
        model.addAttribute("baseUrl", baseUrl);
        return "admin/manage-charity";
    }

    //Show careatePage and Create new charity
    @GetMapping("/newCharity")
    public String showCreateCharity(Charity charity){

        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        return "form/create-charity";
    }
    @PostMapping("/addCharity")
    public String createCharity(@RequestParam("image") MultipartFile file, @Valid Charity charity, Errors errors, Model model, RedirectAttributes redirectAttributes) throws IOException {
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        if (null != errors && errors.getErrorCount() > 0){
            model.addAttribute("error","Charity create failed!!");
            return "form/create-charity";
        }
        String fileName =  StringUtils.cleanPath(file.getOriginalFilename());
        charity.setImg(Base64.getEncoder().encodeToString(file.getBytes()));
        serviceC.save(charity);
        redirectAttributes.addFlashAttribute("success","Charity created successfully!!");
        return "redirect:/manageCharity";
    }

    // Show update_Page update charity
    @GetMapping ("/editCharity")
    public String editcharity( @RequestParam("id") Integer id, Model model){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        Charity charity = serviceC.get(id);
        model.addAttribute("charity", charity);

        return "form/update-charity";
    }
    //Update charity
    @PostMapping("/updateCharity")
    public String updateCharity(@RequestParam("image") MultipartFile file, @RequestParam("id") Integer id, @Valid Charity charity, Errors errors, Model model, RedirectAttributes redirectAttributes) throws IOException {
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        if(null != errors && errors.getErrorCount() > 0){


            redirectAttributes.addFlashAttribute("error","Charity update failed!!");
            return "form/update-charity";
        }
        if (file.isEmpty()){
            charity.setImg(serviceC.get(id).getImg());
        }else{
            charity.setImg(Base64.getEncoder().encodeToString(file.getBytes()));
        }
            charity.setId(id);
            serviceC.save(charity);
        redirectAttributes.addFlashAttribute("success","Charity updated successfully!!");
        return "redirect:/manageCharity";
    }
    // Delete charrity
    @GetMapping(value = "/deleteCharity")
    public String delete(@RequestParam("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        CharityMN charityMN = serviceC.findbyId(id);
        int process = charityMN.getProcess();
        if(process > 0 ){
           redirectAttributes.addFlashAttribute("error","Delete failed, Charity was donated!!");
            return "redirect:/manageCharity";

        }
        serviceC.delete(id);
        redirectAttributes.addFlashAttribute("success","Charity deleted successfully!!");

        return "redirect:/manageCharity";
    }
    @RequestMapping(value = "/delMultiCharity/{ids}")
    public  String delMultiCharity(@PathVariable("ids") Integer[] myArray , RedirectAttributes redirectAttributes ){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        for(int i = 0 ; i< myArray.length; i ++ ){
            CharityMN charity =  serviceC.findbyId(myArray[i]);
            if(charity.getProcess() > 0){
                redirectAttributes.addFlashAttribute("error","Charity deleted failed, you just can delete charity when it has not been donated yet !!");
                return "redirect:/manageCharity";
            }
        }
        serviceC.delMultiple(myArray);
        redirectAttributes.addFlashAttribute("success","Donations deleted successfully!!");
        return "redirect:/manageCharity";
    }


    //Manage Account
    //Show manage-account page
    @RequestMapping("/manageAccount")
    public String showManageAccount(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                                    @RequestParam("page") Optional<Integer> page){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        int currentPage = page.orElse(1);
        int pageSize = 5;
        Page<AccountDashboard> resultPage = serviceA.findPaginated(currentPage , pageSize);
        List<AccountDashboard> accounts = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        String baseUrl = "/manageAccount";
        model.addAttribute("baseUrl", baseUrl);

        return "admin/manage-account";
    }
    @RequestMapping("/searchAccount")
    public String searchAccount( Model model,@RequestParam(name = "keyword" ) String keyword,
                                 @RequestParam("page") Optional<Integer> page){
        Account account = (Account) http.getSession().getAttribute("accountCr");
        if(account == null || account.getRole() == 1){return "landingPage/error";}
        int currentPage = page.orElse(1);
        int pageSize = 5;
        if(keyword.equals("")) {return "redirect:/manageAccount" ;}
        Page<AccountDashboard> resultPage = serviceA.searchPaginated( keyword , currentPage , pageSize);
        List<AccountDashboard> accounts = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/searchAccount";
        model.addAttribute("baseUrl", baseUrl);
        return "admin/manage-account";

    }


    //Show careatePage and Create new account
    @GetMapping("/newAccount")
    public String showCreateAccount(Account account){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        return "form/create-account";
    }
    @PostMapping("/addAccount")
    public String createAccount(@Valid Account account, Errors errors, Model model, RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        if (null != errors && errors.getErrorCount() > 0){
            model.addAttribute("error","Account create failed!!");
            return "form/create-account";
        }

        if(serviceA.getAccountByEmail(account.getEmail()) != null){
            model.addAttribute("error","Account create failed, Email already exists!!");
            return "form/create-account";
        }
        ZoneId zonedId = ZoneId.of( "America/Montreal" );
        LocalDate todayD = LocalDate.now( zonedId );
        account.setDateCreate(todayD);
        account.setPassword(BCrypt.hashpw(account.getPassword(),BCrypt.gensalt(12)));
        serviceA.save(account);
        redirectAttributes.addFlashAttribute("success","Account created successfully!!");
        return "redirect:/manageAccount";
    }

    // Show update_Page update Account
    @GetMapping ("/editAccount")
    public String editAccount( @RequestParam("id") Integer id, Model model){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        Account account = serviceA.get(id);
        model.addAttribute("account", account);

        return "form/update-account";
    }
    //Update account
    @PostMapping("/updateAccount")
    public String updateAccount(@RequestParam("id") Integer id, @Valid Account account, Errors errors, Model model,RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        if(null != errors && errors.getErrorCount() > 0){

            model.addAttribute("error","Account update failed!!");
            return "form/update-account";
        } else
            account.setId(id);
            account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt(12)));
            serviceA.save(account);
        redirectAttributes.addFlashAttribute("success","Account updated successfully!!");
        return "redirect:/manageAccount";
    }
    // Delete account
    @GetMapping(value = "/deleteAccount")
    public String deleteAccount( @RequestParam("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        AccountDashboard accountDashboard = serviceA.findbyId(id);
        if(accountDashboard.getMoney() > 0 ){
            redirectAttributes.addFlashAttribute("error","Delete failed, Account donated!!");

            return "redirect:/manageAccount";}
        serviceA.delete(id);
        redirectAttributes.addFlashAttribute("success","Account deleted successfully!!");

        return "redirect:/manageAccount";
    }

    @RequestMapping(value = "/delMultiAccount/{ids}")
    public  String delMultiAccount(@PathVariable("ids") Integer[] myArray , RedirectAttributes redirectAttributes ){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        for(int i = 0 ; i< myArray.length; i ++ ){
            AccountDashboard account =  serviceA.findbyId(myArray[i]);
            if(account.getMoney() > 0){
                redirectAttributes.addFlashAttribute("error","Account deleted failed, you just can delete account when it has not donated yet !!");
                return "redirect:/manageAccount";
            }
        }
        serviceA.delMultiple(myArray);
        redirectAttributes.addFlashAttribute("success","Account deleted successfully!!");
        return "redirect:/manageAccount";
    }


    @RequestMapping(value = "/delMultiDonation/{ids}")
    public  String delMultiDonation(@PathVariable("ids") Integer[] myArray , RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
    serviceD.delMultiple(myArray);
        redirectAttributes.addFlashAttribute("success","Donations deleted successfully!!");
        return "redirect:/manageDonation";
    }


    //Manage Donation
    // Show record Donation

    @RequestMapping("/manageDonation")
    public String showManageEachDonation(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                                         @RequestParam("page") Optional<Integer> page){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        int currentPage = page.orElse(1);
        int pageSize = 5;
        Page<EachDonation> resultPage = serviceD.findPaginated(currentPage , pageSize);
        List<EachDonation> donations = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        String baseUrl = "/manageDonation";
        model.addAttribute("baseUrl", baseUrl);
        List<Integer> idDel = null;
        model.addAttribute("idDel", idDel);


        return "admin/manage-donation";
    }
    @RequestMapping("/searchDonation")
    public String search(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                         @RequestParam("page") Optional<Integer> page) {
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        int currentPage = page.orElse(1);
        int pageSize = 5;
        if(keyword.equals("")) {return "redirect:/manageDonation" ;}
        Page<EachDonation> resultPage = serviceD.searchPaginated( keyword , currentPage , pageSize);
        List<EachDonation> donations = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/searchDonation";
        model.addAttribute("baseUrl", baseUrl);
        return "admin/manage-donation";
    }
    //Show careatePage and Create new Donation
    @GetMapping("/newDonation")
    public String showCreateDonation(EachDonation eachDonation){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        return "form/create-donation";
    }
    @PostMapping("/addDonation")
    public String createDonation(@Valid EachDonation eachDonation, Errors errors, Model model, RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        if (null != errors && errors.getErrorCount() > 0){
            model.addAttribute("error","Donation create failed!!");
            return "form/create-donation";
        }
        ZoneId zonedId = ZoneId.of( "America/Montreal" );
        LocalDate today = LocalDate.now( zonedId );
        eachDonation.setDate(today);
        Account account = serviceA.get(eachDonation.getAccountId());
        eachDonation.setUsername(account.getUserName());
        serviceD.save(eachDonation);
        redirectAttributes.addFlashAttribute("success","Donation created successfully!!");
        return "redirect:/manageDonation";
    }

    // Show update_Page update Donation
    @GetMapping ("/editDonation")
    public String editDonation( @RequestParam("id") Integer id, Model model){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        EachDonation eachDonation = serviceD.get(id);
        model.addAttribute("eachDonation", eachDonation);

        return "form/update-donation";
    }
    //Update donation
    @PostMapping("/updateDonation")
    public String updateDonation(@RequestParam("id") Integer id, @Valid EachDonation eachDonation, Errors errors, Model model, RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        if(null != errors && errors.getErrorCount() > 0){

            model.addAttribute("error","Donation update failed!!");
            return "form/update-donation";
        } else
            eachDonation.setId(id);
        Account account = serviceA.get(eachDonation.getAccountId());
        eachDonation.setUsername(account.getUserName());

            serviceD.save(eachDonation);
        redirectAttributes.addFlashAttribute("success","Donation updated successfully!!");
        return "redirect:/manageDonation";
    }
    // Delete donation
    @GetMapping(value = "/deleteDonation")
    public String deleteDonation( @RequestParam("id") Integer id, Model modelm , RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        serviceD.delete(id);
        redirectAttributes.addFlashAttribute("success","Donation deleted successfully!!");

        return "redirect:/manageDonation";
    }
    //Manage News
    // Show record news list
    @RequestMapping("/manageNews")
    public String showManageNews(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                                 @RequestParam("page") Optional<Integer> page){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        int currentPage = page.orElse(1);
        int pageSize = 5;

        Page<News> resultPage = serviceN.findPaginated( currentPage , pageSize);
        List<News> news = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/manageNews";
        model.addAttribute("baseUrl", baseUrl);

        return "admin/manage-news";
    }

    //News search
    @PostMapping("/searchNews")
    public String newsSearch(Model model,@RequestParam(name = "keyword",required = false) String keyword,
                             @RequestParam("page") Optional<Integer> page){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        int currentPage = page.orElse(1);
        int pageSize = 5;
        if(keyword.equals("")) {return "redirect:/manageNews" ;}
        Page<News> resultPage = serviceN.searchPaginated( keyword , currentPage , pageSize);
        List<News> news = resultPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("totalItems", resultPage.getTotalElements());
        model.addAttribute("categoryPage",resultPage);
        model.addAttribute("keyword",keyword);
        String baseUrl = "/searchNews";
        model.addAttribute("baseUrl", baseUrl);
        return "admin/manage-news";
    }
    //Show careatePage and Create new news
    @GetMapping("/newNews")
    public String showCreateNews(News news){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        return "form/create-news";
    }
    @PostMapping("/addNews")
    public String createNews(@RequestParam("image") MultipartFile file,@Valid News news, Errors errors, Model model, RedirectAttributes redirectAttributes) throws IOException {
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        if (null != errors && errors.getErrorCount() > 0) {
            model.addAttribute("error","News create failed!!");
            return "form/create-news";
        }

            ZoneId zonedId = ZoneId.of( "America/Montreal" );
            LocalDate today = LocalDate.now( zonedId );
            news.setNewsDate(today);
            String fileName =  StringUtils.cleanPath(file.getOriginalFilename());
            news.setImg(Base64.getEncoder().encodeToString(file.getBytes()));
            serviceN.save(news);

        redirectAttributes.addFlashAttribute("success","News created successfully!!");
        return "redirect:/manageNews";
    }

    // Show update_Page update news
    @RequestMapping ("/editNews")
    public String editNews( @RequestParam("id") Integer id, Model model){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        News news = serviceN.get(id);
        model.addAttribute("news", news);

        return "form/update-news";
    }
    //Update news
    @PostMapping("/updateNews")
    public String updateNews( @RequestParam("id") Integer id, @RequestParam("image") MultipartFile file, @Valid News news, Errors errors, Model model, RedirectAttributes redirectAttributes
            ) throws IOException {
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        if(null != errors && errors.getErrorCount() > 0){

            model.addAttribute("error","News update failed!!");
            return "form/update-news";
        }
        if (!file.isEmpty())
        {news.setImg(Base64.getEncoder().encodeToString(file.getBytes()));
        } else{news.setImg(serviceN.get(id).getImg());}
            news.setId(id);

            serviceN.save(news);
        redirectAttributes.addFlashAttribute("success","News updated successfully!!");
        return "redirect:/manageNews";
    }
    // Delete news
    @GetMapping(value = "/deleteNews")
    public String deleteNews( @RequestParam("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        serviceN.delete(id);
        redirectAttributes.addFlashAttribute("success","News deleted successfully!!");

        return "redirect:/manageNews";
    }
    @RequestMapping(value = "/delMultiNews/{ids}")
    public  String delMultiNews(@PathVariable("ids") Integer[] myArray , RedirectAttributes redirectAttributes){
        Account account1 = (Account) http.getSession().getAttribute("accountCr");
        if(account1 == null || account1.getRole() == 1){return "landingPage/error";}
        serviceN.delMultiple(myArray);
        redirectAttributes.addFlashAttribute("success","News deleted successfully!!");
        return "redirect:/manageNews";
    }
}

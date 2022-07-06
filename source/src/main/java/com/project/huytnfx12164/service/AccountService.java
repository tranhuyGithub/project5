package com.project.huytnfx12164.service;

import com.project.huytnfx12164.model.*;
import com.project.huytnfx12164.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
public class AccountService {
    @Autowired
    private AccountRepository repo;



    public List<Account> listAll(){
        return repo.findAll();
    }
    public Account getAccountByEmail(String email){return repo.getAccountByEmail(email);}
    public Account getAccountByToken(String token){return repo.getAccountByToken(token);}


    public Account get(int id){
        return repo.findById(id).get();
    }

    public void save(Account account){
        repo.save(account);
    }




    public void delete(int id ){
        repo.deleteById(id);
    }

    public int total() {return repo.total();}

    public List<AccountDashboard> topList(){return repo.topList();}
    public AccountDashboard findbyId(int id){
        return repo.findbyId(id);
    }
    public Page<AccountDashboard> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.findPaginated(pageable);}

    public Page<AccountDashboard> searchPaginated(String keyword, int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.searchPaginated(keyword, pageable);}

    public Page<AccountRanking> rankingDonation(int  id , int pageNo, int pageSize) {
       Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.rankingDonate(id ,  pageable);}

    public void delMultiple(Integer[] configId){
        repo.delMultiple(Arrays.asList(configId));
    }
}

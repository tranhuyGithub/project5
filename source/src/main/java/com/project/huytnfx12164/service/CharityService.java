package com.project.huytnfx12164.service;

import com.project.huytnfx12164.model.*;
import com.project.huytnfx12164.repository.CharityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class CharityService {
    @Autowired
    private CharityRepository repo;

    public List<Charity> listAll(){
        return repo.listAll();
    }

    public Charity get(int id){
        return repo.findById(id).get();
    }

    public void save(Charity charity){
        repo.save(charity);
    }

    public List<Charity> search(String keyword){
        return repo.findByKeyword(keyword);
    }

    public void delete(int id ){
        repo.deleteById(id);
    }
    public int getProcessMoney(int id){ return repo.process(id);}
    public int total() {return repo.total();}
    public List<CharityDashboard> topList() { return repo.topList();}

    public Page<Charity> getAll(int pageNo,  int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.findAll(pageable);
    }

    public Page<Charity> searchAll(String keyword, int pageNo,  int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.searchAll(keyword , pageable);
    }
    public Page<CharityMN> findPaginated(int pageNo,  int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.findPaginated(pageable);}

    public Page<CharityMN> searchPaginated(String keyword, int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.pageSearch(keyword, pageable);}

    public CharityMN findbyId(int id) {return  repo.findbyID(id);}

    public Page<UserDonation> pageUser(int id, int pageNo,  int pageSize ){
        Pageable pageable  = PageRequest.of(pageNo - 1, pageSize);
        return  repo.pageUser(id , pageable);
    }
    public void delMultiple(Integer[] configId){
        repo.delMultiple(Arrays.asList(configId));
    }
}

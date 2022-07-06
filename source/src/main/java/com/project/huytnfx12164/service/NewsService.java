package com.project.huytnfx12164.service;

import com.project.huytnfx12164.model.Account;
import com.project.huytnfx12164.model.EachDonation;
import com.project.huytnfx12164.model.News;
import com.project.huytnfx12164.repository.NewsRepository;
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
public class NewsService {
    @Autowired
    private  NewsRepository repo;

    public List<News> listAll(){
        return repo.getAll();
    }

    public News get(int id){
        return repo.findById(id).get();
    }

    public void save(News news){
        repo.save(news);
    }

    public List<News> searchByKeyword(String keyword){
        return repo.findByKeyword(keyword);
    }
    public int total() {return repo.total();}
    public List<News> topList(){return repo.topList();}
    public void delete(int  id) {repo.deleteById(id);}
    public News findByTitle(String title) {
        return repo.findByTitle(title);
    }

    public Page<News> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.page(pageable);}

    public Page<News> pageBlogs(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.pageBlogs(pageable);}

    public Page<News> searchPaginated(String keyword, int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.pageSearch(keyword, pageable);}

    public void delMultiple(Integer[] configId){
        repo.delMultiple(Arrays.asList(configId));
    }


}

package com.project.huytnfx12164.service;

import com.project.huytnfx12164.model.CharityMN;
import com.project.huytnfx12164.model.EachDonation;
import com.project.huytnfx12164.repository.EachDonationRepository;
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
public class DonationService {

    @Autowired
    private EachDonationRepository repo;

    public List<EachDonation> listAll(){
        return repo.findAll();
    }
    public List<EachDonation> findByAccountId(int id){return repo.findByAccountId(id);}

    public Page<EachDonation> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.findPaginated(pageable);}

    public Page<EachDonation> searchPaginated(String keyword, int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repo.pageSearch(keyword, pageable);}

    public void delete(int  id) {repo.deleteById(id);}
   public void delMultiple(Integer[] configId){
      repo.delMultiple(Arrays.asList(configId));
   }

   public EachDonation getTemp(int id ){
        return (EachDonation) repo.getIdDonation(id);
   }
    public List<EachDonation> findByCharityId(int id){return repo.findByCharityId(id);}
    public List<EachDonation> rankingByCharityId(int id){return repo.rankingByCharityId(id);}
    public int sumMoney(int id ){return repo.sumMoney(id);}

    public EachDonation get(int id){
        return repo.findById(id).get();
    }

    public void save(EachDonation eachDonation){
        repo.save(eachDonation);
    }

    public List<EachDonation> search(String keyword){
        return repo.findByKeyword(keyword);
    }
    public int total() {return repo.total();}

}

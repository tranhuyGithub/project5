package com.project.huytnfx12164.repository;


import com.project.huytnfx12164.model.EachDonation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface EachDonationRepository extends JpaRepository<EachDonation, Integer> {
    @Query(value = "select * from EACHDONATION where USERNAME LIKE %:keyword% or MONEY like %:keyword% or DATE like %:keyword% or PAY_METHOD like %:keyword%", nativeQuery = true)
    List<EachDonation> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from EACHDONATION where ACCOUNT_ID = :id order by DATE DESC", nativeQuery = true)
    List<EachDonation> findByAccountId(@Param("id") int id);
    @Query(value = "select top 1 * from EACHDONATION where ACCOUNT_ID = :id order by ID DESC", nativeQuery = true)
    EachDonation getIdDonation(@Param("id") int id);

    @Query(value = "select * from EACHDONATION where CHARITY_ID = :id order by DATE DESC", nativeQuery = true)
    List<EachDonation> findByCharityId(@Param("id") int id);

    @Query(value = "select * from EACHDONATION order by DATE DESC",
            countQuery = "SELECT COUNT(*) FROM EACHDONATION ", nativeQuery = true)
    Page<EachDonation> page(Pageable pageable);

    @Query(value = "select * from EACHDONATION where CHARITY_ID = :id order by MONEY DESC", nativeQuery = true)
    List<EachDonation> rankingByCharityId(@Param("id") int id);

    @Query(value = "select round(cast(sum(MONEY) as int), 0 ) from EACHDONATION where CHARITY_ID = :id", nativeQuery = true)
    int sumMoney(@Param("id") int id);
    @Query(value = "select  COUNT( distinct ID) from EACHDONATION where STATUS = 1", nativeQuery = true)
    int total();

    @Query(value = "select * from EACHDONATION where USERNAME LIKE %:keyword% or MONEY like %:keyword% or DATE like %:keyword% or PAY_METHOD like %:keyword% order by DATE DESC"
            , countQuery = "select count (distinct ID) from EACHDONATION where USERNAME LIKE %:keyword% or MONEY like %:keyword% or DATE like %:keyword% or PAY_METHOD like %:keyword%", nativeQuery = true)
    Page<EachDonation> pageSearch(@Param("keyword") String keyword,Pageable pageable);

    @Query(value = "select * from EACHDONATION order by DATE DESC",
            countQuery = "select count( distinct ID) from EACHDONATION",
             nativeQuery = true)
    Page<EachDonation> findPaginated(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM EACHDONATION where ID in (?1) ", nativeQuery = true)
    void delMultiple(@Param("ID") List<Integer> configID);

}

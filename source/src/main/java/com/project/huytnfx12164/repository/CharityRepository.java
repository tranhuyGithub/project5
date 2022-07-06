package com.project.huytnfx12164.repository;

import com.project.huytnfx12164.model.Charity;
import com.project.huytnfx12164.model.CharityDashboard;
import com.project.huytnfx12164.model.CharityMN;
import com.project.huytnfx12164.model.UserDonation;
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
public interface CharityRepository extends JpaRepository<Charity, Integer> {

    @Query(value = "select * from CHARITY where NAME LIKE %:keyword% or CONTENT like %:keyword% or DATE_START like %:keyword% or DATE_END like %:keyword% or MONEY like %:keyword% or PAY_METHOD like %:keyword%", nativeQuery = true)
    List<Charity> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select cast(ISNULL(sum(e.MONEY), '0') as bigint)   from CHARITY c inner join EACHDONATION e on c.ID = e.CHARITY_ID  where c.ID= :id ", nativeQuery = true)
    Integer process(@Param("id") int id);
    @Query(value = "select  COUNT( distinct ID) from CHARITY ", nativeQuery = true)
    int total();
    @Query(value = "Select * from CHARITY Where STATUS = 1 Order by DATE_START", nativeQuery = true)
    List<Charity> listAll() ;
    @Query(value = "SELECT TOP 5 c.ID , c.NAME, cast(c.DATE_START  as char) as DATE ,  sum(e.Money) as PROCESS, c.MONEY FROM CHARITY c join EACHDONATION e on c.ID = e.CHARITY_ID group by c.ID, c.NAME, c.DATE_START, c.MONEY ORDER BY sum(e.Money)  DESC", nativeQuery = true)
    List<CharityDashboard> topList();

    @Query(value = "SELECT  c.ID , NAME, cast(c.DATE_START  as char) as DATE ,  cast(ISNULL(sum(e.Money), '0') as bigint)as PROCESS, c.MONEY ,CAST(CASE WHEN c.STATUS = 1 THEN 'Acctive' ELSE 'Non Acctive' END AS nvarchar) as STATUS  FROM CHARITY c left Join EACHDONATION e on c.ID = e.CHARITY_ID group by c.ID, NAME, DATE_START, c.MONEY , c.STATUS ORDER BY c.DATE_START  DESC", nativeQuery = true)
    List<CharityMN> listMN();

    @Query(value = "Select * from CHARITY Where STATUS = 1 Order by DATE_START",
            countQuery = "Select count(*) from CHARITY Where STATUS = 1", nativeQuery = true)
    Page<Charity> findAll(Pageable pageable);
    @Query(value = "Select * from CHARITY Where (STATUS = 1)  and ( Name like %:keyword% or Content like  %:keyword% or DATE_START like %:keyword% or MONEY like %:keyword% ) Order by DATE_START",
            countQuery = "Select count(*) from CHARITY Where (STATUS = 1) and ( Name like %:keyword% or Content like  %:keyword% or DATE_START like %:keyword% or MONEY like %:keyword% )", nativeQuery = true)
    Page<Charity> searchAll(@Param("keyword") String keyword , Pageable pageable);


    @Query(value = "SELECT  c.ID , NAME, cast(c.DATE_START  as char) as DATE ,  cast(ISNULL(sum(e.Money), '0') as bigint)as PROCESS, c.MONEY ,CAST(CASE WHEN c.STATUS = 1 THEN 'Acctive' ELSE 'Non Acctive' END AS nvarchar) as STATUS  FROM CHARITY c left Join EACHDONATION e on c.ID = e.CHARITY_ID where c.ID = :id group by c.ID, NAME, DATE_START, c.MONEY , c.STATUS ", nativeQuery = true)
    CharityMN findbyID(@Param("id") int id);
    @Query(value = "SELECT  c.ID , NAME, cast(c.DATE_START  as char) as DATE ,  cast(ISNULL(sum(e.Money), '0') as bigint)as PROCESS, c.MONEY ,CAST(CASE WHEN c.STATUS = 1 THEN 'Acctive' ELSE 'Non Acctive' END AS nvarchar) as STATUS  FROM CHARITY c left Join EACHDONATION e on c.ID = e.CHARITY_ID group by c.ID, NAME, DATE_START, c.MONEY , c.STATUS ORDER BY c.DATE_START  DESC",
            countQuery = "SELECT COUNT(distinct c.ID) FROM CHARITY c left Join EACHDONATION e on c.ID = e.CHARITY_ID ", nativeQuery = true)
    Page<CharityMN> findPaginated(Pageable pageable);

    @Query(value = "SELECT  c.ID , NAME, cast(c.DATE_START  as char) as DATE ,  cast(ISNULL(sum(e.Money), '0') as bigint)as PROCESS, c.MONEY ,CAST(CASE WHEN c.STATUS = 1 THEN 'Acctive' ELSE 'Non Acctive' END AS nvarchar) as STATUS  FROM CHARITY c left Join EACHDONATION e on c.ID = e.CHARITY_ID where c.Name like %:keyword% or C.ID like %:keyword% or c.DATE_START like %:keyword% or c.MONEY like %:keyword% group by c.ID, NAME, DATE_START, c.MONEY , c.STATUS ORDER BY c.DATE_START  DESC",
            countQuery = "select count(*) from (SELECT  distinct c.ID FROM CHARITY c left Join EACHDONATION e on c.ID = e.CHARITY_ID where c.Name like %:keyword% or C.ID like  %:keyword% or c.DATE_START like %:keyword% or c.MONEY like %:keyword%  group by c.ID, NAME, DATE_START, c.MONEY , c.STATUS) as C ", nativeQuery = true)
    Page<CharityMN> pageSearch(@Param("keyword") String keyword,Pageable pageable );

    @Query(value = "SELECT e. MONEY, cast(e.PAY_METHOD as char) as PAY_METHOD,  c.NAME ,  CAST(CASE WHEN e.STATUS = 1 THEN 'Acctive' ELSE 'Non Acctive' END AS nvarchar) as STATUS , cast(e.DATE  as char) as DATE   FROM CHARITY c Join EACHDONATION e on c.ID = e.CHARITY_ID Where e.ACCOUNT_ID = :id order by e.DATE DESC ",
            countQuery = "SELECT COUNT( e. MONEY)   FROM CHARITY c Join EACHDONATION e on c.ID = e.CHARITY_ID where e.ACCOUNT_ID = :id  ", nativeQuery = true)
    Page<UserDonation> pageUser(@Param("id") int id,  Pageable pageable );


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CHARITY where ID in (?1) ", nativeQuery = true)
    void delMultiple(@Param("ID") List<Integer> configID);
}

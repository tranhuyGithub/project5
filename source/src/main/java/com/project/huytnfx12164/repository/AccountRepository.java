package com.project.huytnfx12164.repository;

import com.project.huytnfx12164.model.Account;
import com.project.huytnfx12164.model.AccountDashboard;
import com.project.huytnfx12164.model.AccountRanking;
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
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query(value = "select * from ACCOUNT where USERNAME LIKE %:keyword% or EMAIL like %:keyword% or DATE like %:keyword% or ID like %:keyword% ",
            countQuery = "select Count(*) from ACCOUNT where USERNAME LIKE %:keyword% or EMAIL like %:keyword% or DATE like %:keyword% or ID like %:keyword% ", nativeQuery = true)
    Page<Account> pageSearch(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select distinct * from ACCOUNT where EMAIL = :email", nativeQuery = true)
    Account getAccountByEmail(@Param("email") String email);

    @Query(value = "select distinct * from ACCOUNT where TOKEN = :token", nativeQuery = true)
    Account getAccountByToken(@Param("token") String token);
    @Query(value = "select  COUNT( distinct ID) from ACCOUNT ", nativeQuery = true)
    int total();


    @Query(value = "SELECT top 5 a.ID , a.USERNAME, cast(ISNULL(sum(e.Money), 0)as bigint) as MONEY  , a.EMAIL FROM Account A join EACHDONATION e on a.ID = e.ACCOUNT_ID  group by a.ID,a.USERNAME,a.EMAIL ORDER BY sum(e.Money)  DESC;", nativeQuery = true)
    List<AccountDashboard> topList();
    @Query(value = "SELECT  a.ID , a.USERNAME, a.EMAIL, a.PHONE, cast(ISNULL(sum(e.Money), 0)as bigint) as MONEY ,CAST(CASE WHEN a.ROLE = 1 THEN 'User' ELSE 'Admin' END AS nvarchar) as ROLE , CAST(CASE WHEN a.STATUS = 1 THEN 'Active' ELSE 'Non Active' END AS nvarchar) as STATUS, CAST(a.DATE as char )  AS DATE FROM Account a left join EACHDONATION e on a.ID = e.ACCOUNT_ID where a.ID = :id  group by a.ID,a.USERNAME,a.EMAIL, a.ROLE, a.STATUS, a.DATE, a.PHONE", nativeQuery = true)
    AccountDashboard findbyId(@Param("id") int id );
    @Query(value = "SELECT  a.ID , a.USERNAME, a.EMAIL, a.PHONE, cast(ISNULL(sum(e.Money), 0)as bigint) as MONEY ,CAST(CASE WHEN a.ROLE = 1 THEN 'User' ELSE 'Admin' END AS nvarchar) as ROLE , CAST(CASE WHEN a.STATUS = 1 THEN 'Active' ELSE 'Non Active' END AS nvarchar) as STATUS, CAST(a.DATE as char )  AS DATE FROM Account a left join EACHDONATION e on a.ID = e.ACCOUNT_ID  group by a.ID,a.USERNAME,a.EMAIL, a.ROLE, a.STATUS, a.DATE, a.PHONE ORDER BY a.USERNAME ASC;"
            ,countQuery = "select Count(*) from (SELECT  distinct a.ID  FROM Account A left join EACHDONATION e on a.ID = e.ACCOUNT_ID ) as ACC", nativeQuery = true)
    Page<AccountDashboard> findPaginated(Pageable pageable);

    @Query(value = "SELECT  a.ID , a.USERNAME, a.EMAIL, a.PHONE, cast(ISNULL(sum(e.Money), 0)as bigint) as MONEY ,CAST(CASE WHEN a.ROLE = 1 THEN 'User' ELSE 'Admin' END AS nvarchar) as ROLE , CAST(CASE WHEN a.STATUS = 1 THEN 'Active' ELSE 'Non Active' END AS nvarchar) as STATUS, CAST(a.DATE as char )  AS DATE FROM Account a left join EACHDONATION e on a.ID = e.ACCOUNT_ID where a.ID like %:keyword% or a.USERNAME like %:keyword% or EMAIL like %:keyword% or a.PHONE like %:keyword% or a.DATE like %:keyword%   group by a.ID,a.USERNAME,a.EMAIL, a.ROLE, a.STATUS, a.DATE, a.PHONE ORDER BY a.USERNAME ASC;",
           countQuery = "Select count(*) from (SELECT distinct a.ID FROM Account A left join EACHDONATION e on a.ID = e.ACCOUNT_ID where a.ID like %:keyword% or a.USERNAME like %:keyword% or EMAIL like %:keyword% or a.PHONE like %:keyword% or a.DATE like %:keyword%   group by a.ID,a.USERNAME,a.EMAIL, a.ROLE, a.STATUS, a.DATE, a.PHONE ) ACC" , nativeQuery = true)
    Page<AccountDashboard> searchPaginated(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT  a.ID , a.USERNAME, a.EMAIL, cast (CONCAT( '*******', SUBSTRING(CAST(a.PHONE as char), LEN(a.Phone) -2, Len(a.phone))) as char) , cast(ISNULL(sum(e.Money), 0)as bigint) as MONEY ,CAST(CASE WHEN a.ROLE = 1 THEN 'User' ELSE 'Admin' END AS nvarchar) as ROLE , CAST(CASE WHEN a.STATUS = 1 THEN 'Acctive' ELSE 'Non Acctive' END AS nvarchar) as STATUS, CAST(a.DATE as char )  AS DATE FROM Account a join EACHDONATION e on a.ID = e.ACCOUNT_ID where e.CHARITY_ID = :id  group by a.ID,a.USERNAME,a.EMAIL, a.ROLE, a.STATUS, a.DATE, a.PHONE ORDER BY MONEY Desc",
           countQuery = "Select count(*) from (SELECT distinct a.ID FROM Account A left join EACHDONATION e on a.ID = e.ACCOUNT_ID where e.CHARITY_ID = :id   group by a.ID,a.USERNAME,a.EMAIL, a.ROLE, a.STATUS, a.DATE, a.PHONE ) ACC" , nativeQuery = true)
    Page<AccountRanking> rankingDonate(@Param("id") int id, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ACCOUNT where ID in (?1) ", nativeQuery = true)
    void delMultiple(@Param("ID") List<Integer> configID);

}

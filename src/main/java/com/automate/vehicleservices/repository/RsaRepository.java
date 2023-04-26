package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.RSA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RsaRepository extends CrudRepository<RSA, Integer> {
//public interface RsaRepository extends JpaRepository<RSA, Integer> {


    Page<RSA> findAll(Pageable paging);

    Page<RSA> findAllByStatus(String status, Pageable paging);

    Page<RSA> findAllByStatusAndCustomerId(String status, Integer customerId, Pageable paging);

    Page<RSA> findAllByCustomerId(Integer customerid, Pageable paging);

    List<RSA> findAllByCustomerId(int customerid);

    @Query(value = "select * from rsa r, rsa_address ra where (lower(r.ID) like lower(concat('%', :searchParam,'%'))) or (lower(r.CUSTOMER_ID) like lower(concat('%', :searchParam,'%'))) or " +
            "(lower(r.AMOUNT) like lower(concat('%', :searchParam,'%'))) or (lower(r.DATE) like lower(concat('%', :searchParam,'%'))) or (lower(r.REASON) like lower(concat('%', :searchParam,'%'))) or " +
            "(lower(r.REMARKS) like lower(concat('%', :searchParam,'%'))) or (lower(r.CREATED_BY) like lower(concat('%', :searchParam,'%'))) or (lower(r.CREATED_DATE) like lower(concat('%', :searchParam,'%'))) or " +
            "(lower(r.LAST_MODIFIED_DATE) like lower(concat('%', :searchParam,'%'))) or (lower(r.LAST_MODIFIED_BY) like lower(concat('%', :searchParam,'%'))) or " +
            "(lower(r.STATUS) like lower(concat('%', :searchParam,'%'))) or (lower(ra.address) like lower(concat('%', :searchParam,'%'))) or" +
            " (lower(ra.area) like lower(concat('%', :searchParam,'%'))) or (lower(ra.landmark) like lower(concat('%', :searchParam,'%'))) or " +
            "(lower(ra.pin) like lower(concat('%', :searchParam,'%')))", nativeQuery = true)
    Page<RSA> getRSAList(@Param("searchParam") String searchParam, Pageable pageable);
}

package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriversRepository extends JpaRepository<Driver, Integer>, JpaSpecificationExecutor<Driver> {

    List<Driver> findAllByDriverType(String s);

    List<Driver> findAllByOrgIdAndBranchId(int orgId, int branchId);

    List<Driver> findByIdIn(List<Integer> collect);

    @Query(value = "SELECT * FROM drivers WHERE id not in (:collect) AND driver_type=:type", nativeQuery = true)
    List<Driver> findByIdNotInAndDriverType(@Param("collect") List<Integer> collect, @Param("type") String type);

    @Query(value = "SELECT * FROM drivers ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Driver getLastSavedId();
}
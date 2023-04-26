package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.LeadAllocationDetails;
import com.automate.vehicleservices.repository.dtoprojection.LeadAllocationCREProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadAllocationDetailRepository extends JpaRepository<LeadAllocationDetails, Integer> {


    @Query(nativeQuery = true, value = "SELECT CRE_ID as creId , count(VEHICLE_ID) as leadCount,ed.name as creName,lad.CRE_SERVICE_CENTER_CODE as dealerCode  from lead_allocation_details lad inner join employee_details ed on ed.id = lad.CRE_ID where STATUS = 'ACTIVE' and CRE_ID in :employeeId and ( :location is null or CRE_LOCATION like :location ) and ( :dealerCode is null or CRE_SERVICE_CENTER_CODE like :dealerCode ) group by CRE_ID")
    List<LeadAllocationCREProjection> getCreLeadDetails(@Param("employeeId") List<Integer> inHouseEmployeeIds, @Param("dealerCode") String dealerCode, @Param("location") String location);

}

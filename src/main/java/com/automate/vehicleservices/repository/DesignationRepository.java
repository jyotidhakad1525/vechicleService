package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.DesignationDetail;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<DesignationDetail, Integer> {
    Optional<DesignationDetail> findByIdAndOrganizationId(Integer id, int orgId);

    Page<DesignationDetail> findAllByOrganizationId(int orgId, Pageable pageable);

    @Query(value = "SELECT * from designation_detail dd where ( :designationName is null or dd.designation_name like %:designationName% ) and ( :designationValue is null or dd.designation_value = :designationValue ) and ( :#{#status?.name()} is null or dd.status = :#{#status?.name()}  ) and ( :departmentId is null or dd.department_id = :departmentId ) and dd.org_id = :orgId"
            , countQuery = "SELECT count(*) from designation_detail dd where ( :designationName is null or dd.designation_name like %:designationName% ) and ( :designationValue is null or dd.designation_value = :designationValue ) and ( :#{#status?.name()} is null or dd.status = :#{#status?.name()}  ) and ( :departmentId is null or dd.department_id = :departmentId ) and dd.org_id = :orgId"
            , nativeQuery = true)
    Page<DesignationDetail> search(@Param("designationName") String designationName, @Param("designationValue") Integer designationValue, @Param("status") ActiveInActiveStatus status, @Param("departmentId") Integer departmentId, @Param("orgId") int orgId, Pageable pageable);

    List<DesignationDetail> findByDepartmentDetailIdAndOrganizationId(Integer departmentId, int orgId);

    Optional<DesignationDetail> findByIdAndOrganizationIdAndDepartmentDetailId(Integer id, int orgId, Integer departmentId);
}

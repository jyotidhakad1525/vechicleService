package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.DepartmentDetail;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentDetail, Integer> {
    Optional<DepartmentDetail> findByIdAndOrganizationId(Integer id, int orgId);

    Page<DepartmentDetail> findAllByOrganizationId(int orgId, Pageable pageable);

    @Query(value = "SELECT * from department_detail dd where ( :departmentName is null or dd.department_name like %:departmentName% ) and ( :departmentValue is null or dd.department_value = :departmentValue ) and ( :#{#status?.name()} is null or dd.status = :#{#status?.name()} ) and dd.org_id = :orgId"
            , countQuery = "SELECT count(*) from department_detail dd where ( :departmentName is null or dd.department_name like %:departmentName% ) and ( :departmentValue is null or dd.department_value = :departmentValue ) and ( :#{#status?.name()} is null or dd.status = :#{#status?.name()} ) and dd.org_id = :orgId"
            , nativeQuery = true)
    Page<DepartmentDetail> search(@Param("departmentName") String departmentName, @Param("departmentValue") Integer departmentValue, @Param("status") ActiveInActiveStatus status, @Param("orgId") int orgId, Pageable pageable);
}

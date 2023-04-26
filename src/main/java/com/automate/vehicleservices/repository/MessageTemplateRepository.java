package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.PreDefineMessageTemplate;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageTemplateRepository extends JpaRepository<PreDefineMessageTemplate, Integer> {
    Optional<PreDefineMessageTemplate> findByIdAndOrganizationId(Integer id, int orgId);

    Page<PreDefineMessageTemplate> findAllByOrganizationIdAndTemplateType(int orgId, int type, Pageable pageable);

    @Query(value = "SELECT * FROM pre_define_template pdt where (:templateName is null or pdt.template_name like %:templateName%) and (:template is null or pdt.template  like %:template%) and (:#{#status?.name()} is null or pdt.status = :#{#status?.name()}) and pdt.template_type =:type and pdt.org_id =:orgId"
            , countQuery = "SELECT count(*) FROM pre_define_template pdt where (:templateName is null or pdt.template_name like %:templateName%) and (:template is null or pdt.template  like %:template%) and (:#{#status?.name()} is null or pdt.status = :#{#status?.name()}) and pdt.template_type =:type and pdt.org_id =:orgId"
            , nativeQuery = true)
    Page<PreDefineMessageTemplate> search(@Param("type") int type, @Param("templateName") String templateName, @Param("template") String template, @Param("status") ActiveInActiveStatus status, @Param("orgId") int orgId, Pageable pageable);
}

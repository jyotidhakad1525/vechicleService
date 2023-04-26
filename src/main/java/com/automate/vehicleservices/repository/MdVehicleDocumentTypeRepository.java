package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.MdVehicleDocumentType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
public interface MdVehicleDocumentTypeRepository extends CrudRepository<MdVehicleDocumentType, Integer> {

    List<MdVehicleDocumentType> findByMdTenant(MdTenant mdTenant);

    MdVehicleDocumentType findByMdTenantAndDocumentNameAndLabel(final MdTenant mdTenant, final String documentName,
                                                                final String label);

    MdVehicleDocumentType findByDocumentNameAndLabelAndMdTenant_TenantIdentifier(final String documentName,
                                                                                 final String label,
                                                                                 final String tenantIdentifier);

    List<MdVehicleDocumentType> findAllByMdTenant_Id(int tenantId);

    MdVehicleDocumentType findByIdAndMdTenant_Id(int id, int tenantId);

    boolean existsByIdAndMdTenant_Id(int id, int tenantId);
}

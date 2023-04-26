package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdOrganization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandrashekar V
 */
@Repository
public interface MdOrganizationRepository extends CrudRepository<MdOrganization, Integer> {

    MdOrganization findByOrgIdentifier(final String orgIdentifier);

    MdOrganization findOrgIdentifierByMasterIdentifier(int masterOrgIdentifier);
}

package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Chandrashekar V
 */
@Repository
@Transactional
public interface ServiceDocumentRepository extends CrudRepository<ServiceDocument, Integer> {
}

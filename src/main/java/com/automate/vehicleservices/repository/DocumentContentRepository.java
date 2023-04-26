package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.DocumentContent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Chandrashekar V
 */
@Repository
@Transactional
public interface DocumentContentRepository extends CrudRepository<DocumentContent, Integer> {

    DocumentContent findByUuid(final String uuid);
}

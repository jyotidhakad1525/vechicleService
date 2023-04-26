package com.automate.vehicleservices.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GenericService<E, I> {
    E save(E entity);

    List<E> save(List<E> entities);

    void deleteById(I id);

    Optional<E> findById(I id);

    List<E> findAll();

    Page<E> findAll(Pageable pageable);

    E update(E entity, I id);
}
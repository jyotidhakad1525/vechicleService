/**
 *
 */
package com.automate.vehicleservices.framework.service;

import java.util.Optional;

/**
 * @author chandrashekharv
 *
 */
public interface CrudService {

    <T, S extends T> S save(S var1);

    <T, S extends T> Iterable<S> saveAll(Iterable<S> var1);

    <T, I> Optional<T> findById(I var1, Class<T> aClass);

    <T, I> boolean existsById(I i, Class<T> aClass);

    <T> Iterable<T> findAll(Class<T> aClass);

    <T, I> Iterable<T> findAllById(Iterable<I> var1, Class<T> aClass);

    <T> long count(Class<T> aClass);

    <T, I> void deleteById(I var1, Class<T> aClass);

    <T> void delete(T var1);

    <T> void deleteAll(Iterable<? extends T> var1);

    <T> void deleteAll(Class<T> aClass);
}



package com.automate.vehicleservices.framework.service;

import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author Chandrashekar V
 */
@Component
public class GenericCRUDService implements CrudService {

    public static final String REPOSITORY_SUFFIX = "Repository";

    private final ApplicationContext applicationContext;

    private Repositories repositories;

    public GenericCRUDService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void init() {
        repositories = new Repositories(applicationContext);
    }

    private CrudRepository of(Class aClass) {
        Optional<Object> crudRepositoryOptional = repositories.getRepositoryFor(aClass);
        if (!crudRepositoryOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.INTERNAL_SERVER_ERROR, "Implementation not found");

        return (CrudRepository) crudRepositoryOptional.get();
    }


    @Override
    public <T, S extends T> S save(S s) {
        CrudRepository<S, Object> crudRepository = of(s.getClass());
        return crudRepository.save(s);
    }

    @Override
    public <T, S extends T> Iterable<S> saveAll(Iterable<S> sIterable) {
        S next = sIterable.iterator().next();
        CrudRepository<S, Object> crudRepository = of(next.getClass());
        return crudRepository.saveAll(sIterable);

    }

    @Override
    public <T, I> Optional<T> findById(I i, Class<T> aClass) {
        CrudRepository<T, I> crudRepository = of(aClass);
        return crudRepository.findById(i);
    }

    @Override
    public <T, I> boolean existsById(I i, Class<T> aClass) {
        CrudRepository<T, I> crudRepository = of(aClass);
        return crudRepository.existsById(i);
    }

    @Override
    public <T> Iterable<T> findAll(Class<T> aClass) {
        CrudRepository<T, Object> crudRepository = of(aClass);
        return crudRepository.findAll();
    }

    @Override
    public <T, I> Iterable<T> findAllById(Iterable<I> var1, Class<T> aClass) {
        CrudRepository<T, I> crudRepository = of(aClass);
        return crudRepository.findAllById(var1);
    }

    @Override
    public <T> long count(Class<T> aClass) {
        CrudRepository<T, Object> crudRepository = of(aClass);
        return crudRepository.count();
    }

    @Override
    public <T, I> void deleteById(I var1, Class<T> aClass) {
        CrudRepository<T, I> crudRepository = of(aClass);
        crudRepository.deleteById(var1);
    }

    @Override
    public <T> void delete(T var1) {
        CrudRepository<T, Object> crudRepository = of(var1.getClass());
        crudRepository.delete(var1);
    }

    @Override
    public <T> void deleteAll(Iterable<? extends T> var1) {
        T next = var1.iterator().next();
        CrudRepository<T, Object> crudRepository = of(next.getClass());
        crudRepository.deleteAll(var1);
    }

    @Override
    public <T> void deleteAll(Class<T> aClass) {
        CrudRepository<T, Object> crudRepository = of(aClass);
        crudRepository.deleteAll();
    }
}

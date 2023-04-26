/**
 *
 */
package com.automate.vehicleservices.framework.common;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * @author chandrashekharv
 *
 */
@Slf4j
public class DefaultSupplier {

    public static <T> Supplier<T> nullSupplier(Class<?> clazz) {
        return () -> null;
    }

    public static <T> Supplier<?> defaultConstructorInstanceSupplier(Class<T> clazz) {
        return () -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                log.error(String.format("Error while instantiating the class %s ", clazz), e);
            }
            return null;
        };
    }
}

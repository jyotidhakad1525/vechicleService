package com.automate.vehicleservices.framework;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Singleton class to provide application context and relevant utility methods for non-spring components where DI is not
 * possible.
 */
@Component
public final class CustomBeanFactory implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static <T> T bean(Class<T> aClass) {
        return applicationContext.getBean(aClass);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CustomBeanFactory.applicationContext = applicationContext;
    }
}

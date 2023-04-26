package com.automate.vehicleservices.service.aop;

import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Aspect
@Slf4j
public class TenantInterceptor {

    @Pointcut("execution (public * com.automate.vehicleservices.service.MdTenantService.tenantByIdentifier(String))")
    public void tenantIdentifier() {
    }

    @Around("tenantIdentifier()")
    public Object throwExceptionIfTenantDoesntExists(ProceedingJoinPoint proceedingJoinPoint) {
        Object[] args = proceedingJoinPoint.getArgs();
        if (Objects.isNull(args) || args.length == 0)
            throw new VehicleServicesException("Invalid identifier to fetch tenant");
        final String tenantIdentifier = (String) args[0];
        try {
            Object object = proceedingJoinPoint.proceed();
            if (Objects.isNull(object))
                throw new VehicleServicesException(String.format("Tenant not found for given identifier: %s",
                        tenantIdentifier));

            return object;
        } catch (VehicleServicesException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new VehicleServicesException("Tenant not found");
        }
    }
}

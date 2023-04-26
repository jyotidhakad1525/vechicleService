package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.event.NewVehicleEvent;
import com.automate.vehicleservices.event.UpdateVehicleEvent;
import com.automate.vehicleservices.event.VehicleEventData;
import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class VehicleInterceptor {

    private final EventPublisher eventPublisher;

    public VehicleInterceptor(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Pointcut("execution(* com.automate.vehicleservices.repository.ServiceVehicleRepository.save(..))")
    private void vehiclePointcut() {
    }

    @Around(("vehiclePointcut()"))
    public Object interceptForHandlingVehicleSchedule(ProceedingJoinPoint joinPoint) {

        boolean newVehicleEvent = false;
        boolean updateVehicleEvent = false;

        final Object[] args = joinPoint.getArgs();
        if (Objects.nonNull(args)) {
            ServiceVehicle serviceVehicle = (ServiceVehicle) args[0];
            if (serviceVehicle.getId() == 0) {
                newVehicleEvent = true;
            } else updateVehicleEvent = true;
        }
        try {
            final Object proceed = joinPoint.proceed(args);

            if (Objects.nonNull(proceed) && proceed instanceof ServiceVehicle) {
                ServiceVehicle serviceVehicle = (ServiceVehicle) proceed;
                if (serviceVehicle.getId() != 0 && newVehicleEvent) {
                    log.info(String.format("Vehicle %s is created. Publishing New Vehicle event.",
                            serviceVehicle.getRegNumber()));
                    eventPublisher.publish(NewVehicleEvent.builder()
                            .eventData(new VehicleEventData(serviceVehicle)).build());
                } else if (updateVehicleEvent) {
                    log.info(String.format("Vehicle %s is updated. Publishing vehicle update event.",
                            serviceVehicle.getRegNumber()));
                    eventPublisher.publish(UpdateVehicleEvent.builder()
                            .eventData(new VehicleEventData(serviceVehicle)).build());
                }
                return proceed;
            }
        } catch (Throwable throwable) {
            log.error("Exception while saving vehicle.", throwable);
        }
        throw new VehicleServicesException("Exception while saving vehicle");
    }

}

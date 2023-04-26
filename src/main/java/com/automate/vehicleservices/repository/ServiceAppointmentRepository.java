package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceAppointment;
import com.automate.vehicleservices.entity.enums.ServiceAppointmentStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public interface ServiceAppointmentRepository extends CrudRepository<ServiceAppointment, Integer>,
        JpaSpecificationExecutor<ServiceAppointment> {


    List<ServiceAppointment> findByServiceDateAndServiceVehicleRegNumberAndStatusIn(final LocalDate serviceDate,
                                                                                    final String regNumber,
                                                                                    ServiceAppointmentStatus... status);

    List<ServiceAppointment> findByServiceDateEqualsOrServiceDateAfterAndMdTenant_TenantIdentifier(final LocalDate on
            , final LocalDate after, String tenant);

    List<ServiceAppointment> findByServiceDateEqualsOrServiceDateAfterAndMdTenant_TenantIdentifierAndServiceVehicle_RegNumber(
            final LocalDate on, final LocalDate after, String tenant, final String vehicleRegNumber);

    List<ServiceAppointment> findByMdTenant_TenantIdentifierAndServiceVehicle_RegNumber(String tenant,
                                                                                        final String vehicleRegNumber);

    List<ServiceAppointment> findByMdTenant_MdOrganization_OrgIdentifierAndCustomer_ContactNumberContaining(
            String orgIdentifier,
            final String customerContactNumber);

    List<ServiceAppointment> findByMdTenant_MdOrganization_OrgIdentifierAndCustomer_ContactNumberLike(String org,
                                                                                                      String s);

    boolean existsByIdAndMdTenant_TenantIdentifier(int id, String tenant);

    Optional<ServiceAppointment> findByIdAndMdTenant_TenantIdentifier(int id, String tenant);


    @Query("update ServiceAppointment s set s.status=:status where s.id=:id")
    @Modifying
    void updateAppointmentStatus(@Param("status") final ServiceAppointmentStatus status,
                                 @Param("id") final int id);

    @Query("select max(BOOKING_ID) from ServiceAppointment")
    Integer getLastBookingId();
}

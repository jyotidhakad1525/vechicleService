package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.RSAAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RsaAddressRepository extends JpaRepository<RSAAddress, Integer> {

}

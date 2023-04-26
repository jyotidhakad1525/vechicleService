package com.automate.vehicleservices.service.rsa;

import com.automate.vehicleservices.api.model.rsa.RSAAddressRequest;
import com.automate.vehicleservices.entity.RSAAddress;
import com.automate.vehicleservices.entity.builder.RsaAddressBuilder;
import com.automate.vehicleservices.repository.RsaAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RSAAddressService {

    private final RsaAddressRepository rsaAddressRepository;

    public RSAAddressService(RsaAddressRepository rsaAddressRepository) {
        this.rsaAddressRepository = rsaAddressRepository;
    }

    public RSAAddress createRSAAddressEntity(RSAAddressRequest rsaAddressRequest) {
        return RsaAddressBuilder.aRsaAddress()
                .withAddress(rsaAddressRequest.getAddress())
                .withArea(rsaAddressRequest.getArea())
                .withLandmark(rsaAddressRequest.getLandmark())
                .withLatitude(rsaAddressRequest.getLatitude())
                .withLongitude(rsaAddressRequest.getLongitude())
                .withPin(rsaAddressRequest.getPin())
                .build();

    }
}

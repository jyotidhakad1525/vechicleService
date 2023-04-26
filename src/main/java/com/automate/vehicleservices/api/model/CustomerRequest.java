package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.Gender;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Chandrashekar V
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class CustomerRequest {

    private String firstName;

    private String lastName;

    private int leadSource;

    private int parentLeadSource;

    @NotBlank
    private String contactNumber;

    private String alternateContactNumber;

    @Email
    private String email;

    private List<AddressRequest> addresses;

    private Gender gender;

    private String customerType;

    private String occupation;

    private LocalDate dateOfBirth;

    private LocalDate dateOfArrival;

    private String refered_by;
    
    public String fullName() {
        StringBuilder fullNameBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(firstName))
            fullNameBuilder.append(firstName).append(StringUtils.SPACE);
        if (StringUtils.isNoneBlank(lastName))
            fullNameBuilder.append(lastName);

        return fullNameBuilder.toString();


    }
}

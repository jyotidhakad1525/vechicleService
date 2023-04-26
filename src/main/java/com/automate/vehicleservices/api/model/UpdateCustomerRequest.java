package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.Gender;
import com.automate.vehicleservices.entity.enums.Salutation;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Map;

/**
 * @author Chandrashekar V
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class UpdateCustomerRequest {

    private String firstName;

    private String lastName;

    private int leadSource;

    @NotBlank
    private String contactNumber;

    private String alternateContactNumber;

    @Email
    private String email;

    private Map<Integer, AddressRequest> addresses;

    private Gender gender;

    private String customerType;

    private String occupation;
    
    private String refered_by;

    private LocalDate dateOfBirth;

    private LocalDate dateOfArrival;

    private Salutation salutation;

    private String relationName;

    private Integer age;

    public String fullName() {
        StringBuilder fullNameBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(firstName))
            fullNameBuilder.append(firstName).append(StringUtils.SPACE);
        if (StringUtils.isNoneBlank(lastName))
            fullNameBuilder.append(lastName);

        return fullNameBuilder.toString();


    }
}

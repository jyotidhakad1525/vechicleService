package com.automate.vehicleservices.api.model.v1;

import com.automate.vehicleservices.entity.enums.Gender;
import com.automate.vehicleservices.entity.enums.Salutation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
public class CustomerRequestV1 {

    private Integer id;

    private Salutation salutation;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private Gender gender;

    private String relationName;

    private LocalDate dateOfBirth;

    private Integer age;

    private LocalDate dateOfArrival;

    @NotBlank
    private String contactNumber;

    private String alternateContactNumber;

    @Email
    private String email;

    private String occupation;

    private int leadSource;

    private int parentLeadSource;

    private String customerType;

    private List<AddressRequestV1> addresses;
    
    public String fullName() {
        StringBuilder fullNameBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(firstName))
            fullNameBuilder.append(firstName).append(StringUtils.SPACE);
        if (StringUtils.isNoneBlank(lastName))
            fullNameBuilder.append(lastName);

        return fullNameBuilder.toString();

    }
}

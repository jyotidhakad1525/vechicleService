package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.MdTenant;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Chandrashekar V
 */
@JsonRootName(value = "tenant")
@Builder
@Getter
@NoArgsConstructor
public class TenantDTO {
    private String name;
    private String identifier;
    private String organization;
    private int id;

    private TenantDTO(String name, String identifier, String organization, final int id) {
        this.name = name;
        this.identifier = identifier;
        this.organization = organization;
        this.id = id;
    }

    public TenantDTO(MdTenant mdTenant) {
        this.name = mdTenant.getTenantName();
        this.identifier = mdTenant.getTenantIdentifier();
        this.organization = mdTenant.getMdOrganization().getOrgName();
        this.id = mdTenant.getId();
    }

    public static final class TenantDTOBuilder {
        private String name;
        private String identifier;
        private String organization;
        private int id;

        public TenantDTOBuilder() {
        }

        public static TenantDTOBuilder aTenantDTO() {
            return new TenantDTOBuilder();
        }

        public TenantDTOBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TenantDTOBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public TenantDTOBuilder withIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public TenantDTOBuilder withOrganization(String organization) {
            this.organization = organization;
            return this;
        }

        public TenantDTO build() {
            return new TenantDTO(name, identifier, organization, id);
        }
    }
}

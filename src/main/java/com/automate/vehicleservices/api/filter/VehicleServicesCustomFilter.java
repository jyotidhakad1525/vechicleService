package com.automate.vehicleservices.api.filter;

import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.repository.MdOrganizationRepository;
import com.automate.vehicleservices.service.MdTenantService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@NoArgsConstructor
@Slf4j
@Component
public class VehicleServicesCustomFilter extends GenericFilterBean {

    public static final String API_TENANT = "/api/tenant/";
    public static final String API_ORG = "/api/org/";
    public static final String EMP_ID = "empId";
    private MdTenantService tenantService;

    private MdOrganizationRepository organizationRepository;

    private RequestContext requestContext;

    @Autowired
    public VehicleServicesCustomFilter(MdTenantService tenantService,
                                       MdOrganizationRepository organizationRepository, RequestContext requestContext) {
        this.tenantService = tenantService;
        this.organizationRepository = organizationRepository;
        this.requestContext = requestContext;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final var requestURI = httpServletRequest.getRequestURI();
        log.info("request URI:" + requestURI);

        final String org = orgFromRequest(requestURI);
        if (StringUtils.isNoneBlank(org))
            requestContext.setOrg(org);

        final MdTenant tenant = tenantFromRequest(requestURI);
        if (Objects.isNull(tenant) && StringUtils.isBlank(org)) {
            ((HttpServletResponse) servletResponse)
                    .sendError(HttpServletResponse.SC_BAD_REQUEST, "No tenant/org found.");
            return;
        }
        if (Objects.nonNull(tenant)) {
            requestContext.setTenant(tenant.getTenantIdentifier());
            requestContext.setTenantId(tenant.getId());
        }

        userIdFromHeader((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void userIdFromHeader(HttpServletRequest servletRequest) {
        final var loggedInEmployeeId = servletRequest.getHeader(EMP_ID);
        if (StringUtils.isNotBlank(loggedInEmployeeId)) {
            try {
                requestContext.setEmpId(Integer.parseInt(loggedInEmployeeId));
            } catch (NumberFormatException ex) {
                log.error(String.format("Unable to capture emp id from request header : %s", loggedInEmployeeId), ex);
            }
        }
    }

    private MdTenant tenantFromRequest(String requestURI) {
        int indexOfTenant = requestURI.indexOf(API_TENANT);
        if (indexOfTenant == -1)
            return null;

        final var substring = requestURI.substring(indexOfTenant + API_TENANT.length());
        final var tenant = substring.substring(0, substring.indexOf("/"));

        if (!NumberUtils.isCreatable(tenant)) {
            return null;
        }
        log.info(String.format("master tenant identifier %s", tenant));
        int intMasterIdentifier = Integer.parseInt(tenant);
        return tenantService.findByMasterIdentifier(intMasterIdentifier);

    }

    private String orgFromRequest(String requestURI) {
        int indexOfOrg = requestURI.indexOf(API_ORG);
        if (indexOfOrg == -1)
            return StringUtils.EMPTY;

        final var substring = requestURI.substring(indexOfOrg + API_ORG.length());
        final var org = substring.substring(0, substring.indexOf("/"));
        if (NumberUtils.isCreatable(org)) {
            log.info(String.format("master org identifier %s", org));
            int intMasterIdentifier = Integer.parseInt(org);
            final var mdOrganization =
                    organizationRepository.findOrgIdentifierByMasterIdentifier(intMasterIdentifier);
            if (mdOrganization != null)
                return mdOrganization.getOrgIdentifier();
        }
        return org;
    }


}

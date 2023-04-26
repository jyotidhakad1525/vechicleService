/**
 *
 */
package com.automate.vehicleservices.framework.api;

import com.automate.vehicleservices.api.filter.RequestContext;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.util.BasedComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author chandrashekharv
 *
 *         T Represents the request object for Create and update operations. I
 *         represents the data type of the identifier that is used for fetch,
 *         delete operations. R represents Response Object for CRUD Operations.
 *
 */
public class AbstractBaseController {

    public static final String TENANT = "tenant";

    public static final String BASE_URL = "/api/tenant/{tenant}";
    @Autowired
    protected CrudService crudService;
    @Autowired
    private RequestContext requestContext;

    @Autowired
    private BasedComponent basedComponent;

    protected String getTenant() {
        return requestContext.getTenant();
    }

    protected int getTenantId() {
        return requestContext.getTenantId();
    }

    protected String getOrg() {
        return requestContext.getOrg();
    }

    protected String getCurrentUser() {
        return requestContext.getUser();
    }

    protected int getLoggedInEmpId() {
        return requestContext.getEmpId();
    }

    protected MdOrganization getOrgBasedOnTenantId() {
        return basedComponent.getOrgIdBasedOnIdentifierId(requestContext.getTenant());
    }

    /**
     * This method returns APIResponse by wrapping 't' as body. if t is null it
     * assumes the processing is failed and returns failure response.
     *
     * @param t
     * @return
     */
    public <E> APIResponse<?> defaultResponse(E t) {
        return defaultResponse(t, null);
    }

    /**
     * This method returns APIResponse by wrapping 't' as body. if t is null it
     * assumes the processing is failed and returns failure response.
     *
     * @return
     */
    public APIResponse<?> emptySuccessResponse() {
        return APIResponse.defaultSuccessResponse();

    }

    /**
     * This method returns empty failure response.
     *
     * @return
     */
    public APIResponse<Void> emptyFailureResponse() {
        return APIResponse.defaultFailureResponse();

    }

    /**
     * Returns APIResponse by wrapping the given object 'e'. This method also
     * accepts predicate on 'e' to be tested. In case the predicate is failed
     * against 'e' then failure response would be returned.
     *
     * @param e
     * @param predicate
     * @return
     */
    public <E> APIResponse<?> defaultResponse(E e, Predicate<E> predicate) {

        if (Objects.isNull(e) || (!Objects.isNull(predicate) && !predicate.test(e)))
            return APIResponse.defaultFailureResponse();

        return APIResponse.APIResponseBuilder.anAPIResponse().withBody(e).withStatus(HttpStatus.OK).withIsSuccess(true)
                .build();
    }

    /**
     * Returns APIResponse with provided status code.
     *
     * @return
     */
    public <E> APIResponse<?> defaultVoidResponse(HttpStatus httpStatus) {

        return APIResponse.APIResponseBuilder.anAPIResponse()
                .withStatus(httpStatus)
                .withIsSuccess(true)
                .build();
    }

    /**
     * Checks any given predicate is failed against the object passed.
     *
     * @param predicates
     * @param e
     * @return boolean returns true if any predicate is failed against the object
     *         passed. If all predicates are successfully tested against 'e' then
     *         return false. Also returns false if there are no predicates passed to
     *         start with.
     */
    public <E> boolean anyPredicateFailed(Predicate<E>[] predicates, E e) {

        if (Objects.isNull(predicates) || predicates.length == 0)
            return false;

        Optional<Predicate<E>> optional = Arrays.stream(predicates).filter(Objects::nonNull).filter(p -> !p.test(e))
                .findAny();
        return optional.isPresent();
    }

    /**
     * Wraps API response into Spring ResponseEntity<E> and returns the same.
     * HTTPStatus depends on the isSuccess flag available in APIResponse instance.
     *
     * @param e
     * @param predicate
     * @return ResponseEntity
     */
    public <E> ResponseEntity<APIResponse<?>> responseEntity(E e, Predicate<E> predicate) {

        APIResponse<?> apiResponse = defaultResponse(e, predicate);
        return responseEntity(apiResponse);
    }

    /**
     * Wraps API response into Spring ResponseEntity<E> and returns the same.
     * HTTPStatus depends on the isSuccess flag available in APIResponse instance.
     *
     * @param e
     * @return ResponseEntity
     */
    public <E> ResponseEntity<APIResponse<?>> responseEntity(E e) {
        return responseEntity(e, null);
    }

    /**
     * Wraps API response into Spring ResponseEntity<E> and returns the same.
     * HTTPStatus depends on the isSuccess flag available in APIResponse instance.
     *
     * @param apiResponse
     * @return ResponseEntity
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <E> ResponseEntity<APIResponse<?>> responseEntity(APIResponse apiResponse) {

        if (apiResponse.isSuccess())
            return new ResponseEntity(apiResponse, HttpStatus.OK);

        return new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public <E> APIResponse<?> failureResponse(E t, List<? extends APIError> errors) {
        return new APIResponse<E>(t, errors);
    }

}

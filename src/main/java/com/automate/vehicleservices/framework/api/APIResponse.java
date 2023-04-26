/**
 *
 */
package com.automate.vehicleservices.framework.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author chandrashekharv
 *
 *         Generic APi response would be used for sending response to any
 *         request.
 *
 *
 */
public class APIResponse<T> {

    public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy hh:mm:ss";

    private HttpStatus status;
    private int statusCode;
    private String message;
    private T body;
    private boolean isSuccess;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DD_MM_YYYY_HH_MM_SS)
    private LocalDateTime time = LocalDateTime.now();
    private List<? extends APIError> errors;

    public APIResponse(T t) {
        this.body = t;
    }

    public APIResponse(HttpStatus httpStatus) {
        this.status = httpStatus;
        this.message = httpStatus.getReasonPhrase();
        this.statusCode = httpStatus.value();
    }

    /**
     * Constructor for default error response.
     *
     * @param t
     * @param errors
     */
    public APIResponse(T t, List<? extends APIError> errors) {
        this.body = t;
        this.errors = errors;

        if (!Objects.isNull(errors)) {
            this.isSuccess = false;
        }
    }

    /**
     * Throws default failure response. Default failure response is to wrap any
     * internal processing error that is not identified during implementation time
     * with custom error and message.
     *
     * @return
     */
    public static APIResponse<Void> defaultFailureResponse() {
        return constructFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Throws default failure response. Default failure response is to wrap any
     * internal processing error that is not identified during implementation time
     * with custom error and message.
     *
     * @return
     */
    public static APIResponse<Void> invalidRequestBodyResponse(final String message) {
        return constructFailureResponse(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Throws default failure response. Default failure response is to wrap any
     * internal processing error that is not identified during implementation time
     * with custom error and message.
     *
     * @return
     */
    public static <T extends APIError> APIResponse<Void> invalidRequestBodyResponse(List<T> errors) {
        APIResponse<Void> apiResponse = constructFailureResponse(HttpStatus.BAD_REQUEST);
        apiResponse.errors = errors;
        return apiResponse;
    }

    /**
     * Returns Access denied response.
     *
     * @return
     */
    public static APIResponse<Void> accessDeniedResponse() {
        return constructFailureResponse(HttpStatus.UNAUTHORIZED);

    }

    private static APIResponse<Void> constructFailureResponse(HttpStatus httpStatus) {
        APIResponse<Void> apiResponse = new APIResponse(httpStatus);
        apiResponse.isSuccess = false;
        return apiResponse;
    }

    public static APIResponse<Void> constructFailureResponse(HttpStatus httpStatus, final String message) {
        APIResponse<Void> apiResponse = new APIResponse(httpStatus);
        apiResponse.isSuccess = false;
        apiResponse.message = message;
        return apiResponse;
    }

    /**
     * Default success response is returned when the processing is complete without
     * any errors and there is no specific response body is required to be wrapped.
     *
     * @return
     */
    public static APIResponse<Void> defaultSuccessResponse() {
        APIResponse<Void> apiResponse = new APIResponse(HttpStatus.OK);
        apiResponse.isSuccess = true;
        return apiResponse;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public T getBody() {
        return body;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public List<? extends APIError> getErrors() {
        return errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    private void setStatus(HttpStatus status) {
        this.status = status;
        this.statusCode = status.value();
        this.message = status.getReasonPhrase();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public static final class APIResponseBuilder<T> {
        private HttpStatus status;
        private String message;
        private T body;
        private boolean isSuccess;
        private LocalDateTime time = LocalDateTime.now();
        private List<? extends APIError> errors = new ArrayList<>();

        private APIResponseBuilder() {
        }

        public static APIResponseBuilder anAPIResponse() {
            return new APIResponseBuilder();
        }

        public APIResponseBuilder withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public APIResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public APIResponseBuilder withBody(T body) {
            this.body = body;
            return this;
        }

        public APIResponseBuilder withIsSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
            return this;
        }

        public APIResponseBuilder withTime(LocalDateTime time) {
            this.time = time;
            return this;
        }

        public APIResponseBuilder withErrors(List<? extends APIError> errors) {
            this.errors = errors;
            return this;
        }

        public APIResponse build() {
            APIResponse aPIResponse = new APIResponse(body, errors);
            aPIResponse.setStatus(this.status);
            aPIResponse.message = this.message;
            aPIResponse.body = this.body;
            aPIResponse.time = this.time;
            aPIResponse.isSuccess = this.isSuccess;
            return aPIResponse;
        }
    }
}

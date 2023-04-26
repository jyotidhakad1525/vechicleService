/**
 *
 */
package com.automate.vehicleservices.framework.exception;

import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.validation.FieldValidationError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * @author chandrashekharv
 *
 *         Generic exception handler that catches exceptions either re-thrown or
 *         propagated through controllers.
 *
 */
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Handles invalid arguments passed via request body.
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {

        List<FieldValidationError> errors = ex.getBindingResult().getFieldErrors().stream().map(fieldError ->
                new FieldValidationError(fieldError.getField(), fieldError.getDefaultMessage())).collect(toList());
        return new ResponseEntity<Object>(APIResponse.invalidRequestBodyResponse(errors), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleConstraintViolationException(DataIntegrityViolationException e) {
        return new ResponseEntity<Object>(APIResponse.constructFailureResponse(HttpStatus.BAD_REQUEST,
                e.getRootCause().getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        Optional<ConstraintViolation<?>> first = e.getConstraintViolations().stream().findFirst();
        String errorMessage = first.isPresent() ? first.get().getMessage() : e.getMessage();
        return new ResponseEntity<Object>(APIResponse.constructFailureResponse(HttpStatus.BAD_REQUEST,
                errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Invalid data received for parameter: %s, " +
                        "expecting type: %s, And received value is: %s",
                ex.getName(), ex.getRequiredType().getSimpleName(), ex.getValue());
        return new ResponseEntity<Object>(APIResponse.constructFailureResponse(HttpStatus.BAD_REQUEST,
                errorMessage), HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        return new ResponseEntity<Object>(APIResponse.invalidRequestBodyResponse(ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        String supportedMethods = StringUtils.joinWith(",", ex.getSupportedHttpMethods());
        return new ResponseEntity<Object>(APIResponse.constructFailureResponse(HttpStatus.METHOD_NOT_ALLOWED,
                new StringBuilder(ex.getMessage()).append(".").append(String.format("Supported Methods are: %s",
                        supportedMethods))
                        .toString()),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        return wrapResponseExceptionIntoResponseEntity(StringUtils.EMPTY, HttpStatus.NOT_FOUND, "API not found");
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return wrapResponseExceptionIntoResponseEntity(StringUtils.EMPTY, HttpStatus.NOT_FOUND,
                StringUtils.defaultIfEmpty(ex.getMessage(), "Resource not found"));
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Object> handleConflict(RuntimeException ex) {
        return wrapResponseExceptionIntoResponseEntity(StringUtils.EMPTY, HttpStatus.BAD_REQUEST, "Invalid input");
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(APIResponse.invalidRequestBodyResponse(String.format(
                "Malformed JSON request : Reason: %s", ex.getMessage())),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles runtime exceptions.
     */
    @ExceptionHandler
    public <T extends RuntimeException> ResponseEntity<Object> handleRuntimeException(T ex, WebRequest request) {
        ex.printStackTrace();
       /* RequestProvider requestProvider = new RequestProvider.Builder()
                .userIpHeaderName(request.getHeader("REMOTE_ADDR")).build();

       final var rollBar = Rollbar.init(
                ConfigBuilder.withAccessToken(rollBarClient.getAccessToken()).request(requestProvider).build());
        rollBar.error(ex);*/
        return new ResponseEntity<Object>(APIResponse.defaultFailureResponse(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Handles caught and rethrown runtime exceptions
     */
    @ExceptionHandler
    public <T extends RuntimeException> ResponseEntity<Object> vehicleServicesException(VehicleServicesException ex) {
        return wrapResponseExceptionIntoResponseEntity(StringUtils.EMPTY, ex.getStatus(), ex.getReason());

    }

    private ResponseEntity<Object> wrapResponseExceptionIntoResponseEntity(String body, HttpStatus status,
                                                                           String reason) {
        APIResponse apiResponse = APIResponse.APIResponseBuilder.anAPIResponse().withBody(body)
                .withStatus(status)
                .withMessage(reason)
                .withIsSuccess(false)
                .build();
        return new ResponseEntity<Object>(apiResponse, status);
    }

    /**
     * Handles response status exceptions
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(
            ResponseStatusException ex) {
        return wrapResponseExceptionIntoResponseEntity(StringUtils.EMPTY, ex.getStatus(), ex.getReason());

    }

    /**
     * Handles authorization exceptions.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(APIResponse.accessDeniedResponse(), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

}

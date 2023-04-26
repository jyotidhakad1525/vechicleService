package com.automate.vehicleservices.outbound;

import org.springframework.http.HttpHeaders;

import java.util.Map;

/**
 * Outbound caller interface.
 * <p>
 * R - return Object type
 * B - Request Body type
 * </p>
 *
 * @param <R>
 */
public interface OutboundCaller<R> {

    <B> R post(final String url, Map<String, Object> params, B requestBody, Class<R> clazz, HttpHeaders httpHeaders);

    R get(final String url, Map<String, Object> params, Class<R> clazz);
}

package com.automate.vehicleservices.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class RestAPIInvoker implements OutboundCaller {

    private final RestTemplate restTemplate;

    public RestAPIInvoker(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @SneakyThrows
    @Override
    public Object post(String url, Map params, Object requestBody, Class clazz, HttpHeaders httpHeaders) {
        log.info(String.format("POST URL: %s, Params: %s, requestBody: %s", url, params, requestBody.toString()));

        HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
        final var forObject = restTemplate.postForObject(url, httpEntity, clazz);

        log.info(String.format("Response: %s", new ObjectMapper().writeValueAsString(forObject)));
        return forObject;
    }

    @SneakyThrows
    @Override
    public Object get(String url, Map params, Class clazz) {
        log.info(String.format("GET URL: %s, Params: %s", url, params));
        final var forObject = restTemplate.getForObject(url, clazz);

        log.info(String.format("Response: %s", new ObjectMapper().writeValueAsString(forObject)));
        return forObject;
    }
}

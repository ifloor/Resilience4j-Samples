package com.samples.resilience4jsamples.circuitbreaker;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DownstreamRequest {

    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().rootUri("http://localhost:9123").build();
    }
}

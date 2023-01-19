package com.samples.resilience4jsamples.circuitbreaker;

import org.springframework.stereotype.Component;

@Component
public class DownstreamService {
    private final DownstreamRequest request;

    public DownstreamService(DownstreamRequest request) {
        this.request = request;
    }

    public String callApi() {
        return this.request.restTemplate().getForObject("/api/external", String.class);
    }
}

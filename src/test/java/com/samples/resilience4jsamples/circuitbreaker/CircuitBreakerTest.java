package com.samples.resilience4jsamples.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;

@SpringBootTest
public class CircuitBreakerTest {
    private static Logger LOG = Logger.getLogger(CircuitBreakerTest.class.getCanonicalName());

    @Test
    public void circuitBreakShowcase() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .minimumNumberOfCalls(10)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .waitDurationInOpenState(Duration.of(5, ChronoUnit.SECONDS))
                .permittedNumberOfCallsInHalfOpenState(30)
                .slidingWindowSize(100)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();

        CircuitBreaker circuitBreaker = CircuitBreaker.of("CircuitBreakerService", config);


        Supplier<String> decoratedProcessing = CircuitBreaker.decorateSupplier(circuitBreaker, CircuitBreakerTest::backendProcessing);

        /*Retry retry = Retry.ofDefaults("CircuitBreakerService");
        decoratedProcessing = Retry.decorateSupplier(retry, decoratedProcessing);*/

        for (int i = 0; i < 20; i++) {
            try {
                String processing = decoratedProcessing.get();
                LOG.info("Processing got output: " + processing);
            } catch (Exception e) {
                LOG.warning("Execution got exception: " + e.getMessage());
            }
        }

        try {
            LOG.info("Sleeping 5 seconds");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            LOG.warning("Exception when sleeping");
        }
        LOG.info("After 5 seconds");

        for (int i = 0; i < 10; i++) {
            try {
                String processing = decoratedProcessing.get();
                LOG.info("Processing got output after waiting: " + processing);
            } catch (Exception e) {
                LOG.warning("Execution got exception: " + e.getMessage());
            }
        }
    }

    public static String backendProcessing() {
        throw new RuntimeException("Not able to process");
//        return "abid";
    }
}

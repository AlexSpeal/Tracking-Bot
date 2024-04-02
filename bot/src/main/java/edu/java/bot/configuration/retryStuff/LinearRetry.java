package edu.java.bot.configuration.retryStuff;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import org.springframework.retry.RetryException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

public class LinearRetry extends Retry {
    private final int maxAttempts;
    private final Duration minDelay;

    private final Predicate<? super Throwable> filter;

    public LinearRetry(int maxAttempts, Duration minDelay, List<Integer> statusCodes) {
        this.maxAttempts = maxAttempts;
        this.minDelay = minDelay;
        filter = throwable -> statusCodes.contains(((ResponseStatusException) throwable).getStatusCode().value());
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return flux.flatMap(this::getRetry);
    }

    public Mono<Long> getRetry(RetrySignal rs) {
        if (!filter.test(rs.failure())) {
            return Mono.error(rs.failure());
        }
        if (rs.totalRetries() < maxAttempts) {
            log.info("Perform retry on {}", rs.failure().getLocalizedMessage());
            Duration delay = minDelay.multipliedBy(rs.totalRetries());

            log.debug("retry {} with backoff {} seconds", rs.totalRetries(), delay.toSeconds());
            return Mono.delay(delay).thenReturn(rs.totalRetries());
        } else {
            log.error("Retry failed");
            return Mono.error(new RetryException("Failed to process, max attempts"));
        }
    }
}

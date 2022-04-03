package com.solutions.ratelimiter.bucket4j;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
class TestController {

    private final Bucket bucket;

    public TestController() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping(value = "/api/v1/test")
    public ResponseEntity<String> test() {

        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok("YES! you can use it");
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();

    }
}
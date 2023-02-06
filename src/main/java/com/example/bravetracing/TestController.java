package com.example.bravetracing;

import io.micrometer.tracing.Tracer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
public class TestController {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    Tracer tracer;

    @RequestMapping(path = "/callable", produces = "text/html")
    Callable<ResponseEntity<String>> callable() {
        logger.info(String.format("1 - Thread %s - Span %s",Thread.currentThread().getName(), tracer.currentSpan().context().toString()));
        return () -> {
            logger.info(String.format("2 - Thread %s - Span %s",Thread.currentThread().getName(), tracer.currentSpan().context().toString()));
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        };
    }

    @RequestMapping(path = "/normal", method = RequestMethod.GET, produces = "text/html")
    ResponseEntity<String> normal() {
        logger.info(String.format("Thread %s - Span %s", Thread.currentThread().getName(), tracer.currentSpan().context().toString()));
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}

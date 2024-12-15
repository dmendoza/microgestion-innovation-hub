package com.microgestion.example.rag.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microgestion.example.rag.model.QueryRequest;
import com.microgestion.example.rag.model.QueryResponse;
import com.microgestion.example.rag.service.QueryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for handling query requests.
 * Provides endpoints for executing queries and health checks.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QueryController {

    private final QueryService queryService;

    /**
     * Executes a database query and returns the results.
     * 
     * @param request QueryRequest with the query to execute
     * @return QueryResponse with the query results and execution details
     */
    @PostMapping("/query")
    public ResponseEntity<QueryResponse> executeQuery(@Valid @RequestBody QueryRequest request) {
        log.info("Executing query 2: {}", request.getQuery());

        queryService.validateQuery(request.getQuery());
        return ResponseEntity.ok(queryService.executeQuery(request));
    }

    /**
     * Provides a health check endpoint for the application.
     * 
     * @return Map with the status and timestamp of the health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "healthy",
                "timestamp", LocalDateTime.now()));
    }

    /**
     * Handles exceptions thrown during request processing.
     * 
     * @param e Exception that was thrown
     * @return Map with the error message and timestamp
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleError(Exception e) {
        log.error("Error processing request", e);
        return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "timestamp", LocalDateTime.now()));
    }
}
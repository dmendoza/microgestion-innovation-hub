package com.microgestion.example.rag.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.microgestion.example.rag.model.QueryRequest;
import com.microgestion.example.rag.model.QueryResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for executing database queries.
 * Utilizes JdbcTemplate to interact with the database.
 * Logs query execution details and handles exceptions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Executes a database query and returns the results.
     * Validates the query to ensure it is a SELECT statement
     * and does not contain forbidden keywords.
     * 
     * @param request QueryRequest with the query to execute
     * @return QueryResponse with the query results and execution details
     */
    public QueryResponse executeQuery(QueryRequest request) {
        validateQuery(request.getQuery());

        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> results = new ArrayList<>();

        try {
            results = jdbcTemplate.queryForList(request.getQuery());
        } catch (Exception e) {
            log.error("Error executing query: {}", e.getMessage());
            throw new RuntimeException("Error executing query: " + e.getMessage());
        }
        log.info(results.toString());

        double executionTime = (System.currentTimeMillis() - startTime) / 1000.0;

        return QueryResponse.builder()
                .data(results)
                .rowCount(results.size())
                .executionTime(executionTime)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Validates the query to ensure it is a SELECT statement
     * and does not contain forbidden keywords.
     * 
     * @param query String with the query to validate
     */
    public void validateQuery(String query) {
        String normalizedQuery = query.toLowerCase().trim();
        log.info("Validating query: {}", normalizedQuery);

        // Validar que sea una consulta SELECT
        if (!normalizedQuery.startsWith("select")) {
            log.error("Only SELECT queries are allowed");
            throw new IllegalArgumentException("Only SELECT queries are allowed");
        }

        // Validar que no contenga palabras clave prohibidas
        List<String> forbiddenKeywords = List.of(
                "insert", "update", "delete", "drop", "create", "alter", "truncate");

        if (forbiddenKeywords.stream().anyMatch(normalizedQuery::contains)) {
            throw new IllegalArgumentException("Query contains forbidden keywords");
        }
    }
}
package com.microgestion.example.rag.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * Model class for a query response.
 * Contains the query results and execution details.
 */
@Data
@Builder
public class QueryResponse {
    private List<Map<String, Object>> data;
    private int rowCount;
    private double executionTime;
    private LocalDateTime timestamp;
}
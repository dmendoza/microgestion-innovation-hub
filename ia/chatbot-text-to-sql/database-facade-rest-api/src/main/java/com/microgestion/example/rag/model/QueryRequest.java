package com.microgestion.example.rag.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Model class for a query request.
 * Contains the query to execute and optional parameters.
 */
@Data
public class QueryRequest {
    @NotEmpty(message = "Query cannot be empty")
    private String query;
    private String explanation;
    private List<String> tables;
}
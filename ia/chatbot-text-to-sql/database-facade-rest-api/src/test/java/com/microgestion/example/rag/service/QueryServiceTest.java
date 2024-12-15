package com.microgestion.example.rag.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import com.microgestion.example.rag.model.QueryRequest;
import com.microgestion.example.rag.model.QueryResponse;

/**
 * Unit tests for the QueryService class.
 * Tests the executeQuery method with valid and invalid queries.
 */
@ExtendWith(MockitoExtension.class)
class QueryServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private QueryService queryService;

    private QueryRequest validRequest;

    /**
     * Sets up a valid QueryRequest object for testing.
     */
    @BeforeEach
    void setUp() {
        validRequest = new QueryRequest();
        validRequest.setQuery("SELECT * FROM TEST_TABLE");
        validRequest.setExplanation("Test query");
        validRequest.setTables(List.of("TEST_TABLE"));
    }

    /**
     * Tests the executeQuery method with a valid query.
     * Verifies that the response contains the expected results.
     */
    @Test
    void executeQuery_WithValidQuery_ShouldReturnResults() {
        // Given
        List<Map<String, Object>> expectedResults = List.of(
                Map.of("id", 1, "name", "Test"));
        when(jdbcTemplate.queryForList(anyString())).thenReturn(expectedResults);

        // When
        QueryResponse response = queryService.executeQuery(validRequest);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
        assertEquals(expectedResults, response.getData());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getExecutionTime() >= 0);
    }

    /**
     * Tests the executeQuery method with an invalid query.
     * Verifies that an IllegalArgumentException is thrown.
     */
    @Test
    void executeQuery_WithInvalidQuery_ShouldThrowException() {
        // Given
        validRequest.setQuery("DROP TABLE TEST_TABLE");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> queryService.executeQuery(validRequest));
    }

    /**
     * Tests the executeQuery method with a database error.
     * Verifies that a RuntimeException is thrown.
     */
    @Test
    void executeQuery_WithDatabaseError_ShouldThrowException() {
        // Given
        when(jdbcTemplate.queryForList(anyString())).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> queryService.executeQuery(validRequest));
    }
}
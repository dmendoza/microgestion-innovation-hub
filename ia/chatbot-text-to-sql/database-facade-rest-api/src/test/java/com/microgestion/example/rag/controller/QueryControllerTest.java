package com.microgestion.example.rag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microgestion.example.rag.DatabaseFacadeRestApiApplication;
import com.microgestion.example.rag.model.QueryRequest;
import com.microgestion.example.rag.model.QueryResponse;
import com.microgestion.example.rag.service.QueryService;

/**
 * Unit tests for the QueryController class.
 * Tests the health check and executeQuery endpoints.
 */
@SpringBootTest(classes = DatabaseFacadeRestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QueryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private QueryService queryService;

        @Autowired
        private ObjectMapper objectMapper;

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
         * Tests the health check endpoint.
         * Verifies that the response status is OK and contains the expected values.
         *
         * @throws Exception if an error occurs during the test
         */
        @Test
        void healthCheck_ShouldReturnHealthy() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/health"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("healthy"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
        }

        /**
         * Tests the executeQuery endpoint with a valid request.
         * Verifies that the response status is OK and contains the expected values.
         *
         * @throws Exception if an error occurs during the test
         */
        @Test
        void executeQuery_WithValidRequest_ShouldSucceed() throws Exception {
                // Given
                when(queryService.executeQuery(any(QueryRequest.class)))
                                .thenReturn(QueryResponse.builder()
                                                .data(List.of(Map.of("column1", "value1")))
                                                .rowCount(1)
                                                .executionTime(0.1)
                                                .timestamp(LocalDateTime.now())
                                                .build());

                // When
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)));

                // Then
                result.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.rowCount").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.executionTime").exists())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
        }

        /**
         * Tests the executeQuery endpoint with an empty query.
         *
         * @throws Exception if an error occurs during the test
         */
        @Test
        void executeQuery_WithEmptyQuery_ShouldReturnBadRequest() throws Exception {
                // Given
                validRequest.setQuery("");

                // When
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)));

                // Then
                result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
}
package com.arun.pwa.service;

import com.arun.pwa.config.AirtableConfig;
import com.arun.pwa.model.TodoDto;
import com.arun.pwa.model.airtable.AirtableFields;
import com.arun.pwa.model.airtable.AirtableRecord;
import com.arun.pwa.model.airtable.AirtableResponse;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AirtableService {

    private static final Logger logger = LoggerFactory.getLogger(AirtableService.class);

    private final AirtableConfig config;
    private final RestTemplate restTemplate;

    public AirtableService(AirtableConfig config) {
        this.config = config;
        // Use Apache HttpClient to support PATCH and other verbs reliably
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = new RestTemplate(requestFactory);

        logger.info("AirtableService initialized for base: {} table: {}", config.getBaseId(), config.getTableName());
    }

    private String baseUrl() {
        // Build URL with proper encoding of path segments (table names may contain spaces or special chars)
        return UriComponentsBuilder.fromHttpUrl("https://api.airtable.com")
                .pathSegment("v0", config.getBaseId(), config.getTableName())
                .toUriString();
    }

    private HttpHeaders headers() {
        String token = config.getToken();
        if (token == null || token.isBlank()) {
            logger.error("Airtable token is not configured");
            throw new IllegalStateException("Airtable token is not configured. Set 'airtable.token' in application.properties or as an environment variable.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    // GET ALL TODOS
    public List<TodoDto> getAllTodos() {
        HttpEntity<Void> entity = new HttpEntity<>(headers());

        try {
            logger.debug("Sending GET request to Airtable: {}", baseUrl());
            ResponseEntity<AirtableResponse> response =
                    restTemplate.exchange(
                            baseUrl(),
                            HttpMethod.GET,
                            entity,
                            AirtableResponse.class
                    );

            List<TodoDto> result = new ArrayList<>();

            if (response.getBody() == null) return result;

            for (AirtableRecord record : response.getBody().getRecords()) {
                AirtableFields f = record.getFields();

                TodoDto dto = new TodoDto();
                dto.setId(record.getId());
                dto.setTitle(f.getTitle());
                dto.setCompleted(f.isCompleted());
                dto.setCategory(f.getCategory());
                dto.setPriority(f.getPriority());
                dto.setCreatedAt(f.getCreatedAt());
                dto.setCompletedAt(f.getCompletedAt());

                result.add(dto);
            }

            logger.info("GET /todos - received {} records", result.size());
            return result;
        } catch (HttpClientErrorException e) {
            // Surface helpful diagnostic to the developer/ops — do not leak secrets
            String body = e.getResponseBodyAsString();
            logger.error("Airtable GET failed: {} - {}", e.getStatusCode(), body);
            throw new IllegalStateException("Airtable API returned HTTP " + e.getStatusCode() + ": " + body + " — check that the base ID, table name and API token are correct and that the token has access to the base.", e);
        }
    }

    // CREATE TODO
    public void createTodo(String title) {
        Map<String, Object> fields = Map.of(
                "title", title,
                "completed", false,
                "createdAt", Instant.now().toString()
        );

        Map<String, Object> record = Map.of("fields", fields);
        Map<String, Object> body = Map.of("records", List.of(record));

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers());

        try {
            logger.debug("Sending POST to Airtable: {} with payload: {}", baseUrl(), body);
            restTemplate.postForEntity(baseUrl(), entity, String.class);
            logger.info("Created todo with title: {}", title);
        } catch (HttpClientErrorException e) {
            String resp = e.getResponseBodyAsString();
            logger.error("Airtable create failed: {} - {}", e.getStatusCode(), resp);
            throw new IllegalStateException("Airtable create returned HTTP " + e.getStatusCode() + ": " + resp + " — check token/permissions and table name.", e);
        }
    }

    // MARK COMPLETE
    public void markCompleted(String recordId) {
        Map<String, Object> fields = Map.of(
                "completed", true,
                "completedAt", Instant.now().toString()
        );

        Map<String, Object> body = Map.of("fields", fields);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers());

        try {
            logger.debug("Sending PATCH to Airtable: {}/{} with payload: {}", baseUrl(), recordId, body);
            restTemplate.exchange(
                    baseUrl() + "/" + recordId,
                    HttpMethod.PATCH,
                    entity,
                    String.class
            );
            logger.info("Marked record {} as completed", recordId);
        } catch (HttpClientErrorException e) {
            String resp = e.getResponseBodyAsString();
            logger.error("Airtable patch failed: {} - {}", e.getStatusCode(), resp);
            throw new IllegalStateException("Airtable patch returned HTTP " + e.getStatusCode() + ": " + resp + " — check token/permissions and record id.", e);
        }
    }

    // DELETE TODO
    public void deleteTodo(String recordId) {
        HttpEntity<Void> entity = new HttpEntity<>(headers());

        try {
            logger.debug("Sending DELETE to Airtable: {}/{}", baseUrl(), recordId);
            restTemplate.exchange(
                    baseUrl() + "/" + recordId,
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );
            logger.info("Deleted record {}", recordId);
        } catch (HttpClientErrorException e) {
            String resp = e.getResponseBodyAsString();
            logger.error("Airtable delete failed: {} - {}", e.getStatusCode(), resp);
            throw new IllegalStateException("Airtable delete returned HTTP " + e.getStatusCode() + ": " + resp + " — check token/permissions and record id.", e);
        }
    }
}

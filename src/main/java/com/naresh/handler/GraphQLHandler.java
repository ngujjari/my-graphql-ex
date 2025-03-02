package com.naresh.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naresh.service.GraphQLService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphQLHandler implements HttpHandler {
    private final GraphQLService graphQLService;
    private final ObjectMapper objectMapper;
    
    public GraphQLHandler(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                String requestBody = br.lines().collect(Collectors.joining());
                Map<String, Object> requestMap = objectMapper.readValue(requestBody, Map.class);
                
                String query = (String) requestMap.get("query");
                Map<String, Object> variables = (Map<String, Object>) requestMap.getOrDefault("variables", new HashMap<>());
                
                String response = graphQLService.executeQuery(query, variables);
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception e) {
                String errorResponse = "{\"errors\":[{\"message\":\"" + e.getMessage() + "\"}]}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(400, errorResponse.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(errorResponse.getBytes());
                }
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
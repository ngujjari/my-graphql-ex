package com.naresh.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class RootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Java GraphQL and REST API Server\n" +
                          "Available endpoints:\n" +
                          "- POST /graphql - GraphQL endpoint\n" +
                          "- GET /api/products - Get all products\n" +
                          "- GET /api/products/{id} - Get product by ID\n" +
                          "- POST /api/products - Create a new product\n" +
                          "- PUT /api/products/{id} - Update a product\n" +
                          "- DELETE /api/products/{id} - Delete a product\n";
        
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
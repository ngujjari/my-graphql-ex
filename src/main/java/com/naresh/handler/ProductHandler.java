package com.naresh.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naresh.model.Product;
import com.naresh.service.ProductService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class ProductHandler implements HttpHandler {
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    
    public ProductHandler(ProductService productService) {
        this.productService = productService;
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        
        String response;
        int statusCode = 200;
        
        try {
            if (path.equals("/api/products")) {
                if ("GET".equals(method)) {
                    // Get all products
                    response = objectMapper.writeValueAsString(productService.getAllProducts());
                } else if ("POST".equals(method)) {
                    // Create a new product
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                        String requestBody = br.lines().collect(Collectors.joining());
                        Product product = objectMapper.readValue(requestBody, Product.class);
                        Product createdProduct = productService.createProduct(product);
                        response = objectMapper.writeValueAsString(createdProduct);
                        statusCode = 201;
                    }
                } else {
                    response = "{\"error\":\"Method not allowed\"}";
                    statusCode = 405;
                }
            } else {
                // Assume it's a path like /api/products/{id}
                String[] pathParts = path.split("/");
                if (pathParts.length == 4) {
                    String productId = pathParts[3];
                    if ("GET".equals(method)) {
                        // Get product by ID
                        Product product = productService.getProductById(productId);
                        if (product != null) {
                            response = objectMapper.writeValueAsString(product);
                        } else {
                            response = "{\"error\":\"Product not found\"}";
                            statusCode = 404;
                        }
                    } else if ("PUT".equals(method)) {
                        // Update product
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                            String requestBody = br.lines().collect(Collectors.joining());
                            Product product = objectMapper.readValue(requestBody, Product.class);
                            Product updatedProduct = productService.updateProduct(productId, product);
                            response = objectMapper.writeValueAsString(updatedProduct);
                        }
                    } else if ("DELETE".equals(method)) {
                        // Delete product
                        productService.deleteProduct(productId);
                        response = "{}";
                        statusCode = 204;
                    } else {
                        response = "{\"error\":\"Method not allowed\"}";
                        statusCode = 405;
                    }
                } else {
                    response = "{\"error\":\"Invalid path\"}";
                    statusCode = 404;
                }
            }
        } catch (Exception e) {
            response = "{\"error\":\"" + e.getMessage() + "\"}";
            statusCode = 500;
        }
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
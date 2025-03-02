package com.naresh;

import com.naresh.config.ServerConfig;
import com.naresh.handler.GraphQLHandler;
import com.naresh.handler.ProductHandler;
import com.naresh.handler.RootHandler;
import com.naresh.repository.ProductRepository;
import com.naresh.repository.impl.InMemoryProductRepository;
import com.naresh.service.GraphQLService;
import com.naresh.service.ProductService;
import com.naresh.service.impl.DefaultGraphQLService;
import com.naresh.service.impl.DefaultProductService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create server
        int port = ServerConfig.PORT;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Initialize repositories
        ProductRepository productRepository = new InMemoryProductRepository();
        
        // Initialize services
        ProductService productService = new DefaultProductService(productRepository);
        GraphQLService graphQLService = new DefaultGraphQLService(productService);
        
        // Register handlers
        server.createContext("/graphql", new GraphQLHandler(graphQLService));
        server.createContext("/api/products", new ProductHandler(productService));
        server.createContext("/", new RootHandler());
        
        // Start server
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port " + port);
    }
}
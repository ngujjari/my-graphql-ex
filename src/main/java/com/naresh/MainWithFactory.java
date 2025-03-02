package com.naresh;

import com.naresh.config.ServerConfig;
import com.naresh.factory.ServiceFactory;
import com.naresh.handler.GraphQLHandler;
import com.naresh.handler.ProductHandler;
import com.naresh.handler.RootHandler;
import com.naresh.service.GraphQLService;
import com.naresh.service.ProductService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainWithFactory {
    public static void main(String[] args) throws IOException {
        // Create server
        int port = ServerConfig.PORT;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Get services from factory
        ProductService productService = ServiceFactory.getProductService();
        GraphQLService graphQLService = ServiceFactory.getGraphQLService();
        
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
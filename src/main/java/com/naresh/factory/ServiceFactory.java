package com.naresh.factory;

import com.naresh.repository.ProductRepository;
import com.naresh.repository.impl.InMemoryProductRepository;
import com.naresh.service.GraphQLService;
import com.naresh.service.ProductService;
import com.naresh.service.impl.DefaultProductService;
import com.naresh.service.impl.EnhancedGraphQLService;

public class ServiceFactory {
    private static ProductRepository productRepository;
    private static ProductService productService;
    private static GraphQLService graphQLService;
    
    public static ProductRepository getProductRepository() {
        if (productRepository == null) {
            productRepository = new InMemoryProductRepository();
        }
        return productRepository;
    }
    
    public static ProductService getProductService() {
        if (productService == null) {
            productService = new DefaultProductService(getProductRepository());
        }
        return productService;
    }
    
    public static GraphQLService getGraphQLService() {
        if (graphQLService == null) {
            graphQLService = new EnhancedGraphQLService(getProductService());
        }
        return graphQLService;
    }
}
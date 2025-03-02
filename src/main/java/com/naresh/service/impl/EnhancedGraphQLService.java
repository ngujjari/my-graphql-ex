package com.naresh.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naresh.graphql.datafetcher.ProductDataFetcher;
import com.naresh.graphql.schema.SchemaProvider;
import com.naresh.service.GraphQLService;
import com.naresh.service.ProductService;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;

public class EnhancedGraphQLService implements GraphQLService {
    private final GraphQL graphQL;
    private final ObjectMapper objectMapper;
    
    public EnhancedGraphQLService(ProductService productService) {
        this.objectMapper = new ObjectMapper();
        
        // Get schema
        SchemaProvider schemaProvider = new SchemaProvider();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaProvider.getSchema();
        
        // Get data fetchers
        ProductDataFetcher productDataFetcher = new ProductDataFetcher(productService);
        
        // Create runtime wiring
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query", builder -> builder
                .dataFetcher("products", productDataFetcher.getAllProducts())
                .dataFetcher("product", productDataFetcher.getProductById())
            )
            .type("Mutation", builder -> builder
                .dataFetcher("createProduct", productDataFetcher.createProduct())
                .dataFetcher("updateProduct", productDataFetcher.updateProduct())
                .dataFetcher("deleteProduct", productDataFetcher.deleteProduct())
            )
            .build();
        
        // Generate schema
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        
        // Create GraphQL instance
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }
    
    @Override
    public String executeQuery(String query, Map<String, Object> variables) {
         ExecutionInput eInput =  ExecutionInput.newExecutionInput(query).extensions(variables).build();
        ExecutionResult executionResult = graphQL.execute(eInput);
        Map<String, Object> result = new HashMap<>();
        
        if (!executionResult.getErrors().isEmpty()) {
            result.put("errors", executionResult.getErrors());
        }
        
        result.put("data", executionResult.getData());
        
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            return "{\"errors\":[{\"message\":\"Error serializing response: " + e.getMessage() + "\"}]}";
        }
    }
}
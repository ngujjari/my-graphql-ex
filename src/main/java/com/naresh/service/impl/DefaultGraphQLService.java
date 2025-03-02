package com.naresh.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naresh.model.Product;
import com.naresh.service.GraphQLService;
import com.naresh.service.ProductService;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultGraphQLService implements GraphQLService {
    private final GraphQL graphQL;
    private final ObjectMapper objectMapper;
    
    public DefaultGraphQLService(ProductService productService) {
        this.objectMapper = new ObjectMapper();
        
        // Load schema from file or resource in a real application
        String schema = loadSchema();
        
        // Parse schema
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);
        
        // Create runtime wiring
        RuntimeWiring runtimeWiring = buildRuntimeWiring(productService);
        
        // Generate schema
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        
        // Create GraphQL instance
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }
    
    private String loadSchema() {
        // In a real application, this would load from a file or resource
        return "type Query {\n" +
               "  products: [Product]\n" +
               "  product(id: ID!): Product\n" +
               "}\n" +
               "\n" +
               "type Mutation {\n" +
               "  createProduct(name: String!, description: String, price: Float!, stock: Int!): Product\n" +
               "  updateProduct(id: ID!, name: String!, description: String, price: Float!, stock: Int!): Product\n" +
               "  deleteProduct(id: ID!): Boolean\n" +
               "}\n" +
               "\n" +
               "type Product {\n" +
               "  id: ID!\n" +
               "  name: String!\n" +
               "  description: String\n" +
               "  price: Float!\n" +
               "  stock: Int!\n" +
               "}";
    }
    
    private RuntimeWiring buildRuntimeWiring(ProductService productService) {
        return RuntimeWiring.newRuntimeWiring()
            .type("Query", builder -> builder
                .dataFetcher("products", environment -> productService.getAllProducts())
                .dataFetcher("product", environment -> {
                    String id = environment.getArgument("id");
                    return productService.getProductById(id);
                })
            )
            .type("Mutation", builder -> builder
                .dataFetcher("createProduct", environment -> {
                    String name = environment.getArgument("name");
                    String description = environment.getArgument("description");
                    double price = environment.getArgument("price");
                    int stock = environment.getArgument("stock");
                    
                    Product product = new Product(null, name, description, price, stock);
                    return productService.createProduct(product);
                })
                .dataFetcher("updateProduct", environment -> {
                    String id = environment.getArgument("id");
                    String name = environment.getArgument("name");
                    String description = environment.getArgument("description");
                    double price = environment.getArgument("price");
                    int stock = environment.getArgument("stock");
                    
                    Product product = new Product(null, name, description, price, stock);
                    return productService.updateProduct(id, product);
                })
                .dataFetcher("deleteProduct", environment -> {
                    String id = environment.getArgument("id");
                    try {
                        productService.deleteProduct(id);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
            )
            .build();
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
package com.naresh.graphql.schema;

import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.SchemaParser;

public class SchemaProvider {
    public TypeDefinitionRegistry getSchema() {
        SchemaParser schemaParser = new SchemaParser();
        return schemaParser.parse(getSchemaDefinition());
    }
    
    private String getSchemaDefinition() {
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
}
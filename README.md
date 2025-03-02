# Java GraphQL and REST API

This project demonstrates a pure Java implementation of both GraphQL and REST APIs without using any frameworks like Spring. The project uses design patterns to separate responsibilities and make it easier to add new models.

## Features

- Pure Java implementation using only the GraphQL Java library
- REST API endpoints for CRUD operations
- GraphQL API for flexible querying
- In-memory product database
- No external frameworks or dependencies (except GraphQL Java and Jackson for JSON processing)
- Design patterns for separation of concerns:
  - Repository Pattern
  - Factory Pattern
  - Dependency Injection
  - Interface Segregation

## Project Structure

- `model` - Data models
- `repository` - Data access layer
- `service` - Business logic layer
- `handler` - HTTP request handlers
- `graphql` - GraphQL schema and data fetchers
- `config` - Configuration
- `factory` - Service factory

## API Endpoints

### REST API

- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create a new product
- `PUT /api/products/{id}` - Update a product
- `DELETE /api/products/{id}` - Delete a product

### GraphQL API

- `POST /graphql` - GraphQL endpoint

## GraphQL Examples

Query all products:
```graphql
{
  products {
    id
    name
    price
    stock
  }
}
```

Query a specific product:
```graphql
{
  product(id: "1") {
    id
    name
    description
    price
    stock
  }
}
```

Create a new product:
```graphql
mutation {
  createProduct(
    name: "New Product"
    description: "Product description"
    price: 29.99
    stock: 100
  ) {
    id
    name
    price
  }
}
```

Update a product:
```graphql
mutation {
  updateProduct(
    id: "1"
    name: "Updated Product"
    description: "Updated description"
    price: 39.99
    stock: 50
  ) {
    id
    name
    price
  }
}
```

Delete a product:
```graphql
mutation {
  deleteProduct(id: "1")
}
```

## Adding New Models

To add a new model to the system:

1. Create a new model class in the `model` package
2. Create a repository interface and implementation in the `repository` package
3. Create a service interface and implementation in the `service` package
4. Update the GraphQL schema in `SchemaProvider`
5. Create data fetchers for the new model in the `graphql/datafetcher` package
6. Update the runtime wiring in `EnhancedGraphQLService`
7. Create a handler for REST endpoints
8. Register the handler in `Main`
package com.naresh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductService {
    private final Map<String, Product> products = new HashMap<>();
    
    public ProductService() {
        // Add some sample products
        createProduct(new Product(null, "Laptop", "High-performance laptop", 1299.99, 10));
        createProduct(new Product(null, "Smartphone", "Latest smartphone model", 799.99, 20));
        createProduct(new Product(null, "Headphones", "Noise-cancelling headphones", 199.99, 30));
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    
    public Product getProductById(String id) {
        return products.get(id);
    }
    
    public Product createProduct(Product product) {
        // Generate ID if not provided
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }
        
        // Validate product
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
        
        // Save product
        products.put(product.getId(), product);
        return product;
    }
    
    public Product updateProduct(String id, Product product) {
        if (!products.containsKey(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        
        product.setId(id);
        products.put(id, product);
        return product;
    }
    
    public void deleteProduct(String id) {
        if (!products.containsKey(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        
        products.remove(id);
    }
}
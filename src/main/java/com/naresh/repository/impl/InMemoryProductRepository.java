package com.naresh.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.naresh.model.Product;
import com.naresh.repository.ProductRepository;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> products = new HashMap<>();
    
    public InMemoryProductRepository() {
        // Add some sample products
        save(new Product(null, "Laptop", "High-performance laptop", 1299.99, 10));
        save(new Product(null, "Smartphone", "Latest smartphone model", 799.99, 20));
        save(new Product(null, "Headphones", "Noise-cancelling headphones", 199.99, 30));
    }
    
    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
    
    @Override
    public Product findById(String id) {
        return products.get(id);
    }
    
    @Override
    public Product save(Product product) {
        // Generate ID if not provided
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }
        
        products.put(product.getId(), product);
        return product;
    }
    
    @Override
    public void delete(String id) {
        products.remove(id);
    }
    
    @Override
    public boolean exists(String id) {
        return products.containsKey(id);
    }
}
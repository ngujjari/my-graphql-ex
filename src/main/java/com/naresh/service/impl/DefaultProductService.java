package com.naresh.service.impl;

import java.util.List;

import com.naresh.model.Product;
import com.naresh.repository.ProductRepository;
import com.naresh.service.ProductService;

public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;
    
    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Product createProduct(Product product) {
        // Validate product
        validateProduct(product);
        
        // Save product
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(String id, Product product) {
        if (!productRepository.exists(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        
        validateProduct(product);
        
        product.setId(id);
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(String id) {
        if (!productRepository.exists(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        
        productRepository.delete(id);
    }
    
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
    }
}
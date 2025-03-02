package com.naresh.repository;

import java.util.List;

import com.naresh.model.Product;

public interface ProductRepository {
    List<Product> findAll();
    Product findById(String id);
    Product save(Product product);
    void delete(String id);
    boolean exists(String id);
}
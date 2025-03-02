package com.naresh.graphql.datafetcher;

import com.naresh.model.Product;
import com.naresh.service.ProductService;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class ProductDataFetcher {
    private final ProductService productService;
    
    public ProductDataFetcher(ProductService productService) {
        this.productService = productService;
    }
    
    public DataFetcher<Iterable<Product>> getAllProducts() {
        return environment -> productService.getAllProducts();
    }
    
    public DataFetcher<Product> getProductById() {
        return environment -> {
            String id = environment.getArgument("id");
            return productService.getProductById(id);
        };
    }
    
    public DataFetcher<Product> createProduct() {
        return environment -> {
            String name = environment.getArgument("name");
            String description = environment.getArgument("description");
            double price = environment.getArgument("price");
            int stock = environment.getArgument("stock");
            
            Product product = new Product(null, name, description, price, stock);
            return productService.createProduct(product);
        };
    }
    
    public DataFetcher<Product> updateProduct() {
        return environment -> {
            String id = environment.getArgument("id");
            String name = environment.getArgument("name");
            String description = environment.getArgument("description");
            double price = environment.getArgument("price");
            int stock = environment.getArgument("stock");
            
            Product product = new Product(null, name, description, price, stock);
            return productService.updateProduct(id, product);
        };
    }
    
    public DataFetcher<Boolean> deleteProduct() {
        return environment -> {
            String id = environment.getArgument("id");
            try {
                productService.deleteProduct(id);
                return true;
            } catch (Exception e) {
                return false;
            }
        };
    }
}
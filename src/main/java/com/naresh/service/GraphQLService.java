package com.naresh.service;

import java.util.Map;

public interface GraphQLService {
    String executeQuery(String query, Map<String, Object> variables);
}
package com.example.orders.service;

import com.example.orders.model.Orders;
import com.example.orders.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public ResponseEntity<Object> addOrder(Orders orders) {

        Long id = orders.getProduct();
        String productServiceUrl = "http://localhost:8081/api/products/";

        ResponseEntity<Object> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(productServiceUrl + id, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("no existe el producto", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
            orderRepository.save(orders);
            return new ResponseEntity<>("Creado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error de producto", HttpStatus.NOT_FOUND);
        }
    }
}
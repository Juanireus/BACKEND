package com.example.orders;

import com.example.orders.model.Orders;
import com.example.orders.repositories.OrderRepository;
import com.example.orders.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrderCommunicationError() {
        Orders order = new Orders(null,10L,5,null);

        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        ResponseEntity<Object> response = orderService.addOrder(order);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al comunicarse con el servicio de productos", response.getBody());
    }

    @Test
    public void testAddOrderInsufficientStock() {
        Orders order = new Orders(null,10L,10,null);

        Map<String, Object> productMap = new HashMap<>();
        productMap.put("stock", 5L);

        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(productMap, HttpStatus.OK));

        ResponseEntity<Object> response = orderService.addOrder(order);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La cantidad ingresada supera el stock disponible", response.getBody());
    }

    @Test
    public void testAddOrderSuccess() {
        Orders order = new Orders(null,10L,5,null);

        Map<String, Object> productMap = new HashMap<>();
        productMap.put("stock", 10L);

        when(restTemplate.getForEntity("http://localhost:8081/api/products/10", Object.class))
                .thenReturn(new ResponseEntity<>(productMap, HttpStatus.OK));


        when(restTemplate.exchange(
                eq("http://localhost:8081/api/products/stock"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(String.class)))
                .thenReturn(new ResponseEntity<>("Stock actualizado con exito!", HttpStatus.OK));

        when(orderRepository.save(any(Orders.class))).thenReturn(order);

        ResponseEntity<Object> response = orderService.addOrder(order);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Orden creada exitosamente", response.getBody());
        verify(orderRepository, times(1)).save(order);
    }

}
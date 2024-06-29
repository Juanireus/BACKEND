package com.example.orders.controller;

import com.example.orders.model.Orders;
import com.example.orders.service.OrderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> newOrder(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody Orders orders, BindingResult bindingResult){

        String jwt = authorizationHeader.substring(7);

        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwt).getBody();
            String username = claims.getSubject();

        } catch (Exception e) {
            return new ResponseEntity<>("Token JWT inválido o expirado", HttpStatus.UNAUTHORIZED);
        }

        if(bindingResult.hasErrors()){

            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
        return orderService.addOrder(orders);
    }
}

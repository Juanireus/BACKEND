package com.example.login.controller;

import com.example.login.model.Login;
import com.example.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createUser(@RequestBody Login login){

        try {
            loginService.createUser(login);
            //return ResponseEntity.ok("Usuario creado");
            return new ResponseEntity<>("Usuario creado",HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear usuario: " + e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Login login) {
        String user = login.getUser();
        String password = login.getPassword();
        try {
            String token = loginService.authenticate(user, password);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }

}

package com.example.login.service;

import com.example.login.config.JwtConfig;
import com.example.login.model.Login;
import com.example.login.repositories.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public boolean createUser(Login login){

        loginRepository.save(login);
        try {
            String pass = login.getPassword();
            String password = passwordEncoder.encode(pass);
            login.setPassword(password);

            loginRepository.save(login);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }

    public String authenticate(String user, String password) {

        Login login = loginRepository.findByuser(user);
        String pass = login.getPassword();

        if (login != null && passwordEncoder.matches(password, pass)) {
            return generateToken(user);
        } else {
            throw new RuntimeException("Error");
        }
    }

    private String generateToken(String user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .setSubject(user)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();
    }

}

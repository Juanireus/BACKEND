package com.example.login.repositories;

import com.example.login.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
    Login findByuser(String user);
}
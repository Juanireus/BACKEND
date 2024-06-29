package com.ucc.crud_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class CrudServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrudServiceApplication.class, args);
	}

	@GetMapping(path = "/")
	public List<String> getNames(){
		return List.of("luciano");
	}
}
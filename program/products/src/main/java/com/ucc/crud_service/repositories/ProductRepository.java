package com.ucc.crud_service.repositories;

import com.ucc.crud_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{

}

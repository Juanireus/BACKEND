package com.ucc.crud_service.service;

import com.ucc.crud_service.model.Product;
import com.ucc.crud_service.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public ResponseEntity<Object> addproduct(Product product){

        if (product.getId() != null && productRepository.existsById(product.getId())) {
            throw new RuntimeException("El producto existe");
        }

        productRepository.save(product);
        return new ResponseEntity<>("Producto agregado", HttpStatus.OK);
    }

    public Product getoneproduct(Long id) { return productRepository.findById(id).orElse(null); }

    public ResponseEntity<Object> deleteproduct(Long id){

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("El articulo "+id+"no existe");
        }
        productRepository.deleteById(id);
        return new ResponseEntity<>("Producto eliminado", HttpStatus.OK);
    }

    public ResponseEntity<Object> updateproduct(Product product){

        Long id = product.getId();

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("El articulo "+id+"no existe");
        }
        productRepository.save(product);
        return new ResponseEntity<>("Producto modificado", HttpStatus.OK);
    }

}

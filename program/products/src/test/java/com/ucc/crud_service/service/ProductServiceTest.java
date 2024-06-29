package com.ucc.crud_service.service;

import com.ucc.crud_service.model.Product;
import com.ucc.crud_service.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getProducts(){

        Product product = new Product(4L,"producto-prueba",5000.00, 8500L);
        List<Product> products = Collections.singletonList(product);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getProducts();
        assertEquals(1,result.size());
        assertEquals(4L,result.get(0).getId());

    }

    @Test
    public void deleteProduct(){

        Long productId = 4L;

        when(productRepository.existsById(productId)).thenReturn(true);

        ResponseEntity<Object> response = productService.deleteproduct(productId);

        verify(productRepository,  times(1)).deleteById(productId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Producto eliminado con exito!",response.getBody());

    }

    @Test
    public void newProduct() {
        Product product = new Product(null, "producto-prueba", 5000.00, 8500L);

        when(productRepository.save(product)).thenReturn(product);

        ResponseEntity<Object> response = productService.addproduct(product);

        verify(productRepository, times(0)).existsById(any());
        verify(productRepository, times(1)).save(product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto agregado con éxito!", response.getBody());
    }

    @Test
    public void newProductWithId() {
        Product product = new Product(10L, "producto-prueba", 5000.00, 8500L);

        // Simular que el producto no existe
        when(productRepository.existsById(product.getId())).thenReturn(false);
        when(productRepository.save(product)).thenReturn(product);

        ResponseEntity<Object> response = productService.addproduct(product);

        verify(productRepository, times(1)).existsById(product.getId());
        verify(productRepository, times(1)).save(product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto agregado con éxito!", response.getBody());
    }

    @Test
    public void newProductExists() {
        Product product = new Product(10L, "producto-prueba", 5000.00, 8500L);

        when(productRepository.existsById(product.getId())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.addproduct(product);
        });

        verify(productRepository, times(1)).existsById(product.getId());
        verify(productRepository, times(0)).save(product);

        assertEquals("El producto ya existe", exception.getMessage());
    }

    @Test
    public void updateProduct(){

        Product product = new Product(10L, "producto-prueba", 5000.00, 8500L);

        when(productRepository.existsById(product.getId())).thenReturn(true);

        ResponseEntity<Object> response = productService.updateproduct(product);

        verify(productRepository, times(1)).existsById(product.getId());
        verify(productRepository, times(1)).save(product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto modificado con exito!", response.getBody());

    }

}

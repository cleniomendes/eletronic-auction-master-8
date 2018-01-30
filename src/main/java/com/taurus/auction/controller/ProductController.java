package com.taurus.auction.controller;

import com.taurus.auction.domain.Product;
import com.taurus.auction.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clenio on 29/01/2018.
 */
@RestController
@RequestMapping("/api/product")
@PreAuthorize("hasAuthority('Administrador')")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    @PatchMapping
    public ResponseEntity createProduct(@RequestBody Product product) {
        try {
            productRepository.saveAndFlush(product);
            log.info(String.format("save product=%s success", product.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping
    public ResponseEntity deleteProduct(@RequestBody Product product) {
        try {
            productRepository.delete(product);
            log.info(String.format("delete product=%s success", product.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping
    public ResponseEntity listAllCompany() {
        List<Product> products = new ArrayList<>();
        try {
            products = productRepository.findAll();
            if (products != null) {
                log.info(String.format("Found %s products", products.size()));
            }

        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }
        return ResponseEntity.ok().body(products);
    }
}

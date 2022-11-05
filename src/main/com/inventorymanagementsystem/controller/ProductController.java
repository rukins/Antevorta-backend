package com.inventorymanagementsystem.controller;

import com.inventorymanagementsystem.exception.serverexception.ServerException;
import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.service.ProductService;
import com.inventorymanagementsystem.utils.ProductJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        String body = ProductJsonUtils.getString(productService.getAll());

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ServerException {
        String body = ProductJsonUtils.getString(productService.getById(id));

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @PostMapping("/{arbitraryStoreName}")
    public ResponseEntity<?> create(@RequestBody String requestBody, @PathVariable String arbitraryStoreName) throws ServerException {
        String body = ProductJsonUtils.getString(productService.create(requestBody, arbitraryStoreName));

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody String requestBody, @PathVariable Long id) throws ServerException {
        String body = ProductJsonUtils.getString(productService.update(requestBody, id));

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ServerException {
        productService.delete(id);

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(),
                String.format("Product with id '%o' has been successfully deleted", id));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProductList() {
        productService.updateProductList();

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(), "Product list has been successfully updated");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

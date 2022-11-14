package com.antevorta.controller;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.ResponseBody;
import com.antevorta.service.ProductService;
import com.antevorta.utils.ProductJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        String body = ProductJsonUtils.getString(productService.getAll());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ServerException {
        String body = ProductJsonUtils.getString(productService.getById(id));

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody String requestBody, @PathVariable Long id) throws ServerException {
        String body = ProductJsonUtils.getString(productService.update(requestBody, id));

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ServerException {
        productService.delete(id);

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(),
                String.format("Product with id '%o' has been successfully deleted", id));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/merge")
    public ResponseEntity<?> merge(@RequestBody String productJson, @RequestParam("id") String productIds) throws ServerException {
        String body = ProductJsonUtils.getString(productService.merge(productJson, productIds));

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProductList() {
        productService.updateProductList();

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(), "Product list has been successfully updated");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

package com.antevorta.controller;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.ResponseBody;
import com.antevorta.service.ProductService;
import com.antevorta.utils.ProductJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(ProductJsonUtils.getString(productService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ServerException {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody String requestBody, @PathVariable Long id) throws ServerException {
        return ResponseEntity.ok(productService.update(requestBody, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ServerException {
        productService.delete(id);

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(),
                        String.format("Product with id '%o' has been successfully deleted", id))
        );
    }

    @PostMapping("/merge")
    public ResponseEntity<?> merge(@RequestBody String productJson, @RequestParam("id") String productIds) throws ServerException {
        return ResponseEntity.ok(productService.merge(productJson, productIds));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProductList() {
        productService.updateProductList();

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(), "Product list has been successfully updated")
        );
    }
}

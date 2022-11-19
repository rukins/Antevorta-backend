package com.antevorta.controller;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.OnlineStoreDetails;
import com.antevorta.model.OnlineStoreType;
import com.antevorta.model.ResponseBody;
import com.antevorta.service.OnlineStoreService;
import com.antevorta.service.ProductService;
import com.antevorta.utils.OnlineStoreJsonUtils;
import com.antevorta.utils.ProductJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onlinestores")
public class OnlineStoreController {
    private final OnlineStoreService onlineStoreService;
    private final ProductService productService;

    @Autowired
    public OnlineStoreController(OnlineStoreService onlineStoreService, ProductService productService) {
        this.onlineStoreService = onlineStoreService;
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(
                        OnlineStoreJsonUtils.getString(onlineStoreService.getAll())
                );
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getAllByType(@PathVariable String type) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(OnlineStoreJsonUtils.getString(onlineStoreService.getAllByType(OnlineStoreType.valueOf(type.toUpperCase())),
                        OnlineStoreType.valueOf(type.toUpperCase()))
                );
    }

    @PostMapping("")
    public ResponseEntity<?> addToUser(@RequestBody OnlineStoreDetails onlineStoreDetails)
            throws ServerException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(
                        OnlineStoreJsonUtils.getString(onlineStoreService.addToUser(onlineStoreDetails))
                );
    }

    @PutMapping("/{currentName}")
    public ResponseEntity<?> updateArbitraryStoreName(@PathVariable String currentName, @RequestParam("new") String newName)
            throws ServerException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(
                        OnlineStoreJsonUtils.getString(onlineStoreService.updateArbitraryStoreName(currentName, newName))
                );
    }

    @DeleteMapping("/{arbitraryStoreName}")
    public ResponseEntity<?> deleteByArbitraryStoreName(@PathVariable String arbitraryStoreName)
            throws ServerException {
        onlineStoreService.deleteByArbitraryStoreName(arbitraryStoreName);

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(),
                        String.format("Online store '%s' has been successfully deleted", arbitraryStoreName))
        );
    }

    @GetMapping("/{arbitraryStoreName}/products")
    public ResponseEntity<?> getAllProductsByArbitraryStoreName(@PathVariable String arbitraryStoreName) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(
                        ProductJsonUtils.getString(productService.getAllByArbitraryStoreName(arbitraryStoreName))
                );
    }

    @PostMapping("/{arbitraryStoreName}/products")
    public ResponseEntity<?> createProduct(@RequestBody String requestBody, @PathVariable String arbitraryStoreName)
            throws ServerException {
        return ResponseEntity.ok(productService.create(requestBody, arbitraryStoreName));
    }
}

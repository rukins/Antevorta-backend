package com.antevorta.controller;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.OnlineStoreDetails;
import com.antevorta.model.OnlineStoreType;
import com.antevorta.model.ResponseBody;
import com.antevorta.service.OnlineStoreService;
import com.antevorta.service.ProductService;
import com.antevorta.utils.OnlineStoreJsonUtils;
import com.antevorta.utils.ProductJsonUtils;
import com.antevorta.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/onlinestores")
public class OnlineStoreController {
    private static final Logger logger = LoggerFactory.getLogger(OnlineStoreController.class);

    private final OnlineStoreService onlineStoreService;
    private final ProductService productService;

    @Autowired
    public OnlineStoreController(OnlineStoreService onlineStoreService, ProductService productService) {
        this.onlineStoreService = onlineStoreService;
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        String body = OnlineStoreJsonUtils.getString(onlineStoreService.getAll());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body);
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getAllByType(@PathVariable String type, HttpServletRequest request) {
        String body = OnlineStoreJsonUtils.getString(
                onlineStoreService.getAllByType(OnlineStoreType.valueOf(type.toUpperCase())),
                OnlineStoreType.valueOf(type.toUpperCase())
        );

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body);
    }

    @PostMapping("")
    public ResponseEntity<?> addToUser(@RequestBody OnlineStoreDetails onlineStoreDetails, HttpServletRequest request)
            throws ServerException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(OnlineStoreJsonUtils.getString(onlineStoreService.addToUser(onlineStoreDetails)));
    }

    @PutMapping("/{currentName}")
    public ResponseEntity<?> updateArbitraryStoreName(@PathVariable String currentName, @RequestParam("new") String newName)
            throws ServerException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(OnlineStoreJsonUtils.getString(onlineStoreService.updateArbitraryStoreName(currentName, newName)));
    }

    @DeleteMapping("/{arbitraryStoreName}")
    public ResponseEntity<?> deleteByArbitraryStoreName(@PathVariable String arbitraryStoreName, HttpServletRequest request)
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
                .body(ProductJsonUtils.getString(productService.getAllByArbitraryStoreName(arbitraryStoreName)));
    }

    @PostMapping("/{arbitraryStoreName}/products")
    public ResponseEntity<?> createProduct(@RequestBody String requestBody, @PathVariable String arbitraryStoreName)
            throws ServerException {
        return ResponseEntity.ok(productService.create(requestBody, arbitraryStoreName));
    }
}

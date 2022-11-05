package com.inventorymanagementsystem.controller;

import com.inventorymanagementsystem.exception.serverexception.ServerException;
import com.inventorymanagementsystem.model.OnlineStoreDetails;
import com.inventorymanagementsystem.model.OnlineStoreType;
import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.service.OnlineStoreService;
import com.inventorymanagementsystem.service.ProductService;
import com.inventorymanagementsystem.utils.OnlineStoreJsonUtils;
import com.inventorymanagementsystem.utils.ProductJsonUtils;
import com.inventorymanagementsystem.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
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

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @PostMapping("")
    public ResponseEntity<?> addToUser(@RequestBody OnlineStoreDetails onlineStoreDetails, HttpServletRequest request)
            throws ServerException {
        onlineStoreService.addToUser(onlineStoreDetails);

        ResponseBody body = new ResponseBody(HttpStatus.CREATED.value(),
                String.format("Online store '%s' has been successfully added", onlineStoreDetails.getArbitraryStoreName()));

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PutMapping("/{currentName}")
    public ResponseEntity<?> updateArbitraryStoreName(@PathVariable String currentName, @RequestParam("new") String newName,
                                                   HttpServletRequest request) throws ServerException {
        onlineStoreService.updateArbitraryStoreName(currentName, newName);

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(),
                String.format("Arbitrary store name '%s' of online store has been successfully updated to '%s'",
                        currentName, newName));

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{arbitraryStoreName}")
    public ResponseEntity<?> deleteByArbitraryStoreName(@PathVariable String arbitraryStoreName, HttpServletRequest request)
            throws ServerException {
        onlineStoreService.deleteByArbitraryStoreName(arbitraryStoreName);

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(),
                String.format("Online store '%s' has been successfully deleted", arbitraryStoreName));

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/{arbitraryStoreName}/products")
    public ResponseEntity<?> getAllProductsByArbitraryStoreName(@PathVariable String arbitraryStoreName) {
        String body = ProductJsonUtils.getString(productService.getAllByArbitraryStoreName(arbitraryStoreName));

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @PostMapping("/{arbitraryStoreName}/products")
    public ResponseEntity<?> createProduct(@RequestBody String requestBody, @PathVariable String arbitraryStoreName) throws ServerException {
        String body = ProductJsonUtils.getString(productService.create(requestBody, arbitraryStoreName));

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }
}

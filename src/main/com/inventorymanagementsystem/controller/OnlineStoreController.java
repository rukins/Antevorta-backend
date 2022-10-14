package com.inventorymanagementsystem.controller;

import com.inventorymanagementsystem.model.OnlineStoreDetails;
import com.inventorymanagementsystem.model.OnlineStoreType;
import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.service.OnlineStoreService;
import com.inventorymanagementsystem.utils.OnlineStoreJsonUtils;
import com.inventorymanagementsystem.utils.RequestPaths;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RequestPaths.ONLINE_STORES)
public class OnlineStoreController {
    private final OnlineStoreService onlineStoreService;

    public OnlineStoreController(OnlineStoreService onlineStoreService) {
        this.onlineStoreService = onlineStoreService;
    }

    @PostMapping("")
    public ResponseEntity<?> addOnlineStoreToUser(@RequestBody OnlineStoreDetails onlineStoreDetails) {
        onlineStoreService.addOnlineStoreToUser(onlineStoreDetails);

        ResponseBody messageBody = new ResponseBody(HttpStatus.CREATED.value(), "Online store successfully added",
                RequestPaths.ONLINE_STORES);

        return new ResponseEntity<>(messageBody, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllOnlineStoresOfCurrentUser() {
        String body = OnlineStoreJsonUtils.getString(onlineStoreService.getAllOnlineStoresOfCurrentUser());

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getAllOnlineStoresOfCurrentUserByType(@PathVariable String type) {
        String body = OnlineStoreJsonUtils.getString(
                onlineStoreService.getAllOnlineStoresOfCurrentUserByType(OnlineStoreType.valueOf(type.toUpperCase())),
                OnlineStoreType.valueOf(type.toUpperCase())
        );

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }
}

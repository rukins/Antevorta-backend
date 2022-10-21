package com.inventorymanagementsystem.controller;

import com.inventorymanagementsystem.exception.globalexception.ArbitraryStoreNameNotUniqueException;
import com.inventorymanagementsystem.exception.globalexception.EmptyArbitraryStoreNameException;
import com.inventorymanagementsystem.exception.globalexception.EntityNotFoundException;
import com.inventorymanagementsystem.exception.globalexception.MultipleOnlineStoresException;
import com.inventorymanagementsystem.model.OnlineStoreDetails;
import com.inventorymanagementsystem.model.OnlineStoreType;
import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.service.OnlineStoreService;
import com.inventorymanagementsystem.utils.OnlineStoreJsonUtils;
import com.inventorymanagementsystem.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public OnlineStoreController(OnlineStoreService onlineStoreService) {
        this.onlineStoreService = onlineStoreService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllOnlineStoresOfCurrentUser(HttpServletRequest request) {
        String body = OnlineStoreJsonUtils.getString(onlineStoreService.getAllOnlineStoresOfCurrentUser());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body);

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getAllOnlineStoresOfCurrentUserByType(@PathVariable String type, HttpServletRequest request) {
        String body = OnlineStoreJsonUtils.getString(
                onlineStoreService.getAllOnlineStoresOfCurrentUserByType(OnlineStoreType.valueOf(type.toUpperCase())),
                OnlineStoreType.valueOf(type.toUpperCase())
        );

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body);

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "application/json").body(body);
    }

    @PostMapping("")
    public ResponseEntity<?> addOnlineStoreToUser(@RequestBody OnlineStoreDetails onlineStoreDetails, HttpServletRequest request)
            throws EmptyArbitraryStoreNameException, MultipleOnlineStoresException, ArbitraryStoreNameNotUniqueException {
        onlineStoreService.addOnlineStoreToUser(onlineStoreDetails);

        ResponseBody body = new ResponseBody(HttpStatus.CREATED.value(),
                String.format("Online store '%s' successfully added", onlineStoreDetails.getArbitraryStoreName()));

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PutMapping("/{currentName}")
    public ResponseEntity<?> updateOnlineStoreName(@PathVariable String currentName, @RequestParam String newName,
                                                   HttpServletRequest request)
            throws EmptyArbitraryStoreNameException, EntityNotFoundException, ArbitraryStoreNameNotUniqueException {
        onlineStoreService.updateOnlineStoreName(currentName, newName);

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(),
                String.format("Name '%s' of online store successfully updated to '%s'", currentName, newName));

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteOnlineStoreByName(@PathVariable String name, HttpServletRequest request)
            throws EntityNotFoundException {
        onlineStoreService.deleteOnlineStoreByName(name);

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(), String.format("Online store '%s' successfully deleted", name));

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

package com.antevorta.controller;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.ResponseBody;
import com.antevorta.service.InventoryItemService;
import com.antevorta.utils.InventoryItemJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventoryitems")
public class InventoryItemController {
    private final InventoryItemService inventoryItemService;

    @Autowired
    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(InventoryItemJsonUtils.getString(inventoryItemService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ServerException {
        return ResponseEntity.ok(inventoryItemService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody String requestBody, @PathVariable Long id) throws ServerException {
        return ResponseEntity.ok(inventoryItemService.update(requestBody, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ServerException {
        inventoryItemService.delete(id);

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(),
                        String.format("Product with id '%o' has been successfully deleted", id))
        );
    }

    @PostMapping("/merge")
    public ResponseEntity<?> merge(@RequestBody String productJson, @RequestParam("id") String inventoryIds) throws ServerException {
        return ResponseEntity.ok(inventoryItemService.merge(productJson, inventoryIds));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateInventoryItemList() {
        inventoryItemService.updateInventoryItemList();

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(), "Inventory item list has been successfully updated")
        );
    }
}

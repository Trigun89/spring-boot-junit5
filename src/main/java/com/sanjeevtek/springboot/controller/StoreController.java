package com.sanjeevtek.springboot.controller;

import com.sanjeevtek.springboot.model.Stores;
import com.sanjeevtek.springboot.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Stores createStore(@RequestBody Stores stores){
        return storeService.saveStore(stores);
    }

    @GetMapping
    public List<Stores> getAllStores(){
        return storeService.getAllStores();
    }

    @GetMapping("{id}")
    public ResponseEntity<Stores> getStoreById(@PathVariable("id") long storeId){
        return storeService.getStoreById(storeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Stores> updateStore(@PathVariable("id") long storeId,
                                                 @RequestBody Stores stores){
        return storeService.getStoreById(storeId)
                .map(savedStore -> {

                    savedStore.setFirstName(stores.getFirstName());
                    savedStore.setLastName(stores.getLastName());
                    savedStore.setEmail(stores.getEmail());

                    Stores updatedStores = storeService.updateStore(savedStore);
                    return new ResponseEntity<>(updatedStores, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteStore(@PathVariable("id") long storeId){

        storeService.deleteStore(storeId);

        return new ResponseEntity<String>("Stores deleted successfully!.", HttpStatus.OK);

    }

}

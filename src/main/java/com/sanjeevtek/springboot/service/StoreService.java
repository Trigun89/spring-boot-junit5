package com.sanjeevtek.springboot.service;

import com.sanjeevtek.springboot.model.Stores;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    Stores saveStore(Stores stores);
    List<Stores> getAllStores();
    Optional<Stores> getStoreById(long id);
    Stores updateStore(Stores updatedStores);
    void deleteStore(long id);
}

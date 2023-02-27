package com.sanjeevtek.springboot.service.impl;

import com.sanjeevtek.springboot.exception.ResourceNotFoundException;
import com.sanjeevtek.springboot.model.Stores;
import com.sanjeevtek.springboot.repository.StoreRepository;
import com.sanjeevtek.springboot.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    private StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Stores saveStore(Stores stores) {

        Optional<Stores> savedStore = storeRepository.findByEmail(stores.getEmail());
        if(savedStore.isPresent()){
            throw new ResourceNotFoundException("Stores already exist with given email:" + stores.getEmail());
        }
        return storeRepository.save(stores);
    }

    @Override
    public List<Stores> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Optional<Stores> getStoreById(long id) {
        return storeRepository.findById(id);
    }

    @Override
    public Stores updateStore(Stores updatedStores) {
        return storeRepository.save(updatedStores);
    }

    @Override
    public void deleteStore(long id) {
        storeRepository.deleteById(id);
    }
}

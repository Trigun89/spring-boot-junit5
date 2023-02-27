package com.sanjeevtek.springboot.service;

import com.sanjeevtek.springboot.model.Stores;
import com.sanjeevtek.springboot.exception.ResourceNotFoundException;
import com.sanjeevtek.springboot.repository.StoreRepository;
import com.sanjeevtek.springboot.service.impl.StoreServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StoresServiceTests {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    private Stores stores;

    @BeforeEach
    public void setup(){
        //storeRepository = Mockito.mock(StoreRepository.class);
        //storeService = new StoreServiceImpl(storeRepository);
        stores = Stores.builder()
                .id(1L)
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
    }

    // JUnit test for saveStore method
    @DisplayName("JUnit test for saveStore method")
    @Test
    public void givenStoreObject_whenSaveStore_thenReturnStoreObject(){
        // given - precondition or setup
        given(storeRepository.findByEmail(stores.getEmail()))
                .willReturn(Optional.empty());

        given(storeRepository.save(stores)).willReturn(stores);

        System.out.println(storeRepository);
        System.out.println(storeService);

        // when -  action or the behaviour that we are going test
        Stores savedStores = storeService.saveStore(stores);

        System.out.println(savedStores);
        // then - verify the output
        assertThat(savedStores).isNotNull();
    }

    // JUnit test for saveStore method
    @DisplayName("JUnit test for saveStore method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveStore_thenThrowsException(){
        // given - precondition or setup
        given(storeRepository.findByEmail(stores.getEmail()))
                .willReturn(Optional.of(stores));

        System.out.println(storeRepository);
        System.out.println(storeService);

        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            storeService.saveStore(stores);
        });

        // then
        verify(storeRepository, never()).save(any(Stores.class));
    }

    // JUnit test for getAllStores method
    @DisplayName("JUnit test for getAllStores method")
    @Test
    public void givenStoresList_whenGetAllStores_thenReturnStoresList(){
        // given - precondition or setup

        Stores stores1 = Stores.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();

        given(storeRepository.findAll()).willReturn(List.of(stores, stores1));

        // when -  action or the behaviour that we are going test
        List<Stores> storesList = storeService.getAllStores();

        // then - verify the output
        assertThat(storesList).isNotNull();
        assertThat(storesList.size()).isEqualTo(2);
    }

    // JUnit test for getAllStores method
    @DisplayName("JUnit test for getAllStores method (negative scenario)")
    @Test
    public void givenEmptyStoresList_whenGetAllStores_thenReturnEmptyStoresList(){
        // given - precondition or setup

        Stores stores1 = Stores.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();

        given(storeRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        List<Stores> storesList = storeService.getAllStores();

        // then - verify the output
        assertThat(storesList).isEmpty();
        assertThat(storesList.size()).isEqualTo(0);
    }

    // JUnit test for getStoreById method
    @DisplayName("JUnit test for getStoreById method")
    @Test
    public void givenStoreId_whenGetStoreById_thenReturnStoreObject(){
        // given
        given(storeRepository.findById(1L)).willReturn(Optional.of(stores));

        // when
        Stores savedStores = storeService.getStoreById(stores.getId()).get();

        // then
        assertThat(savedStores).isNotNull();

    }

    // JUnit test for updateStore method
    @DisplayName("JUnit test for updateStore method")
    @Test
    public void givenStoreObject_whenUpdateStore_thenReturnUpdatedStore(){
        // given - precondition or setup
        given(storeRepository.save(stores)).willReturn(stores);
        stores.setEmail("ram@gmail.com");
        stores.setFirstName("Ayman");
        // when -  action or the behaviour that we are going test
        Stores updatedStores = storeService.updateStore(stores);

        // then - verify the output
        assertThat(updatedStores.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedStores.getFirstName()).isEqualTo("Ayman");
    }

    // JUnit test for deleteStore method
    @DisplayName("JUnit test for deleteStore method")
    @Test
    public void givenStoreId_whenDeleteStore_thenNothing(){
        // given - precondition or setup
        long StoreId = 1L;

        willDoNothing().given(storeRepository).deleteById(StoreId);

        // when -  action or the behaviour that we are going test
        storeService.deleteStore(StoreId);

        // then - verify the output
        verify(storeRepository, times(1)).deleteById(StoreId);
    }
}

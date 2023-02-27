package com.sanjeevtek.springboot.repository;

import com.sanjeevtek.springboot.model.Stores;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoresRepositoryTests {

    @Autowired
    private StoreRepository storeRepository;

    private Stores stores;

    @BeforeEach
    public void setup(){
        stores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail,com")
                .build();
    }
    // JUnit test for save stores operation
    //@DisplayName("JUnit test for save stores operation")
    @Test
    public void givenStoreObject_whenSave_thenReturnSavedStore(){

        //given - precondition or setup
        Stores stores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Sanjeev")
                .email("Sanjeev@gmail,com")
                .build();
        // when - action or the behaviour that we are going test
        Stores savedStores = storeRepository.save(stores);

        // then - verify the output
        assertThat(savedStores).isNotNull();
        assertThat(savedStores.getId()).isGreaterThan(0);
    }


    // JUnit test for get all Stores operation
    @DisplayName("JUnit test for get all Stores operation")
    @Test
    public void givenStoresList_whenFindAll_thenStoresList(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Sanjeev")
//                .email("Sanjeev@gmail,com")
//                .build();

        Stores stores1 = Stores.builder()
                .firstName("John")
                .lastName("Cena")
                .email("cena@gmail,com")
                .build();

        storeRepository.save(stores);
        storeRepository.save(stores1);

        // when -  action or the behaviour that we are going test
        List<Stores> storesList = storeRepository.findAll();

        // then - verify the output
        assertThat(storesList).isNotNull();
        assertThat(storesList.size()).isEqualTo(3);

    }

    // JUnit test for get stores by id operation
    @DisplayName("JUnit test for get stores by id operation")
    @Test
    public void givenStoreObject_whenFindById_thenReturnStoreObject(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Sanjeev")
//                .email("Sanjeev@gmail,com")
//                .build();
        storeRepository.save(stores);

        // when -  action or the behaviour that we are going test
        Stores storesDB = storeRepository.findById(stores.getId()).get();

        // then - verify the output
        assertThat(storesDB).isNotNull();
    }

    // JUnit test for get stores by email operation
    @DisplayName("JUnit test for get stores by email operation")
    @Test
    public void givenStoreEmail_whenFindByEmail_thenReturnStoreObject(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Kumar S")
//                .email("Sanjeev@gmail,com")
//                .build();
        storeRepository.save(stores);

        // when -  action or the behaviour that we are going test
        Stores storesDB = storeRepository.findByEmail(stores.getEmail()).get();

        // then - verify the output
        assertThat(storesDB).isNotNull();
    }

    // JUnit test for update stores operation
    @DisplayName("JUnit test for update stores operation")
    @Test
    public void givenStoreObject_whenUpdateStore_thenReturnUpdatedStore(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Kumar S")
//                .email("Sanjeev@gmail,com")
//                .build();
        storeRepository.save(stores);

        // when -  action or the behaviour that we are going test
        Stores savedStores = storeRepository.findById(stores.getId()).get();
        savedStores.setEmail("ram@gmail.com");
        savedStores.setFirstName("Ayman");
        Stores updatedStores =  storeRepository.save(savedStores);

        // then - verify the output
        assertThat(updatedStores.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedStores.getFirstName()).isEqualTo("Ayman");
    }

    // JUnit test for delete stores operation
    @DisplayName("JUnit test for delete stores operation")
    @Test
    public void givenStoreObject_whenDelete_thenRemoveStore(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Kumar S")
//                .email("Sanjeev@gmail,com")
//                .build();
        storeRepository.save(stores);

        // when -  action or the behaviour that we are going test
        storeRepository.deleteById(stores.getId());
        Optional<Stores> StoreOptional = storeRepository.findById(stores.getId());

        // then - verify the output
        assertThat(StoreOptional).isEmpty();
    }

    // JUnit test for custom query using JPQL with index
    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnStoreObject(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Kumar S")
//                .email("Sanjeev@gmail,com")
//                .build();
        storeRepository.save(stores);
        String firstName = "Sanjeev";
        String lastName = "Kumar S";

        // when -  action or the behaviour that we are going test
        Stores savedStores = storeRepository.findByJPQL(firstName, lastName);

        // then - verify the output
        assertThat(savedStores).isNotNull();
    }

    // JUnit test for custom query using JPQL with Named params
    @DisplayName("JUnit test for custom query using JPQL with Named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnStoreObject(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Kumar S")
//                .email("Sanjeev@gmail,com")
//                .build();
        storeRepository.save(stores);
        String firstName = "Sanjeev";
        String lastName = "Kumar S";

        // when -  action or the behaviour that we are going test
        Stores savedStores = storeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - verify the output
        assertThat(savedStores).isNotNull();
    }

    // JUnit test for custom query using native SQL with index
    @DisplayName("JUnit test for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnStoreObject(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Kumar S")
//                .email("Sanjeev@gmail,com")
//                .build();
        storeRepository.save(stores);
        // String firstName = "Sanjeev";
        // String lastName = "Kumar S";

        // when -  action or the behaviour that we are going test
        Stores savedStores = storeRepository.findByNativeSQL(stores.getFirstName(), stores.getLastName());

        // then - verify the output
        assertThat(savedStores).isNotNull();
    }

    // JUnit test for custom query using native SQL with named params
    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnStoreObject(){
        // given - precondition or setup
//        Stores stores = Stores.builder()
//                .firstName("Sanjeev")
//                .lastName("Kumar S")
//                .email("Sanjeev@gmail,com")
//                .build();
        storeRepository.save(stores);
        // String firstName = "Sanjeev";
        // String lastName = "Kumar S";

        // when -  action or the behaviour that we are going test
        Stores savedStores = storeRepository.findByNativeSQLNamed(stores.getFirstName(), stores.getLastName());

        // then - verify the output
        assertThat(savedStores).isNotNull();
    }

}
